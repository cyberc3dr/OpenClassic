package org.spacehq.openclassic.client.render.level;

import org.lwjgl.opengl.GL11;
import org.spacehq.openclassic.api.block.BlockType;
import org.spacehq.openclassic.api.block.model.Texture;
import org.spacehq.openclassic.api.player.Player;
import org.spacehq.openclassic.client.level.ClientLevel;
import org.spacehq.openclassic.client.render.Frustum;
import org.spacehq.openclassic.client.render.Renderer;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class Chunk {

	public static int chunkUpdates = 0;
	public static final int PASSES = 2;
	
	public int baseListId = -1;
	private ClientLevel level;
	private int x;
	private int y;
	private int z;
	private int width;
	private int height;
	private int depth;
	private boolean[] dirty = new boolean[PASSES];
	public boolean visible = false;
	public boolean loaded;

	public Chunk(ClientLevel level, int x, int y, int z, int size, int baseId) {
		this.level = level;
		this.x = x;
		this.y = y;
		this.z = z;
		this.width = size;
		this.height = size;
		this.depth = size;
		this.baseListId = baseId;
		this.setAllDirty();
	}

	public void buildLists() {
		chunkUpdates++;
		this.setAllDirty();
		if(this.baseListId == -1) {
			this.baseListId = GL11.glGenLists(PASSES);
		}
		
		List<Texture> found = new ArrayList<Texture>();
		found.add(BlockType.TERRAIN_TEXTURE);
		for(int pass = 0; pass < PASSES; pass++) {
			boolean finished = true;
			GL11.glNewList(this.baseListId + pass, GL11.GL_COMPILE);
			if(pass > 0) {
				GL11.glDisable(GL11.GL_CULL_FACE);
			}
			
			for(int currentTex = 0; currentTex < found.size(); currentTex++) {
				Texture current = found.get(currentTex);
				Renderer.get().begin();
				for(int x = this.x; x < this.x + this.width; x++) {
					for(int y = this.y; y < this.y + this.height; y++) {
						for(int z = this.z; z < this.z + this.depth; z++) {
							BlockType block = this.level.getBlockTypeAt(x, y, z);
							int required = !block.getModel().useCulling() ? 1 : 0;
							if(pass != required) {
								finished = false;
								continue;
							}
							
							if(block.getModel().getQuads().size() > 0) {
								Texture texture = block.getModel().getQuad(0).getTexture();
								boolean foundOne = false;
								for(Texture tex : found) {
									if(tex.equals(texture)) {
										foundOne = true;
										break;
									}
								}
								
								if(!foundOne) {
									found.add(texture);
								}
								
								if(!texture.equals(current)) {
									continue;
								}
								
								block.getModel(this.level, x, y, z).render(x, y, z, this.level.getBrightness(x, y, z), true);
							}
						}
					}
				}
				
				current.bind();
				Renderer.get().end();
			}
			
			if(pass > 0) {
				GL11.glEnable(GL11.GL_CULL_FACE);
			}
			
			GL11.glEndList();
			this.dirty[pass] = false;
			if(finished) {
				break;
			}
		}

	}

	public float distanceSquared(Player player) {
		return player.getPosition().distanceSquared(this.x, this.y, this.z);
	}

	private void setAllDirty() {
		for(int index = 0; index < 2; index++) {
			this.dirty[index] = true;
		}
	}

	public void dispose() {
		this.setAllDirty();
	}

	public void appendLists(IntBuffer buffer, boolean firstPass) {
		for(int pass = 0; pass < PASSES; pass++) {
			if(firstPass) {
				if(pass > 0) {
					break;
				}
			} else if(pass == 0) {
				continue;
			}
			
			if(this.visible && !this.dirty[pass]) {
				buffer.put(this.baseListId + pass);
			}
		}
	}
	
	public void clip() {
		this.visible = Frustum.isBoxInFrustum(this.x, this.y, this.z, this.x + this.width, this.y + this.height, this.z + this.depth);
	}

}
