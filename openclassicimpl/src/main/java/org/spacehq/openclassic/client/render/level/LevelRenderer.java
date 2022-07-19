package org.spacehq.openclassic.client.render.level;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.block.BlockType;
import org.spacehq.openclassic.api.block.VanillaBlock;
import org.spacehq.openclassic.api.block.model.Model;
import org.spacehq.openclassic.api.math.BoundingBox;
import org.spacehq.openclassic.api.player.Player;
import org.spacehq.openclassic.client.ClassicClient;
import org.spacehq.openclassic.client.level.ClientLevel;
import org.spacehq.openclassic.client.math.Intersection;
import org.spacehq.openclassic.client.player.ClientPlayer;
import org.spacehq.openclassic.client.render.Frustum;
import org.spacehq.openclassic.client.render.RenderHelper;
import org.spacehq.openclassic.client.render.Renderer;
import org.spacehq.openclassic.client.render.Textures;
import org.spacehq.openclassic.client.util.BlockUtils;
import org.spacehq.openclassic.client.util.ChunkDistanceComparator;
import org.spacehq.openclassic.client.util.ChunkVisibleAndDistanceComparator;

import com.mojang.minecraft.entity.player.LocalPlayer;

public class LevelRenderer {

	public int boundaryList = -1;
	public IntBuffer listBuffer = BufferUtils.createIntBuffer(65536);
	public List<Chunk> chunks = new ArrayList<Chunk>();
	private ClientLevel level;
	private Chunk[] sortedChunks;
	public Chunk[] chunkCache;
	private int xChunks;
	private int zChunks;
	private int yChunks;
	public int ticks = 0;
	private float lastLoadX = -9999;
	private float lastLoadY = -9999;
	private float lastLoadZ = -9999;
	private float cracks = 0;
	
	private float fogEnd;
	private float fogRed;
	private float fogGreen;
	private float fogBlue;

	public LevelRenderer(ClientLevel level) {
		this.level = level;
	}
	
	public void setCracks(float cracks) {
		this.cracks = cracks;
	}

	public void refresh() {
		if(this.chunkCache != null) {
			for(int index = 0; index < this.chunkCache.length; index++) {
				this.chunkCache[index].dispose();
			}
		}

		this.xChunks = this.level.getWidth() / 16;
		this.zChunks = this.level.getHeight() / 16;
		this.yChunks = this.level.getDepth() / 16;
		Chunk old[] = this.chunkCache;
		this.chunkCache = new Chunk[this.xChunks * this.zChunks * this.yChunks];
		this.sortedChunks = new Chunk[this.xChunks * this.zChunks * this.yChunks];
		for(int x = 0; x < this.xChunks; x++) {
			for(int y = 0; y < this.zChunks; y++) {
				for(int z = 0; z < this.yChunks; z++) {
					Chunk o = old != null ? old[(z * this.zChunks + y) * this.xChunks + x] : null;
					int id = o != null ? o.baseListId : -1;
					this.chunkCache[(z * this.zChunks + y) * this.xChunks + x] = new Chunk(this.level, x << 4, y << 4, z << 4, 16, id);
					this.sortedChunks[(z * this.zChunks + y) * this.xChunks + x] = this.chunkCache[(z * this.zChunks + y) * this.xChunks + x];
				}
			}
		}

		for(int x = 0; x < this.chunks.size(); x++) {
			this.chunks.get(x).loaded = false;
		}

		this.chunks.clear();
		this.queueChunks(0, 0, 0, this.level.getWidth(), this.level.getHeight(), this.level.getDepth());
		this.boundaryList = -1;
	}
	
	public void queueChunks(int x1, int z1, int y1, int x2, int z2, int y2) {
		x1 /= 16;
		z1 /= 16;
		y1 /= 16;
		x2 /= 16;
		z2 /= 16;
		y2 /= 16;
		if(x1 < 0) {
			x1 = 0;
		}

		if(z1 < 0) {
			z1 = 0;
		}

		if(y1 < 0) {
			y1 = 0;
		}

		if(x2 > this.xChunks - 1) {
			x2 = this.xChunks - 1;
		}

		if(z2 > this.zChunks - 1) {
			z2 = this.zChunks - 1;
		}

		if(y2 > this.yChunks - 1) {
			y2 = this.yChunks - 1;
		}

		for(int x = x1; x <= x2; x++) {
			for(int z = z1; z <= z2; z++) {
				for(int y = y1; y <= y2; y++) {
					Chunk chunk = this.chunkCache[(y * this.zChunks + z) * this.xChunks + x];
					if(!chunk.loaded) {
						chunk.loaded = true;
						this.chunks.add(this.chunkCache[(y * this.zChunks + z) * this.xChunks + x]);
					}
				}
			}
		}
	}
	
	public void render(float delta, int anaglyphPass) {
		Player player = OpenClassic.getClient().getPlayer();
		this.prepareView(delta, anaglyphPass);
		for(int count = 0; count < this.chunkCache.length; count++) {
			this.chunkCache[count].clip();
		}

		try {
			Collections.sort(this.chunks, new ChunkVisibleAndDistanceComparator(player));
		} catch(Exception e) {
		}

		int amount = this.chunks.size();
		if(amount > 3) {
			amount = 3;
		}

		for(int count = 0; count < amount; count++) {
			Chunk chunk = this.chunks.remove(this.chunks.size() - 1);
			if(chunk != null) {
				chunk.buildLists();
				chunk.loaded = false;
			}
		}
		
		this.updateFog(player);
		GL11.glEnable(GL11.GL_FOG);
		this.renderLevel(player, true);
		if(BlockUtils.preventsRendering(this.level, player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ(), 0.1F)) {
			for(int bx = player.getPosition().getBlockX() - 1; bx <= player.getPosition().getBlockX() + 1; bx++) {
				for(int by = player.getPosition().getBlockY() - 1; by <= player.getPosition().getBlockY() + 1; by++) {
					for(int bz = player.getPosition().getBlockZ() - 1; bz <= player.getPosition().getBlockZ() + 1; bz++) {
						BlockType block = this.level.getBlockTypeAt(bx, by, bz);
						if(block != null && block.getPreventsRendering()) {
							GL11.glDepthFunc(GL11.GL_LESS);
							GL11.glDisable(GL11.GL_CULL_FACE);
							block.getModel(this.level, bx, by, bz).renderAll(bx, by, bz, 0.2f);
							GL11.glEnable(GL11.GL_CULL_FACE);
							GL11.glDepthFunc(GL11.GL_LEQUAL);
						}
					}
				}
			}
		}
		
		this.level.renderEntities(delta, anaglyphPass);
		this.renderRockBounds();
		this.renderSky(delta);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		this.renderSelected(delta);
		this.renderUnculled(player, anaglyphPass);
		GL11.glDisable(GL11.GL_FOG);
		this.renderRain(delta, player);
	}
	
	private void prepareView(float delta, int anaglyphPass) {
		Player player = OpenClassic.getClient().getPlayer();
		float fogDensity = 1 - (float) Math.pow(1F / (4 - OpenClassic.getClient().getSettings().getIntSetting("options.render-distance").getValue()), 0.25);
		float skyRed = (this.level.getSkyColor() >> 16 & 255) / 255F;
		float skyGreen = (this.level.getSkyColor() >> 8 & 255) / 255F;
		float skyBlue = (this.level.getSkyColor() & 255) / 255F;
		this.fogRed = (this.level.getFogColor() >> 16 & 255) / 255F;
		this.fogGreen = (this.level.getFogColor() >> 8 & 255) / 255F;
		this.fogBlue = (this.level.getFogColor() & 255) / 255F;
		this.fogRed += (skyRed - this.fogRed) * fogDensity;
		this.fogGreen += (skyGreen - this.fogGreen) * fogDensity;
		this.fogBlue += (skyBlue - this.fogBlue) * fogDensity;
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
		this.fogEnd = (512 >> (OpenClassic.getClient().getSettings().getIntSetting("options.render-distance").getValue() << 1));
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		if(OpenClassic.getClient().getSettings().getBooleanSetting("options.3d-anaglyph").getValue()) {
			GL11.glTranslatef((-((anaglyphPass << 1) - 1)) * 0.07F, 0, 0);
		}

		float fov = 70;
		if(player.getHealth() <= 0) {
			fov /= (1 - 500 / (((LocalPlayer) ((ClientPlayer) player).getHandle()).deathTime + delta + 500)) * 2 + 1;
		}

		GLU.gluPerspective(fov, Display.getWidth() / (float) Display.getHeight(), 0.05f, this.fogEnd);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		if(OpenClassic.getClient().getSettings().getBooleanSetting("options.3d-anaglyph").getValue()) {
			GL11.glTranslatef(((anaglyphPass << 1) - 1) * 0.1F, 0, 0);
		}

		RenderHelper.getHelper().hurtEffect(player, delta);
		if(OpenClassic.getClient().getSettings().getBooleanSetting("options.view-bobbing").getValue()) {
			RenderHelper.getHelper().applyBobbing(player, delta);
		}
		
		GL11.glTranslatef(0, 0, -0.1F);
		GL11.glRotatef(player.getPosition().getInterpolatedPitch(delta), 1, 0, 0);
		GL11.glRotatef(player.getPosition().getInterpolatedYaw(delta), 0, 1, 0);
		GL11.glTranslatef(-player.getPosition().getInterpolatedX(delta), -player.getPosition().getInterpolatedY(delta), -player.getPosition().getInterpolatedZ(delta));
		Frustum.update();
	}

	public void updateFog(Player player) {
		float fogRed = this.fogRed;
		float fogGreen = this.fogGreen;
		float fogBlue = this.fogBlue;
		BlockType type = this.level.getBlockTypeAt(player.getPosition().getBlockX(), (int) (player.getPosition().getY() + 0.12f), player.getPosition().getBlockZ());
		if(type != null && (type.getFogDensity() != -1 || type.getFogRed() != -1 || type.getFogGreen() != -1 || type.getFogBlue() != -1 || type.isLiquid())) {
			if(type.getFogDensity() != -1) {
				GL11.glFogf(GL11.GL_FOG_DENSITY, type.getFogDensity());
			}
			
			if(type.getFogRed() != -1) {
				fogRed = type.getFogRed() / 255f;
			}
			
			if(type.getFogGreen() != -1) {
				fogGreen = type.getFogGreen() / 255f;
			}
			
			if(type.getFogBlue() != -1) {
				fogBlue = type.getFogBlue() / 255f;
			}
			
			if(type.isLiquid()) {
				GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
				float r = type.getFogRed() != -1 ? type.getFogRed() / 255f : 1;
				float g = type.getFogGreen() != -1 ? type.getFogGreen() / 255f : 1;
				float b = type.getFogBlue() != -1 ? type.getFogBlue() / 255f : 1;
				if(OpenClassic.getClient().getSettings().getBooleanSetting("options.3d-anaglyph").getValue()) {
					r = (r * 30 + g * 59 + b * 11) / 100;
					g = (r * 30 + g * 70) / 100;
					b = (r * 30 + b * 70) / 100;
				}

				GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, RenderHelper.getHelper().getParamBuffer(r, g, b, 1));
			}
		} else {
			GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
			GL11.glFogf(GL11.GL_FOG_START, 0);
			GL11.glFogf(GL11.GL_FOG_END, this.fogEnd);
			GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, RenderHelper.getHelper().getParamBuffer(1, 1, 1, 1));
		}
		
		if(OpenClassic.getClient().getSettings().getBooleanSetting("options.3d-anaglyph").getValue()) {
			float fred = (fogRed * 30 + fogBlue * 59 + fogGreen * 11) / 100;
			float fgreen = (fogRed * 30 + fogGreen * 70) / 100;
			float fblue = (fogRed * 30 + fogBlue * 70) / 100;
			fogRed = fred;
			fogGreen = fgreen;
			fogBlue = fblue;
		}
		
		GL11.glClearColor(fogRed, fogGreen, fogBlue, 0);
		GL11.glFog(GL11.GL_FOG_COLOR, RenderHelper.getHelper().getParamBuffer(fogRed, fogGreen, fogBlue, 1));
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT);
	}

	private void renderRockBounds() {
		if(this.boundaryList == -1) {
			this.boundaryList = GL11.glGenLists(2);
			GL11.glNewList(this.boundaryList, GL11.GL_COMPILE);
			float groundLevel = this.level.getGroundLevel();
			int length = 128;
			if(length > this.level.getWidth()) {
				length = this.level.getWidth();
			}

			if(length > this.level.getDepth()) {
				length = this.level.getDepth();
			}

			int scale = 2048 / length;
			Renderer.get().begin();
			Renderer.get().color(0.5f, 0.5f, 0.5f, 1);
			for(int x = -length * scale; x < this.level.getWidth() + length * scale; x += length) {
				for(int z = -length * scale; z < this.level.getDepth() + length * scale; z += length) {
					float y = groundLevel;
					if(x >= 0 && z >= 0 && x < this.level.getWidth() && z < this.level.getDepth()) {
						y = 0;
					}

					Renderer.get().vertexuv(x, y, (z + length), 0, length);
					Renderer.get().vertexuv((x + length), y, (z + length), length, length);
					Renderer.get().vertexuv((x + length), y, z, length, 0);
					Renderer.get().vertexuv(x, y, z, 0, 0);
				}
			}

			Renderer.get().end();
			
			Renderer.get().begin();
			Renderer.get().color(0.8f, 0.8f, 0.8f);
			for(int x = 0; x < this.level.getWidth(); x += length) {
				Renderer.get().vertexuv(x, 0, 0, 0, 0);
				Renderer.get().vertexuv((x + length), 0, 0, length, 0);
				Renderer.get().vertexuv((x + length), groundLevel, 0, length, groundLevel);
				Renderer.get().vertexuv(x, groundLevel, 0, 0, groundLevel);
				Renderer.get().vertexuv(x, groundLevel, this.level.getDepth(), 0, groundLevel);
				Renderer.get().vertexuv((x + length), groundLevel, this.level.getDepth(), length, groundLevel);
				Renderer.get().vertexuv((x + length), 0, this.level.getDepth(), length, 0);
				Renderer.get().vertexuv(x, 0, this.level.getDepth(), 0, 0);
			}

			Renderer.get().color(0.6f, 0.6f, 0.6f);
			for(int z = 0; z < this.level.getDepth(); z += length) {
				Renderer.get().vertexuv(0, groundLevel, z, 0, 0);
				Renderer.get().vertexuv(0, groundLevel, (z + length), length, 0);
				Renderer.get().vertexuv(0, 0, (z + length), length, groundLevel);
				Renderer.get().vertexuv(0, 0, z, 0, groundLevel);
				Renderer.get().vertexuv(this.level.getWidth(), 0, z, 0, groundLevel);
				Renderer.get().vertexuv(this.level.getWidth(), 0, (z + length), length, groundLevel);
				Renderer.get().vertexuv(this.level.getWidth(), groundLevel, (z + length), length, 0);
				Renderer.get().vertexuv(this.level.getWidth(), groundLevel, z, 0, 0);
			}

			Renderer.get().end();
			GL11.glEndList();
			GL11.glNewList(this.boundaryList + 1, GL11.GL_COMPILE);
			float waterLevel = this.level.getWaterLevel();
			int len = 128;
			if(len > this.level.getWidth()) {
				len = this.level.getWidth();
			}

			if(len > this.level.getDepth()) {
				len = this.level.getDepth();
			}

			GL11.glColor4f(1, 1, 1, 1);
			Renderer.get().begin();
			for(int x = -2048; x < this.level.getWidth() + 2048; x += len) {
				for(int z = -2048; z < this.level.getDepth() + 2048; z += len) {
					float y = waterLevel - 0.05F;
					if(x < 0 || z < 0 || x >= this.level.getWidth() || z >= this.level.getDepth()) {
						Renderer.get().vertexuv(x, y, (z + len), 0, len);
						Renderer.get().vertexuv((x + len), y, (z + len), 0 + len, len);
						Renderer.get().vertexuv((x + len), y, z, 0 + len, 0);
						Renderer.get().vertexuv(x, y, z, 0, 0);
						Renderer.get().vertexuv(x, y, z, 0, 0);
						Renderer.get().vertexuv((x + len), y, z, 0 + len, 0);
						Renderer.get().vertexuv((x + len), y, (z + len), 0 + len, len);
						Renderer.get().vertexuv(x, y, (z + len), 0, len);
					}
				}
			}

			Renderer.get().end();
			GL11.glEndList();
		}
		
		Textures.ROCK.bind();
		GL11.glCallList(this.boundaryList);
	}
	
	private void renderUnculled(Player player, int anaglyphPass) {
		VanillaBlock.WATER.getModel().getQuad(0).getTexture().bind();
		GL11.glCallList(this.boundaryList + 1);
		GL11.glColorMask(false, false, false, false);
		int cy = this.renderLevel(player, false);
		GL11.glColorMask(true, true, true, true);
		if(OpenClassic.getClient().getSettings().getBooleanSetting("options.3d-anaglyph").getValue()) {
			if(anaglyphPass == 0) {
				GL11.glColorMask(false, true, true, false);
			} else {
				GL11.glColorMask(true, false, false, false);
			}
		}

		if(cy > 0) {
			GL11.glCallLists(this.listBuffer);
		}
	}
	
	private void renderSky(float delta) {
		Textures.CLOUDS.bind();
		float cloudRed = (this.level.getCloudColor() >> 16 & 255) / 255F;
		float cloudBlue = (this.level.getCloudColor() >> 8 & 255) / 255F;
		float cloudGreen = (this.level.getCloudColor() & 255) / 255F;
		if(OpenClassic.getClient().getSettings().getBooleanSetting("options.3d-anaglyph").getValue()) {
			cloudRed = (cloudRed * 30 + cloudBlue * 59 + cloudGreen * 11) / 100;
			cloudBlue = (cloudRed * 30 + cloudBlue * 70) / 100;
			cloudGreen = (cloudRed * 30 + cloudGreen * 70) / 100;
		}

		float texCoordMod = 1 / 2048f;
		float cloudHeight = (this.level.getHeight() + 2);
		float movement = (this.ticks + delta) * texCoordMod * 0.03F;
		Renderer.get().begin();
		Renderer.get().color(cloudRed, cloudBlue, cloudGreen);

		for(int x = -2048; x < this.level.getWidth() + 2048; x += 512) {
			for(int z = -2048; z < this.level.getDepth() + 2048; z += 512) {
				Renderer.get().vertexuv(x, cloudHeight, (z + 512), x * texCoordMod + movement, (z + 512) * texCoordMod);
				Renderer.get().vertexuv((x + 512), cloudHeight, (z + 512), (x + 512) * texCoordMod + movement, (z + 512) * texCoordMod);
				Renderer.get().vertexuv((x + 512), cloudHeight, z, (x + 512) * texCoordMod + movement, z * texCoordMod);
				Renderer.get().vertexuv(x, cloudHeight, z, x * texCoordMod + movement, z * texCoordMod);
				Renderer.get().vertexuv(x, cloudHeight, z, x * texCoordMod + movement, z * texCoordMod);
				Renderer.get().vertexuv((x + 512), cloudHeight, z, (x + 512) * texCoordMod + movement, z * texCoordMod);
				Renderer.get().vertexuv((x + 512), cloudHeight, (z + 512), (x + 512) * texCoordMod + movement, (z + 512) * texCoordMod);
				Renderer.get().vertexuv(x, cloudHeight, (z + 512), x * texCoordMod + movement, (z + 512) * texCoordMod);
			}
		}

		Renderer.get().end();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		Renderer.get().begin();
		float skRed = (this.level.getSkyColor() >> 16 & 255) / 255F;
		float skBlue = (this.level.getSkyColor() >> 8 & 255) / 255F;
		float skGreen = (this.level.getSkyColor() & 255) / 255F;
		if(OpenClassic.getClient().getSettings().getBooleanSetting("options.3d-anaglyph").getValue()) {
			skRed = (skRed * 30 + skBlue * 59 + skGreen * 11) / 100;
			skBlue = (skRed * 30 + skBlue * 70) / 100;
			skGreen = (skRed * 30 + skGreen * 70) / 100;
		}

		Renderer.get().color(skRed, skBlue, skGreen);
		float skyHeight = this.level.getHeight() + 10;
		for(int x = -2048; x < this.level.getWidth() + 2048; x += 512) {
			for(int z = -2048; z < this.level.getDepth() + 2048; z += 512) {
				Renderer.get().vertex(x, skyHeight, z);
				Renderer.get().vertex((x + 512), skyHeight, z);
				Renderer.get().vertex((x + 512), skyHeight, (z + 512));
				Renderer.get().vertex(x, skyHeight, (z + 512));
			}
		}

		Renderer.get().end();
	}

	private int renderLevel(Player player, boolean firstPass) {
		float xDiff = player.getPosition().getX() - this.lastLoadX;
		float yDiff = player.getPosition().getY() - this.lastLoadY;
		float zDiff = player.getPosition().getZ() - this.lastLoadZ;
		float sqDistance = xDiff * xDiff + yDiff * yDiff + zDiff * zDiff;
		if(sqDistance > 64) {
			this.lastLoadX = player.getPosition().getX();
			this.lastLoadY = player.getPosition().getY();
			this.lastLoadZ = player.getPosition().getZ();

			try {
				Arrays.sort(this.sortedChunks, new ChunkDistanceComparator(player));
			} catch(Exception e) {
			}
		}

		this.listBuffer.clear();
		for(int index = 0; index < this.sortedChunks.length; index++) {
			if(this.sortedChunks[index] != null) {
				this.sortedChunks[index].appendLists(this.listBuffer, firstPass);
			}
		}

		this.listBuffer.flip();
		if(this.listBuffer.remaining() > 0) {
			GL11.glCallLists(this.listBuffer);
		}
		
		return this.listBuffer.remaining();
	}
	
	private void renderSelected(float delta) {
		Intersection selected = ((ClassicClient) OpenClassic.getClient()).getSelected();
		if(selected != null) {
			if(selected.getEntity() != null) {
				selected.getEntity().renderHoverOver(delta);
			} else {
				if(this.cracks > 0) {
					GL11.glDisable(GL11.GL_ALPHA_TEST);
					GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_SRC_COLOR);
					GL11.glPushMatrix();
					GL11.glDepthMask(false);
					BlockType btype = this.level.getBlockTypeAt(selected.getX(), selected.getY(), selected.getZ());
					if(btype == null) {
						btype = VanillaBlock.STONE;
					}
	
					Model model = btype.getModel(this.level, selected.getX(), selected.getY(), selected.getZ());
					for(int count = 0; count < model.getQuads().size(); count++) {
						RenderHelper.getHelper().drawCracks(model.getQuad(count), selected.getX(), selected.getY(), selected.getZ(), 240 + (int) (this.cracks * 10));
					}
	
					GL11.glDepthMask(true);
					GL11.glPopMatrix();
					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
					GL11.glEnable(GL11.GL_ALPHA_TEST);
				}
	
				BlockType block = this.level.getBlockTypeAt(selected.getX(), selected.getY(), selected.getZ());
				if(block != null) {
					BoundingBox bb = block.getModel(this.level, selected.getX(), selected.getY(), selected.getZ()).getSelectionBox(selected.getX(), selected.getY(), selected.getZ());
					if(bb != null) {
						bb = bb.grow(0.002F, 0.002F, 0.002F);
						GL11.glDisable(GL11.GL_ALPHA_TEST);
						GL11.glDisable(GL11.GL_TEXTURE_2D);
						GL11.glDepthMask(false);
						
						Renderer.get().begin(GL11.GL_LINE_STRIP);
						Renderer.get().color(0, 0, 0, 0.4f);
						Renderer.get().vertex(bb.getX1(), bb.getY1(), bb.getZ1());
						Renderer.get().vertex(bb.getX2(), bb.getY1(), bb.getZ1());
						Renderer.get().vertex(bb.getX2(), bb.getY1(), bb.getZ2());
						Renderer.get().vertex(bb.getX1(), bb.getY1(), bb.getZ2());
						Renderer.get().vertex(bb.getX1(), bb.getY1(), bb.getZ1());
						Renderer.get().end();
						Renderer.get().begin(GL11.GL_LINE_STRIP);
						Renderer.get().color(0, 0, 0, 0.4f);
						Renderer.get().vertex(bb.getX1(), bb.getY2(), bb.getZ1());
						Renderer.get().vertex(bb.getX2(), bb.getY2(), bb.getZ1());
						Renderer.get().vertex(bb.getX2(), bb.getY2(), bb.getZ2());
						Renderer.get().vertex(bb.getX1(), bb.getY2(), bb.getZ2());
						Renderer.get().vertex(bb.getX1(), bb.getY2(), bb.getZ1());
						Renderer.get().end();
						Renderer.get().begin(GL11.GL_LINES);
						Renderer.get().color(0, 0, 0, 0.4f);
						Renderer.get().vertex(bb.getX1(), bb.getY1(), bb.getZ1());
						Renderer.get().vertex(bb.getX1(), bb.getY2(), bb.getZ1());
						Renderer.get().vertex(bb.getX2(), bb.getY1(), bb.getZ1());
						Renderer.get().vertex(bb.getX2(), bb.getY2(), bb.getZ1());
						Renderer.get().vertex(bb.getX2(), bb.getY1(), bb.getZ2());
						Renderer.get().vertex(bb.getX2(), bb.getY2(), bb.getZ2());
						Renderer.get().vertex(bb.getX1(), bb.getY1(), bb.getZ2());
						Renderer.get().vertex(bb.getX1(), bb.getY2(), bb.getZ2());
						Renderer.get().end();
						
						GL11.glDepthMask(true);
						GL11.glEnable(GL11.GL_TEXTURE_2D);
						GL11.glEnable(GL11.GL_ALPHA_TEST);
					}
				}
			}
		}
	}
	
	private void renderRain(float delta, Player player) {
		if(this.level.isRaining()) {
			GL11.glDisable(GL11.GL_CULL_FACE);
			Textures.RAIN.bind();
			for(int cx = player.getPosition().getBlockX() - 5; cx <= player.getPosition().getBlockX() + 5; cx++) {
				for(int cz = player.getPosition().getBlockZ() - 5; cz <= player.getPosition().getBlockZ() + 5; cz++) {
					int highest = this.level.getHighestBlockY(cx, cz) + 1;
					int minRY = player.getPosition().getBlockY() - 5;
					int maxRY = player.getPosition().getBlockY() + 5;
					if(minRY < highest) {
						minRY = highest;
					}

					if(maxRY < highest) {
						maxRY = highest;
					}

					if(minRY != maxRY) {
						float downfall = (((this.ticks + cx * 3121 + cz * 418711) % 32) + delta) / 32F;
						float rax = cx + 0.5F - player.getPosition().getX();
						float raz = cz + 0.5F - player.getPosition().getZ();
						float visibility = (float) Math.sqrt(rax * rax + raz * raz) / 5;
						Renderer.get().begin();
						Renderer.get().normal(0, 1, 0);
						Renderer.get().color(1, 1, 1, (1 - visibility * visibility) * 0.7f);
						Renderer.get().vertexuv(cx, minRY, cz, 0, minRY * 2 / 8F + downfall * 2);
						Renderer.get().vertexuv((cx + 1), minRY, (cz + 1), 2, minRY * 2 / 8F + downfall * 2);
						Renderer.get().vertexuv((cx + 1), maxRY, (cz + 1), 2, maxRY * 2 / 8F + downfall * 2);
						Renderer.get().vertexuv(cx, maxRY, cz, 0, maxRY * 2 / 8F + downfall * 2);
						Renderer.get().vertexuv(cx, minRY, (cz + 1), 0, minRY * 2 / 8F + downfall * 2);
						Renderer.get().vertexuv((cx + 1), minRY, cz, 2, minRY * 2 / 8F + downfall * 2);
						Renderer.get().vertexuv((cx + 1), maxRY, cz, 2, maxRY * 2 / 8F + downfall * 2);
						Renderer.get().vertexuv(cx, maxRY, (cz + 1), 0, maxRY * 2 / 8F + downfall * 2);
						Renderer.get().end();
					}
				}
			}

			GL11.glEnable(GL11.GL_CULL_FACE);
		}
	}
	
}
