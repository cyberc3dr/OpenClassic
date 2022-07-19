package org.spacehq.openclassic.api.scheduler;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * A task scheduler for async or sync tasks.
 */
public interface Scheduler {

	/**
	 * Schedules a sync task.
	 * @param obj Object this task belongs to.
	 * @param task The task to schedule.
	 * @return The task's id.
	 */
	public int scheduleTask(Object obj, Runnable task);
	
	/**
	 * Schedules a delayed sync task.
	 * @param obj Object this task belongs to.
	 * @param task The task to schedule.
	 * @param delay The period to delay the task by.
	 * @return The task's id.
	 */
	public int scheduleDelayedTask(Object obj, Runnable task, long delay);
	
	/**
	 * Schedules a repeating sync task.
	 * @param obj Object this task belongs to.
	 * @param task The task to schedule.
	 * @param delay The period to delay the task by.
	 * @param period The period the task will last for.
	 * @return The task's id.
	 */
	public int scheduleRepeatingTask(Object obj, Runnable task, long delay, long period);
	
	/**
	 * Schedules an async task.
	 * @param obj Object this task belongs to.
	 * @param task The task to schedule.
	 * @return The task's id.
	 */
	public int scheduleAsyncTask(Object obj, Runnable task);
	
	/**
	 * Schedules a delayed async task.
	 * @param obj Object this task belongs to.
	 * @param task The task to schedule.
	 * @param delay The period to delay the task by.
	 * @return The task's id.
	 */
	public int scheduleAsyncDelayedTask(Object obj, Runnable task, long delay);
	
	/**
	 * Schedules a repeating async task.
	 * @param obj Object this task belongs to.
	 * @param task The task to schedule.
	 * @param delay The period to delay the task by.
	 * @param period The period the task will last for.
	 * @return The task's id.
	 */
	public int scheduleAsyncRepeatingTask(Object obj, Runnable task, long delay, long period);
	
    /**
     * Calls a method on the main thread and returns a Future object
     * This task will be executed by the main thread
     * 
     * Do NOT call the Future.get() method from the main thread.
     * Additionally, there is at least an average of 10ms latency until the isDone() method returns true
     *
     * @param obj Object this task belongs to.
     * @param task The task to be executed
     * @return The future object related to the task
     */
	public <T> Future<T> callSyncMethod(Object obj, Callable<T> task);
	
	/**
	 * Cancels a task with the given ID.
	 * @param id ID of the task to cancel.
	 */
	public void cancelTask(int id);
	
	/**
	 * Cancels all tasks belonging to the given object.
	 * @param obj Owner to cancel the tasks of.
	 */
	public void cancelTasks(Object obj);
	
	/**
	 * Cancels all tasks.
	 */
	public void cancelAllTasks();
	
	/**
	 * Returns true if a task is running.
	 * @return True if a task is running.
	 */
	public boolean isRunning(int id);
	
	/**
	 * Returns true if a task is queued.
	 * @return True if a task is queued.
	 */
	public boolean isQueued(int id);
	
	/**
	 * Gets a list of active async workers.
	 * @return A list of active async workers.
	 */
	public List<Worker> getActiveWorkers();
	
	/**
	 * Gets a list of pending tasks.
	 * @return A list of pending tasks.
	 */
	public List<Task> getPendingTasks();
	
}
