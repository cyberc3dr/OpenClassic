package org.spacehq.openclassic.game.scheduler;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.scheduler.Worker;

public class ClassicAsyncTask extends ClassicTask {

	private LinkedList<Worker> workers = new LinkedList<Worker>();
	private Map<Integer, ClassicTask> runners;

	public ClassicAsyncTask(Map<Integer, ClassicTask> runners, Object owner, Runnable task, int id, long delay) {
		super(owner, task, id, delay);
		this.runners = runners;
	}

	@Override
	public boolean isSync() {
		return false;
	}

	@Override
	public void run() {
		final Thread thread = Thread.currentThread();
		synchronized(this.workers) {
			if(this.getPeriod() == -2) {
				return;
			}

			this.workers.add(new Worker() {
				@Override
				public Thread getThread() {
					return thread;
				}

				@Override
				public int getTaskId() {
					return ClassicAsyncTask.this.getTaskId();
				}

				@Override
				public Object getOwner() {
					return ClassicAsyncTask.this.getOwner();
				}
			});
		}

		try {
			super.run();
		} catch(Throwable t) {
			OpenClassic.getLogger().severe(String.format("%s generated an exception while executing task %s", this.getOwner(), this.getTaskId()));
			t.printStackTrace();
		} finally {
			synchronized(this.workers) {
				try {
					Iterator<Worker> workers = this.workers.iterator();
					boolean removed = false;
					while(workers.hasNext()) {
						if(workers.next().getThread() == thread) {
							workers.remove();
							removed = true;
							break;
						}
					}

					if(!removed) {
						throw new IllegalStateException(String.format("Unable to remove worker %s on task %s for %s", thread.getName(), getTaskId(), getOwner()));
					}
				} finally {
					if(this.getPeriod() < 0 && this.workers.isEmpty()) {
						this.runners.remove(getTaskId());
					}
				}
			}
		}
	}

	public LinkedList<Worker> getWorkers() {
		return this.workers;
	}

	@Override
	public boolean cancelInternal() {
		synchronized(this.workers) {
			this.setPeriod(-2);
			if(this.workers.isEmpty()) {
				this.runners.remove(getTaskId());
			}
		}

		return true;
	}
	
}
