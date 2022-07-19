package org.spacehq.openclassic.game.network.codec.custom;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.block.BlockFace;
import org.spacehq.openclassic.api.block.BlockType;
import org.spacehq.openclassic.api.block.StepSound;
import org.spacehq.openclassic.api.block.model.CuboidModel;
import org.spacehq.openclassic.api.block.model.Model;
import org.spacehq.openclassic.api.block.model.Quad;
import org.spacehq.openclassic.api.block.model.QuadFactory;
import org.spacehq.openclassic.api.block.model.Texture;
import org.spacehq.openclassic.api.block.model.TextureFactory;
import org.spacehq.openclassic.api.block.model.Vertex;
import org.spacehq.openclassic.api.math.BoundingBox;
import org.spacehq.openclassic.game.SubTexture;
import org.spacehq.openclassic.game.network.MessageCodec;
import org.spacehq.openclassic.game.network.msg.custom.block.CustomBlockMessage;
import org.spacehq.openclassic.server.util.ChannelBufferUtils;

public class CustomBlockCodec extends MessageCodec<CustomBlockMessage> {

	public CustomBlockCodec() {
		super(CustomBlockMessage.class, (byte) 0x11);
	}

	@Override
	public ChannelBuffer encode(CustomBlockMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeByte(message.getBlock().getId());
		buffer.writeByte(message.getBlock().isOpaque() ? 1 : 0);
		buffer.writeByte(message.getBlock().isSelectable() ? 1 : 0);
		ChannelBufferUtils.writeExtendedString(buffer, message.getBlock().getStepSound().name());
		buffer.writeByte(message.getBlock().isLiquid() ? 1 : 0);
		buffer.writeInt(message.getBlock().getTickDelay());
		buffer.writeByte(message.getBlock().getPreventsRendering() ? 1 : 0);
		buffer.writeByte(message.getBlock().canPlaceIn() ? 1 : 0);
		buffer.writeByte(message.getBlock().getPreventsOwnRenderingRaw() ? 1 : 0);
		buffer.writeFloat(message.getBlock().getBrightness());
		buffer.writeFloat(message.getBlock().getSpeedModifier());
		buffer.writeInt(message.getBlock().getFogRed());
		buffer.writeInt(message.getBlock().getFogGreen());
		buffer.writeInt(message.getBlock().getFogBlue());
		buffer.writeFloat(message.getBlock().getFogDensity());
		buffer.writeByte(message.getBlock().isUnbreakable() ? 1 : 0);
		buffer.writeByte(message.getBlock().getLiquidName() != null ? 1 : 0);
		if(message.getBlock().getLiquidName() != null) {
			ChannelBufferUtils.writeExtendedString(buffer, message.getBlock().getLiquidName());
		}
		
		this.writeModel(buffer, message.getBlock().getModel());
		Map<Model, BlockFace[]> outwardModels = message.getBlock().getOutwardModels();
		buffer.writeInt(outwardModels.size());
		for(Model model : outwardModels.keySet()) {
			BlockFace faces[] = outwardModels.get(model);
			buffer.writeInt(faces.length);
			for(BlockFace face : faces) {
				ChannelBufferUtils.writeExtendedString(buffer, face.name());
			}
			
			this.writeModel(buffer, model);
		}

		return buffer;
	}
	
	private void writeModel(ChannelBuffer buffer, Model model) {
		buffer.writeByte(model instanceof CuboidModel ? 1 : 0);
		if(model instanceof CuboidModel) {
			buffer.writeInt(((CuboidModel) model).getSubWidth());
			buffer.writeInt(((CuboidModel) model).getSubHeight());
		}
		
		buffer.writeByte(model.useCulling() ? 1 : 0);
		buffer.writeByte(model.getDefaultCollisionBox() != null ? (byte) 1 : (byte) 0);
		if(model.getDefaultCollisionBox() != null) {
			buffer.writeFloat(model.getDefaultCollisionBox().getX1());
			buffer.writeFloat(model.getDefaultCollisionBox().getX2());
			buffer.writeFloat(model.getDefaultCollisionBox().getY1());
			buffer.writeFloat(model.getDefaultCollisionBox().getY2());
			buffer.writeFloat(model.getDefaultCollisionBox().getZ1());
			buffer.writeFloat(model.getDefaultCollisionBox().getZ2());
		}

		buffer.writeByte(model.getDefaultSelectionBox() != null ? (byte) 1 : (byte) 0);
		if(model.getDefaultSelectionBox() != null) {
			buffer.writeFloat(model.getDefaultSelectionBox().getX1());
			buffer.writeFloat(model.getDefaultSelectionBox().getX2());
			buffer.writeFloat(model.getDefaultSelectionBox().getY1());
			buffer.writeFloat(model.getDefaultSelectionBox().getY2());
			buffer.writeFloat(model.getDefaultSelectionBox().getZ1());
			buffer.writeFloat(model.getDefaultSelectionBox().getZ2());
		}

		buffer.writeInt(model.getQuads().size());
		for(Quad quad : model.getQuads()) {
			buffer.writeInt(quad.getId());
			buffer.writeInt(quad.getVertices().size());
			for(Vertex vertex : quad.getVertices()) {
				buffer.writeFloat(vertex.getX());
				buffer.writeFloat(vertex.getY());
				buffer.writeFloat(vertex.getZ());
			}

			ChannelBufferUtils.writeExtendedString(buffer, quad.getTexture().getURL().toString());
			buffer.writeByte(quad.getTexture() instanceof SubTexture ? 1 : 0);
			buffer.writeInt(quad.getTexture().getX());
			buffer.writeInt(quad.getTexture().getY());
			buffer.writeInt(quad.getTexture().getWidth());
			buffer.writeInt(quad.getTexture().getHeight());
			buffer.writeInt(quad.getTexture().getFrameWidth());
			buffer.writeInt(quad.getTexture().getFrameHeight());
			buffer.writeInt(quad.getTexture().getFrameSpeed());
		}
	}

	@Override
	public CustomBlockMessage decode(ChannelBuffer buffer) throws IOException {
		byte id = buffer.readByte();
		boolean opaque = buffer.readByte() == 1;
		boolean selectable = buffer.readByte() == 1;
		StepSound sound = StepSound.valueOf(ChannelBufferUtils.readExtendedString(buffer));
		boolean liquid = buffer.readByte() == 1;
		int delay = buffer.readInt();
		boolean preventsRendering = buffer.readByte() == 1;
		boolean placeIn = buffer.readByte() == 1;
		boolean preventsOwnRendering = buffer.readByte() == 1;
		float brightness = buffer.readFloat();
		float speedModifier = buffer.readFloat();
		int fogRed = buffer.readInt();
		int fogGreen = buffer.readInt();
		int fogBlue = buffer.readInt();
		float fogDensity = buffer.readFloat();
		boolean unbreakable = buffer.readByte() == 1;
		String liquidName = buffer.readByte() == 1 ? ChannelBufferUtils.readExtendedString(buffer) : null;
		Model model = this.readModel(buffer);
		int outwards = buffer.readInt();

		BlockType block = new BlockType(id, sound, model);
		for(int count = 0; count < outwards; count++) {
			BlockFace faces[] = new BlockFace[buffer.readInt()];
			for(int ct = 0; ct < faces.length; ct++) {
				faces[ct] = BlockFace.valueOf(ChannelBufferUtils.readExtendedString(buffer));
			}
			
			block.addOutwardModel(this.readModel(buffer), faces);
		}
		
		block.setOpaque(opaque);
		block.setLiquid(liquid);
		block.setSelectable(selectable);
		block.setTickDelay(delay);
		block.setPreventsRendering(preventsRendering);
		block.setPlaceIn(placeIn);
		block.setPreventsOwnRendering(preventsOwnRendering);
		block.setBrightness(brightness);
		block.setSpeedModifier(speedModifier);
		block.setFogRed(fogRed);
		block.setFogGreen(fogGreen);
		block.setFogBlue(fogBlue);
		block.setFogDensity(fogDensity);
		block.setUnbreakable(unbreakable);
		block.setLiquidName(liquidName);

		return new CustomBlockMessage(block);
	}
	
	private Model readModel(ChannelBuffer buffer) {
		Model model = buffer.readByte() == 1 ? new CuboidModel(BlockType.TERRAIN_TEXTURE, 16, 0, 0, 0, 1, 1, 1, buffer.readInt(), buffer.readInt()) : new Model();
		model.clearQuads();
		if(buffer.readByte() == 0) {
			model.setUseCulling(false);
		}

		if(buffer.readByte() == 1) {
			float x1 = buffer.readFloat();
			float x2 = buffer.readFloat();
			float y1 = buffer.readFloat();
			float y2 = buffer.readFloat();
			float z1 = buffer.readFloat();
			float z2 = buffer.readFloat();
			model.setCollisionBox(new BoundingBox(x1, y1, z1, x2, y2, z2));
		} else {
			model.setCollisionBox(null);
		}

		if(buffer.readByte() == 1) {
			float sx1 = buffer.readFloat();
			float sx2 = buffer.readFloat();
			float sy1 = buffer.readFloat();
			float sy2 = buffer.readFloat();
			float sz1 = buffer.readFloat();
			float sz2 = buffer.readFloat();
			model.setSelectionBox(new BoundingBox(sx1, sy1, sz1, sx2, sy2, sz2));
		} else {
			model.setSelectionBox(null);
		}

		int quads = buffer.readInt();
		for(int i = 0; i < quads; i++) {
			int qid = buffer.readInt();
			Vertex vertices[] = new Vertex[buffer.readInt()];
			for(int ind = 0; ind < vertices.length; ind++) {
				float x = buffer.readFloat();
				float y = buffer.readFloat();
				float z = buffer.readFloat();

				vertices[ind] = new Vertex(x, y, z);
			}

			String u = ChannelBufferUtils.readExtendedString(buffer);
			URL url = null;
			try {
				url = new URL(u);
			} catch(MalformedURLException e) {
				OpenClassic.getLogger().severe("Invalid texture URL when receiving custom block: " + u);
			}
			
			boolean sub = buffer.readByte() == 1;
			int x = buffer.readInt();
			int y = buffer.readInt();
			int width = buffer.readInt();
			int height = buffer.readInt();
			int frameWidth = buffer.readInt();
			int frameHeight = buffer.readInt();
			int frameSpeed = buffer.readInt();
			
			Texture t = null;
			if(frameWidth > 0 && frameHeight > 0 && frameSpeed > 0) {
				t = TextureFactory.getFactory().newTexture(url, frameWidth, frameHeight, frameSpeed);
			} else {
				t = TextureFactory.getFactory().newTexture(url);
			}
			
			if(sub) {
				t = t.getSubTexture(x, y, width, height);
			}
			
			model.addQuad(QuadFactory.getFactory().newQuad(qid, t, vertices[0], vertices[1], vertices[2], vertices[3]));
		}
		
		return model;
	}

}
