package org.spacehq.openclassic.game.scheduler;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ClassicFuture<T> extends ClassicTask implements Future<T> {

	private Callable<T> callable;
	private T value;
	private Exception exception = null;

	public ClassicFuture(Callable<T> callable, Object owner, int id) {
		super(owner, null, id, -1);
		this.callable = callable;
	}

	@Override
	public T get() throws CancellationException, InterruptedException, ExecutionException {
		try {
			return this.get(0, TimeUnit.MILLISECONDS);
		} catch(TimeoutException e) {
			throw new Error(e);
		}
	}

	@Override
	public synchronized T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		timeout = unit.toMillis(timeout);
		long period = this.getPeriod();
		long timestamp = timeout > 0 ? System.currentTimeMillis() : 0;
		while(true) {
			if(period == -1 || period == -3) {
				this.wait(timeout);
				period = this.getPeriod();
				if(period == -1 || period == -3) {
					if(timeout == 0) {
						continue;
					}

					timeout += timestamp - (timestamp = System.currentTimeMillis());
					if(timeout > 0) {
						continue;
					}

					throw new TimeoutException();
				}
			}

			if(period == -2) {
				throw new CancellationException();
			}

			if(period == -4) {
				if(this.exception == null) {
					return this.value;
				}

				throw new ExecutionException(this.exception);
			}

			throw new IllegalStateException("Expected -1 to -4, got " + period);
		}
	}

	@Override
	public void run() {
		synchronized(this) {
			if(this.getPeriod() == -2) {
				return;
			}

			this.setPeriod(-3);
		}

		try {
			this.value = this.callable.call();
		} catch(Exception e) {
			this.exception = e;
		} finally {
			synchronized(this) {
				this.setPeriod(-4);
				this.notifyAll();
			}
		}
	}

	@Override
	public boolean isCancelled() {
		return this.getPeriod() == -2;
	}

	@Override
	public synchronized boolean cancel(boolean interrupt) {
		if(this.getPeriod() != -1) {
			return false;
		}

		this.setPeriod(-2);
		return true;
	}

	@Override
	public synchronized boolean cancelInternal() {
		if(this.getPeriod() != -1) {
			return false;
		}

		this.setPeriod(-2);
		this.notifyAll();
		return true;
	}

	@Override
	public boolean isDone() {
		return this.getPeriod() != -1 && this.getPeriod() != -3;
	}

}