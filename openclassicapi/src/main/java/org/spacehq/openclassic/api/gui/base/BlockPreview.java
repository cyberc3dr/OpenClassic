package org.spacehq.openclassic.api.gui.base;

import org.spacehq.openclassic.api.block.BlockType;
import org.spacehq.openclassic.api.gui.GuiComponent;

/**
 * A preview of a block, like in the inventory quickbar.
 */
public class BlockPreview extends GuiComponent {

	private BlockType type;
	private float scale;
	private int popTime = 0;
	private boolean rotating = false;
	private float yaw = 45;
	private float pitch = -30;

	public BlockPreview(String name, int x, int y, BlockType type) {
		this(name, x, y, type, 0);
	}

	public BlockPreview(String name, int x, int y, BlockType type, float scale) {
		super(name, x, y, 0, 0);
		this.type = type;
		this.scale = scale;
	}

	/**
	 * Gets the block being previewed.
	 * @return The block being previewed.
	 */
	public BlockType getBlock() {
		return this.type;
	}

	/**
	 * Sets the block being previewed.
	 * @param block The new block being previewed.
	 */
	public void setBlock(BlockType block) {
		this.type = block;
	}

	/**
	 * Gets the scale of this block preview component.
	 * @return The preview's scale.
	 */
	public float getScale() {
		return this.scale;
	}
	
	/**
	 * Gets whether this block preview is rotating.
	 * @return Whether this block preview is rotating.
	 */
	public boolean isRotating() {
		return this.rotating;
	}
	
	/**
	 * Sets whether this block preview is rotating.
	 * @param rotating Whether this block preview should rotate.
	 */
	public void setRotating(boolean rotating) {
		boolean was = this.rotating;
		this.rotating = rotating;
		if(this.rotating && !was) {
			this.pitch = -25;
		} else if(!this.rotating && was) {
            this.yaw = 45;
            this.pitch = -30;
		}
	}

	/**
	 * Triggers a pop animation for this block preview component.
	 */
	public void pop() {
		this.popTime = 5;
	}

	@Override
	public void update(int mouseX, int mouseY) {
		this.popTime--;
		if(this.rotating) {
			this.pitch = -25;
			if(this.yaw >= 360) {
				this.yaw = 0;
			}
	
			this.yaw += 5;
		} else {
            this.yaw = 45;
            this.pitch = -30;
		}
		
		super.update(mouseX, mouseY);
	}

	@Override
	public void render(float delta, int mouseX, int mouseY) {
		ComponentHelper.getHelper().renderBlockPreview(this, this.popTime, this.yaw, this.pitch, delta);
	}

}
