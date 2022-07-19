package org.spacehq.openclassic.api.gui;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a gui component.
 */
public class GuiComponent {
	
	private List<GuiComponent> components = new ArrayList<GuiComponent>();
	
	private String name;
	private int x;
	private int y;
	private int width;
	private int height;
	private boolean focused;
	private GuiComponent parent;
	private boolean visible = true;
	
	public GuiComponent(String name) {
		this.name = name;
	}
	
	public GuiComponent(String name, int x, int y, int width, int height) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Gets the component's name.
	 * @return The component's name.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Gets the component's x value.
	 * @return The component's x value.
	 */
	public int getX() {
		return this.x;
	}
	
	/**
	 * Gets the component's y value.
	 * @return The component's y value.
	 */
	public int getY() {
		return this.y;
	}
	
	/**
	 * Sets the position of this component.
	 * @param x The component's new x value.
	 * @param y The component's new y value.
	 */
	public void setPos(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Gets the component's width.
	 * @return The component's width.
	 */
	public int getWidth() {
		return this.width;
	}
	
	/**
	 * Gets the component's height.
	 * @return The component's height.
	 */
	public int getHeight() {
		return this.height;
	}
	
	/**
	 * Sets the size of this component.
	 * @param width The component's new width.
	 * @param height The component's new height.
	 */
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Gets whether this component is focused on.
	 * @return Whether this component has focus.
	 */
	public boolean isFocused() {
		return this.focused;
	}
	
	/**
	 * Sets whether this component is focused on.
	 * @return Whether this component has focus.
	 */
	public void setFocused(boolean focused) {
		this.focused = focused;
		if(focused && this.parent != null) {
			for(GuiComponent component : this.parent.getComponents()) {
				if(component != this && component.isFocused()) {
					component.setFocused(false);
				}
			}
		}
	}
	
	/**
	 * Gets whether this component is visible.
	 * @return Whether this component is visible.
	 */
	public boolean isVisible() {
		return this.visible;
	}
	
	/**
	 * Sets whether this component is visible.
	 * @return Whether this component is visible.
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	/**
	 * Gets whether this component is attached to another.
	 * @return Whether this component is attached.
	 */
	public boolean isAttached() {
		return this.parent != null;
	}
	
	/**
	 * Gets this component's parent.
	 * @return This component's parent.
	 */
	public GuiComponent getParent() {
		return this.parent;
	}
	
	/**
	 * Called when the component is attached to another.
	 */
	public void onAttached(GuiComponent parent) {
	}
	
	/**
	 * Called when the component is removed from another.
	 */
	public void onRemoved(GuiComponent parent) {
	}
	
	/**
	 * Called when the mouse is clicked in this component.
	 * @param x X of the mouse relative to the component.
	 * @param y Y of the mouse relative to the component.
	 * @param button ID of the clicked button.
	 */
	public void onMouseClick(int x, int y, int button) {
		if(!this.isFocused()) {
			this.setFocused(true);
		}
		
		for(GuiComponent curr : this.getComponents()) {
			if(x >= curr.getX() && y >= curr.getY() && x < curr.getX() + curr.getWidth() && y < curr.getY() + curr.getHeight()) {
				curr.onMouseClick(x - curr.getX(), y - curr.getY(), button);
			}
		}
	}
	
	/**
	 * Called when a key is pressed and this component is focused on.
	 * @param c Character resulting from the key press if applicable.
	 * @param key ID of the pressed key.
	 */
	public void onKeyPress(char c, int key) {
		for(GuiComponent curr : this.getComponents()) {
			if(curr.isFocused()) {
				curr.onKeyPress(c, key);
			}
		}
	}
	
	private void attachTo(GuiComponent parent) {
		if(this.isAttached()) {
			this.detach();
		}
		
		this.parent = parent;
		this.clearComponents();
		this.onAttached(parent);
	}
	
	private void detach() {
		GuiComponent old = this.parent;
		this.parent = null;
		this.onRemoved(old);
	}
	
	/**
	 * Attaches a component to this component.
	 * @param component Component to attach.
	 */
	public void attachComponent(GuiComponent component) {
		this.components.add(component);
		component.attachTo(this);
	}
	
	/**
	 * Removes a component from this component.
	 * @param name Name of the component.
	 */
	public void removeComponent(String name) {
		for(GuiComponent component : this.getComponents()) {
			if(component.getName().equals(name)) {
				this.removeComponent(component);
			}
		}
	}
	
	/**
	 * Removes a component from this component.
	 * @param component Component to remove.
	 */
	public void removeComponent(GuiComponent component) {
		this.components.remove(component);
		component.detach();
	}
	
	/**
	 * Clears the component list.
	 */
	public void clearComponents() {
		for(GuiComponent component : this.components) {
			component.detach();
		}
		
		this.components.clear();
	}
	
	/**
	 * Gets the component with the given name.
	 * @param name Name to look for.
	 * @return The component.
	 */
	public GuiComponent getComponent(String name) {
		for(GuiComponent component : this.components) {
			if(component.getName().equals(name)) {
				return component;
			}
		}
		
		return null;
	}
	
	/**
	 * Gets the component with the given name and type.
	 * @param name Name to look for.
	 * @param type Type of component to look for.
	 * @return The component.
	 */
	@SuppressWarnings("unchecked")
	public <T extends GuiComponent> T getComponent(String name, Class<T> type) {
		for(GuiComponent component : this.components) {
			if(component.getName().equals(name) && type.isInstance(component)) {
				return (T) component;
			}
		}
		
		return null;
	}
	
	/**
	 * Gets a list of all components attached to this component.
	 * @return All of the attached components.
	 */
	public List<GuiComponent> getComponents() {
		return new ArrayList<GuiComponent>(this.components);
	}
	
	/**
	 * Called when a tick update occurs.
	 * @param mouseX The x value of the mouse relative to the component.
	 * @param mouseY The y value of the mouse relative to the component.
	 */
	public void update(int mouseX, int mouseY) {
		for(GuiComponent component : this.getComponents()) {
			component.update(mouseX - component.getX(), mouseY - component.getY());
		}
	}

	/**
	 * Renders the component.
	 * @param delta Delta of the time between the last tick the current one.
	 * @param mouseX The x value of the mouse relative to the component.
	 * @param mouseY The y value of the mouse relative to the component.
	 */
	public void render(float delta, int mouseX, int mouseY) {
		if(this.isVisible()) {
			for(GuiComponent component : this.getComponents()) {
				if(component.isVisible()) {
					component.render(delta, mouseX - component.getX(), mouseY - component.getY());
				}
			}
		}
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
}
