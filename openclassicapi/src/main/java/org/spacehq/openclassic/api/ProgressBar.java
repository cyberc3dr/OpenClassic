package org.spacehq.openclassic.api;

public interface ProgressBar {

	/**
	 * Gets the title the progress bar is showing.
	 * @return The progress bar's title.
	 */
	public String getTitle();
	
	/**
	 * Sets the title of the progress bar.
	 * @param title Title to set.
	 */
	public void setTitle(String title);
	
	/**
	 * Gets the subtitle the progress bar is showing.
	 * @return The progress bar's subtitle.
	 */
	public String getSubtitle();
	
	/**
	 * Sets the subtitle of the progress bar.
	 * @param subtitle Subtitle to set.
	 */
	public void setSubtitle(String subtitle);
	
	/**
	 * Gets the text the progress bar is showing.
	 * @return The progress bar's text.
	 */
	public String getText();
	
	/**
	 * Sets the text of the progress bar.
	 * @param text Text to set.
	 */
	public void setText(String text);
	
	/**
	 * Gets the progress bar's progress.
	 * @return The progress bar's progress.
	 */
	public int getProgress();
	
	/**
	 * Sets the progress bar's progress.
	 * @param progress Progress to set.
	 */
	public void setProgress(int progress);
	
	/**
	 * Returns true if the progress bar subtitle is scaled.
	 * @return True if the progress bar subtitle is scaled.
	 */
	public boolean isSubtitleScaled();
	
	/**
	 * Sets whether the progress bar subtitle is scaled.
	 * @param scaled Whether the progress bar subtitle is scaled.
	 */
	public void setSubtitleScaled(boolean scaled);
	
	/**
	 * Returns true if the progress bar is currently visible.
	 * @return True if the progress bar is visible.
	 */
	public boolean isVisible();
	
	/**
	 * Sets whether the progress bar is visible.
	 * @param visible Whether the progress bar is visible.
	 */
	public void setVisible(boolean visible);
	
	/**
	 * Renders the progress bar display on to the screen.
	 */
	public void render();
	
	/**
	 * Renders only the progress bar on to the screen.
	 */
	public void renderBar();
	
}
