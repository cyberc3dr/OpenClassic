package org.spacehq.openclassic.api.event.level;

import com.zachsthings.onevent.Event;

import org.spacehq.openclassic.api.level.Level;

/**
 * Represents an event involving a level.
 */
public abstract class LevelEvent extends Event {

	private Level level;
	
	public LevelEvent(Level level) {
		this.level = level;
	}
	
	/**
	 * Gets the level involved in this event.
	 * @return The level involved.
	 */
	public Level getLevel() {
		return this.level;
	}
	
}
