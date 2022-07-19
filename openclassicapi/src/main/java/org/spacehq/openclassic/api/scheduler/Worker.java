package org.spacehq.openclassic.api.scheduler;

/**
 * Represents an async worker.
 */
public interface Worker {

	/**
	 * Gets the task ID of the task this worker is executing.
	 * @return The task's ID.
	 */
	public int getTaskId();
	
	/**
	 * Gets the owner of the task this worker is executing.
	 * @return The task's owner.
	 */
	public Object getOwner();
	
	/**
	 * Gets the thread this worker is using.
	 * @return The worker's thread.
	 */
	public Thread getThread();
	
}
