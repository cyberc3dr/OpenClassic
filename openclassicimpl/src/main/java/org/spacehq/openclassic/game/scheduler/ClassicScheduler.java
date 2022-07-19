package org.spacehq.openclassic.game.scheduler;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.plugin.Plugin;
import org.spacehq.openclassic.api.scheduler.Scheduler;
import org.spacehq.openclassic.api.scheduler.Task;
import org.spacehq.openclassic.api.scheduler.Worker;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class ClassicScheduler implements Scheduler {

	private AtomicInteger ids = new AtomicInteger(1);
	private volatile ClassicTask head = new ClassicTask();
	private AtomicReference<ClassicTask> tail = new AtomicReference<ClassicTask>(head);
	private PriorityQueue<ClassicTask> pending = new PriorityQueue<ClassicTask>(10, new Comparator<ClassicTask>() {
		@Override
		public int compare(ClassicTask o1, ClassicTask o2) {
			return (int) (o1.getNextRun() - o2.getNextRun());
		}
	});

	private List<ClassicTask> temp = new ArrayList<ClassicTask>();
	private ConcurrentHashMap<Integer, ClassicTask> runners = new ConcurrentHashMap<Integer, ClassicTask>();
	private volatile int ticks = -1;
	private ExecutorService executor = Executors.newCachedThreadPool();

	public void shutdown() {
		this.cancelAllTasks();
		this.executor.shutdownNow();
	}
	
	@Override
	public int scheduleTask(Object owner, Runnable task) {
		return this.scheduleDelayedTask(owner, task, 0);
	}

	@Override
	public int scheduleAsyncTask(Object owner, Runnable task) {
		return this.scheduleAsyncDelayedTask(owner, task, 0);
	}

	@Override
	public int scheduleDelayedTask(Object owner, Runnable task, long delay) {
		return this.scheduleRepeatingTask(owner, task, delay, -1);
	}

	@Override
	public int scheduleAsyncDelayedTask(Object owner, Runnable task, long delay) {
		return this.scheduleAsyncRepeatingTask(owner, task, delay, -1);
	}

	@Override
	public int scheduleRepeatingTask(Object owner, Runnable runnable, long delay, long period) {
		validate(owner, runnable);
		if(delay < 0) {
			delay = 0;
		}

		if(period == 0) {
			period = 1;
		} else if(period < -1) {
			period = -1;
		}

		return this.handle(new ClassicTask(owner, runnable, nextId(), period), delay);
	}

	@Override
	public int scheduleAsyncRepeatingTask(Object owner, Runnable runnable, long delay, long period) {
		validate(owner, runnable);
		if(delay < 0) {
			delay = 0;
		}

		if(period == 0) {
			period = 1;
		} else if(period < -1) {
			period = -1;
		}

		return this.handle(new ClassicAsyncTask(this.runners, owner, runnable, nextId(), period), delay);
	}

	@Override
	public <T> Future<T> callSyncMethod(Object owner, Callable<T> task) {
		validate(owner, task);
		ClassicFuture<T> future = new ClassicFuture<T>(task, owner, nextId());
		this.handle(future, 0);
		return future;
	}

	@Override
	public void cancelTask(final int taskId) {
		if(taskId <= 0) {
			return;
		}

		ClassicTask task = this.runners.get(taskId);
		if(task != null) {
			task.cancelInternal();
		}

		task = new ClassicTask(new Runnable() {
			@Override
			public void run() {
				if(!this.check(temp)) {
					this.check(pending);
				}
			}

			private boolean check(Iterable<ClassicTask> collection) {
				Iterator<ClassicTask> tasks = collection.iterator();
				while(tasks.hasNext()) {
					ClassicTask task = tasks.next();
					if(task.getTaskId() == taskId) {
						task.cancelInternal();
						tasks.remove();
						if(task.isSync()) {
							runners.remove(taskId);
						}
						return true;
					}
				}

				return false;
			}
		});

		this.handle(task, 0);
		for(ClassicTask pending = this.head.getNext(); pending != null; pending = pending.getNext()) {
			if(pending == task) {
				return;
			}

			if(pending.getTaskId() == taskId) {
				pending.cancelInternal();
			}
		}
	}

	@Override
	public void cancelTasks(final Object owner) {
		if(owner == null) {
			throw new IllegalArgumentException("Cannot cancel tasks of null owner.");
		}

		ClassicTask task = new ClassicTask(new Runnable() {
			@Override
			public void run() {
				this.check(pending);
				this.check(temp);
			}

			private void check(Iterable<ClassicTask> collection) {
				Iterator<ClassicTask> tasks = collection.iterator();
				while(tasks.hasNext()) {
					ClassicTask task = tasks.next();
					if(task.getOwner().equals(owner)) {
						task.cancelInternal();
						tasks.remove();
						if(task.isSync()) {
							runners.remove(task.getTaskId());
						}
					}
				}
			}
		});

		this.handle(task, 0);
		for(ClassicTask pending = this.head.getNext(); pending != null; pending = pending.getNext()) {
			if(pending == task) {
				return;
			}

			if(pending.getTaskId() != -1 && pending.getOwner().equals(owner)) {
				pending.cancelInternal();
			}
		}

		for(ClassicTask runner : this.runners.values()) {
			if(runner.getOwner().equals(owner)) {
				runner.cancelInternal();
			}
		}
	}

	@Override
	public void cancelAllTasks() {
		ClassicTask task = new ClassicTask(new Runnable() {
			@Override
			public void run() {
				Iterator<ClassicTask> it = ClassicScheduler.this.runners.values().iterator();
				while(it.hasNext()) {
					ClassicTask task = it.next();
					task.cancelInternal();
					if(task.isSync()) {
						it.remove();
					}
				}

				pending.clear();
				temp.clear();
			}
		});

		this.handle(task, 0);
		for(ClassicTask pending = this.head.getNext(); pending != null; pending = pending.getNext()) {
			if(pending == task) {
				break;
			}
			pending.cancelInternal();
		}

		for(ClassicTask runner : this.runners.values()) {
			runner.cancelInternal();
		}
	}

	@Override
	public boolean isRunning(int taskId) {
		ClassicTask task = this.runners.get(taskId);
		if(task == null || task.isSync()) {
			return false;
		}

		ClassicAsyncTask asyncTask = (ClassicAsyncTask) task;
		synchronized(asyncTask.getWorkers()) {
			return asyncTask.getWorkers().isEmpty();
		}
	}

	@Override
	public boolean isQueued(int taskId) {
		if(taskId <= 0) {
			return false;
		}

		for(ClassicTask task = this.head.getNext(); task != null; task = task.getNext()) {
			if(task.getTaskId() == taskId) {
				return task.getPeriod() >= -1;
			}
		}

		ClassicTask task = this.runners.get(taskId);
		return task != null && task.getPeriod() >= -1;
	}

	@Override
	public List<Worker> getActiveWorkers() {
		ArrayList<Worker> workers = new ArrayList<Worker>();
		for(ClassicTask t : this.runners.values()) {
			if(t.isSync()) {
				continue;
			}

			ClassicAsyncTask task = (ClassicAsyncTask) t;
			synchronized(task.getWorkers()) {
				workers.addAll(task.getWorkers());
			}
		}

		return workers;
	}

	@Override
	public List<Task> getPendingTasks() {
		ArrayList<ClassicTask> truePending = new ArrayList<ClassicTask>();
		for(ClassicTask task = this.head.getNext(); task != null; task = task.getNext()) {
			if(task.getTaskId() != -1) {
				truePending.add(task);
			}
		}

		ArrayList<Task> pending = new ArrayList<Task>();
		for(ClassicTask task : this.runners.values()) {
			if(task.getPeriod() >= -1) {
				pending.add(task);
			}
		}

		for(ClassicTask task : truePending) {
			if(task.getPeriod() >= -1 && !pending.contains(task)) {
				pending.add(task);
			}
		}

		return pending;
	}

	public void tick(int ticks) {
		this.ticks = ticks;
		this.parsePending();
		while(this.isReady(ticks)) {
			ClassicTask task = this.pending.remove();
			if(task.getPeriod() < -1) {
				if(task.isSync()) {
					this.runners.remove(task.getTaskId(), task);
				}

				this.parsePending();
				continue;
			}

			if(task.isSync()) {
				try {
					task.run();
				} catch(Throwable t) {
					OpenClassic.getLogger().warning(String.format("Task #%s of %s generated an exception", task.getTaskId(), task.getOwner()));
					t.printStackTrace();
				}

				this.parsePending();
			} else {
				this.executor.execute(task);
			}

			long period = task.getPeriod();
			if(period > 0) {
				task.setNextRun(ticks + period);
				this.temp.add(task);
			} else if(task.isSync()) {
				this.runners.remove(task.getTaskId());
			}
		}

		this.pending.addAll(this.temp);
		this.temp.clear();
	}

	private void addTask(ClassicTask task) {
		ClassicTask tailTask = this.tail.get();
		while(!this.tail.compareAndSet(tailTask, task)) {
			tailTask = this.tail.get();
		}

		tailTask.setNext(task);
	}

	private int handle(ClassicTask task, long delay) {
		task.setNextRun(this.ticks + delay);
		this.addTask(task);
		return task.getTaskId();
	}

	private static void validate(Object owner, Object task) {
		if(owner == null) {
			throw new IllegalArgumentException("Owner cannot be null.");
		}

		if(task == null) {
			throw new IllegalArgumentException("Task cannot be null.");
		}

		if(owner instanceof Plugin && !((Plugin) owner).isEnabled()) {
			throw new IllegalStateException("Plugin attempted to register task while disabled");
		}
	}

	private int nextId() {
		return this.ids.incrementAndGet();
	}

	private void parsePending() {
		ClassicTask head = this.head;
		ClassicTask task = head.getNext();
		ClassicTask lastTask = null;
		for(lastTask = head; task != null; task = (lastTask = task).getNext()) {
			if(task.getTaskId() == -1) {
				task.run();
			} else if(task.getPeriod() >= -1) {
				this.pending.add(task);
				this.runners.put(task.getTaskId(), task);
			}
		}

		for(task = head; task != lastTask; task = head) {
			head = task.getNext();
			task.setNext(null);
		}

		this.head = lastTask;
	}

	private boolean isReady(int currentTick) {
		return !this.pending.isEmpty() && this.pending.peek().getNextRun() <= currentTick;
	}

}
