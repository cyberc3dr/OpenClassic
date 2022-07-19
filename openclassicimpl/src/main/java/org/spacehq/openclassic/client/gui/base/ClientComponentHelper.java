package org.spacehq.openclassic.client.gui.base;

import org.lwjgl.opengl.GL11;

import org.spacehq.openclassic.api.block.model.Texture;
import org.spacehq.openclassic.api.gui.base.BlockPreview;
import org.spacehq.openclassic.api.gui.base.Button;
import org.spacehq.openclassic.api.gui.base.DefaultBackground;
import org.spacehq.openclassic.api.gui.base.FadingBox;
import org.spacehq.openclassic.api.gui.base.Image;
import org.spacehq.openclassic.api.gui.base.Label;
import org.spacehq.openclassic.api.gui.base.TextBox;
import org.spacehq.openclassic.api.gui.base.TranslucentBackground;
import org.spacehq.openclassic.api.gui.base.ComponentHelper;
import org.spacehq.openclassic.client.render.Textures;
import org.spacehq.openclassic.client.render.RenderHelper;

public class ClientComponentHelper extends ComponentHelper {

	@Override
	public int getStringWidth(String string, boolean scaled) {
		int width = RenderHelper.getHelper().getStringWidth(string);
		if(scaled) {
			width *= 2;
		}
		
		return width;
	}

	@Override
	public void renderBlockPreview(BlockPreview preview, int popTime, float yaw, float pitch, float delta) {
		if(preview.getBlock() != null) {
			RenderHelper.getHelper().drawRotatedBlock(preview.getX(), preview.getY(), preview.getBlock(), preview.getScale(), popTime, yaw, pitch, delta);
		}
	}

	@Override
	public void renderButton(Button button, int mouseX, int mouseY) {
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		Texture texture = Textures.BUTTON;
		boolean hover = mouseX >= 0 && mouseY >= 0 && mouseX < button.getWidth() && mouseY < button.getHeight();
		if (!button.isActive()) {
			texture = Textures.BUTTON_INACTIVE;
		} else if (hover) {
			texture = Textures.BUTTON_HOVER;
		}

        Texture topLeft = texture.getSubTexture(0, 0, button.getWidth() / 2, button.getHeight() / 2);
        Texture topRight = texture.getSubTexture(400 - button.getWidth() / 2, 0, button.getWidth() / 2, button.getHeight() / 2);
        Texture bottomLeft = texture.getSubTexture(0, 40 - button.getHeight() / 2, button.getWidth() / 2, button.getHeight() / 2);
        Texture bottomRight = texture.getSubTexture(400 - button.getWidth() / 2, 40 - button.getHeight() / 2, button.getWidth() / 2, button.getHeight() / 2);
        RenderHelper.getHelper().drawTexture(topLeft, button.getX(), button.getY(), 1);
        RenderHelper.getHelper().drawTexture(topRight, button.getX() + button.getWidth() / 2, button.getY(), 1);
        RenderHelper.getHelper().drawTexture(bottomLeft, button.getX(), button.getY() + button.getHeight() / 2, 1);
        RenderHelper.getHelper().drawTexture(bottomRight, button.getX() + button.getWidth() / 2, button.getY() + button.getHeight() / 2, 1);
        
		String message = button.getText();
		if(message.length() > 30) {
			message = message.substring(0, 30) + "...";
		}
		
		if (!button.isActive()) {
			RenderHelper.getHelper().renderText(message, button.getX() + button.getWidth() / 2, button.getY() + (button.getHeight() - 16) / 2, -6250336, true);
		} else if (hover) {
			RenderHelper.getHelper().renderText(message, button.getX() + button.getWidth() / 2, button.getY() + (button.getHeight() - 16) / 2, 16777120, true);
		} else {
			RenderHelper.getHelper().renderText(message, button.getX() + button.getWidth() / 2, button.getY() + (button.getHeight() - 16) / 2, 14737632, true);
		}
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	@Override
	public void renderDefaultBackground(DefaultBackground background) {
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		RenderHelper.getHelper().drawDefaultBG(background.getX(), background.getY(), background.getWidth(), background.getHeight());
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	@Override
	public void renderFadingBox(FadingBox box) {
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		RenderHelper.getHelper().color(box.getX(), box.getY(), box.getX() + box.getWidth(), box.getY() + box.getHeight(), box.getColor(), box.getFadeColor());
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	@Override
	public void renderImage(Image image) {
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		RenderHelper.getHelper().drawStretchedTexture(image.getTexture(), image.getX(), image.getY(), image.getWidth(), image.getHeight());
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	@Override
	public void renderLabel(Label label) {
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		if(label.isScaled()) {
			RenderHelper.getHelper().renderScaledText(label.getText(), label.getX(), label.getY(), false);
		} else {
			RenderHelper.getHelper().renderText(label.getText(), label.getX(), label.getY(), false);
		}
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	@Override
	public void renderTextBox(TextBox box) {
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		if(!box.isChatBox()) {
			RenderHelper.getHelper().drawBox(box.getX() - 2, box.getY() - 2, box.getX() + box.getWidth() + 2, box.getY() + box.getHeight() + 2, -6250336);
		}
		
		RenderHelper.getHelper().drawBox(box.getX(), box.getY(), box.getX() + box.getWidth(), box.getY() + box.getHeight(), (!box.isChatBox() ? -16777216 : Integer.MIN_VALUE));
		String render = box.getText();
		RenderHelper.getHelper().renderText(render.substring(0, box.getCursorPosition()) + (box.getBlinkState() && box.isFocused() ? "|" : "") + render.substring(box.getCursorPosition(), render.length()), box.getX() + 8, (box.isChatBox() ? box.getY() + 4 : box.getY() + 12), 14737632, false);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	@Override
	public void renderTranslucentBackground(TranslucentBackground background) {
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		RenderHelper.getHelper().color(background.getX(), background.getY(), background.getWidth(), background.getHeight(), 1610941696, -1607454624);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

}
