package org.spacehq.openclassic.client.math;

import org.spacehq.openclassic.api.block.BlockFace;
import org.spacehq.openclassic.api.math.Vector;

import com.mojang.minecraft.entity.Entity;

public class Intersection {

	private int x;
	private int y;
	private int z;
	private BlockFace face;
	private Vector pos;
	private Entity entity;

	public Intersection(int x, int y, int z, BlockFace face, Vector pos) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.face = face;
		this.pos = pos.clone();
	}

	public Intersection(Entity entity) {
		this.entity = entity;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public int getZ() {
		return this.z;
	}
	
	public BlockFace getFace() {
		return this.face;
	}
	
	public Vector getPos() {
		return this.pos;
	}
	
	public Entity getEntity() {
		return this.entity;
	}
	
}
