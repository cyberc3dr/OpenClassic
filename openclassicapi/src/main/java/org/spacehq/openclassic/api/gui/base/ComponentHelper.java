package org.spacehq.openclassic.api.gui.base;

/**
 * A helper class for use with creating GuiComponents.
 */
public abstract class ComponentHelper {

	private static ComponentHelper helper;
	
	/**
	 * Gets the helper instance.
	 * @return The helper instance.
	 */
	public static ComponentHelper getHelper() {
		return ComponentHelper.helper;
	}
	
	/**
	 * Sets the helper instance if it is not already set.
	 * @param factory Helper instance to use.
	 */
	public static void setHelper(ComponentHelper factory) {
		if(ComponentHelper.helper != null) {
			return;
		}
		
		ComponentHelper.helper = factory;
	}
	
	/**
	 * Gets the rendering width of a string.
	 * @param string String to get the width of.
	 * @param scaled Whether the rendered text is scaled 2x.
	 * @return The width of the string.
	 */
	public abstract int getStringWidth(String string, boolean scaled);

	/**
	 * Renders a block preview component.
	 * @param preview Component to render.
	 * @param pitch 
	 * @param yaw 
	 */
	public abstract void renderBlockPreview(BlockPreview preview, int popTime, float yaw, float pitch, float delta);

	/**
	 * Renders a button component.
	 * @param preview Component to render.
	 */
	public abstract void renderButton(Button button, int mouseX, int mouseY);

	/**
	 * Renders a default background component.
	 * @param preview Component to render.
	 */
	public abstract void renderDefaultBackground(DefaultBackground background);

	/**
	 * Renders a fading box component.
	 * @param preview Component to render.
	 */
	public abstract void renderFadingBox(FadingBox box);

	/**
	 * Renders an image component.
	 * @param preview Component to render.
	 */
	public abstract void renderImage(Image image);

	/**
	 * Renders a label component.
	 * @param preview Component to render.
	 */
	public abstract void renderLabel(Label label);

	/**
	 * Renders a text box component.
	 * @param preview Component to render.
	 */
	public abstract void renderTextBox(TextBox box);

	/**
	 * Renders a translucent background component.
	 * @param preview Component to render.
	 */
	public abstract void renderTranslucentBackground(TranslucentBackground background);
	
}
