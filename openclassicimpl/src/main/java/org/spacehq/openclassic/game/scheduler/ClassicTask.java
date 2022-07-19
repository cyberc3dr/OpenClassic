package org.spacehq.openclassic.game.scheduler;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.scheduler.Task;

public class ClassicTask implements Task, Runnable {

	private volatile ClassicTask next = null;
	private volatile long period;
	private long nextRun;
	private Runnable task;
	private Object owner;
	private int id;

	public ClassicTask() {
		this(null, null, -1, -1);
	}

	public ClassicTask(Runnable task) {
		this(null, task, -1, -1);
	}

	public ClassicTask(Object owner, Runnable task, int id, long period) {
		this.owner = owner;
		this.task = task;
		this.id = id;
		this.period = period;
	}

	@Override
	public int getTaskId() {
		return this.id;
	}

	@Override
	public Object getOwner() {
		return this.owner;
	}

	@Override
	public boolean isSync() {
		return true;
	}

	@Override
	public void run() {
		this.task.run();
	}

	public long getPeriod() {
		return this.period;
	}

	public void setPeriod(long period) {
		this.period = period;
	}

	public long getNextRun() {
		return this.nextRun;
	}

	public void setNextRun(long nextRun) {
		this.nextRun = nextRun;
	}

	public ClassicTask getNext() {
		return this.next;
	}

	public void setNext(ClassicTask next) {
		this.next = next;
	}

	public Class<? extends Runnable> getTaskClass() {
		return this.task.getClass();
	}

	public void cancel() {
		OpenClassic.getGame().getScheduler().cancelTask(this.id);
	}

	public boolean cancelInternal() {
		this.setPeriod(-2);
		return true;
	}

}
