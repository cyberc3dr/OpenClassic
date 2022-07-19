package org.spacehq.openclassic.api.scheduler;

/**
 * Represents a scheduled task.
 */
public interface Task {

	/**
	 * Gets the task ID.
	 * @return The task ID.
	 */
	public int getTaskId();
	
	/**
	 * Gets the owner of this task.
	 * @return The task's owner.
	 */
	public Object getOwner();
	
	/**
	 * Returns true if this task is sync.
	 * @return If the task is sync.
	 */
	public boolean isSync();
	
}
