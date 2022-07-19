package org.spacehq.openclassic.game.level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import com.zachsthings.onevent.EventManager;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.Position;
import org.spacehq.openclassic.api.block.Block;
import org.spacehq.openclassic.api.block.BlockFace;
import org.spacehq.openclassic.api.block.BlockType;
import org.spacehq.openclassic.api.block.Blocks;
import org.spacehq.openclassic.api.block.VanillaBlock;
import org.spacehq.openclassic.api.block.physics.FallingBlockPhysics;
import org.spacehq.openclassic.api.block.physics.FlowerPhysics;
import org.spacehq.openclassic.api.block.physics.LiquidPhysics;
import org.spacehq.openclassic.api.block.physics.MushroomPhysics;
import org.spacehq.openclassic.api.block.physics.SpongePhysics;
import org.spacehq.openclassic.api.block.physics.SpreadPhysics;
import org.spacehq.openclassic.api.block.physics.TreeGrowthPhysics;
import org.spacehq.openclassic.api.data.NBTData;
import org.spacehq.openclassic.api.event.block.BlockPhysicsEvent;
import org.spacehq.openclassic.api.event.level.SpawnChangeEvent;
import org.spacehq.openclassic.api.level.Level;
import org.spacehq.openclassic.api.level.LevelInfo;
import org.spacehq.openclassic.api.util.CoordUtil;

public abstract class ClassicLevel implements Level {

	private String name;
	private String author;
	private long creationTime;
	private short width;
	private short height;
	private short depth;
	private byte[] blocks;
	private Position spawn;
	private short waterLevel;
	private boolean generating;
	private int skyColor = 10079487;
	private int fogColor = 16777215;
	private int cloudColor = 16777215;
	private boolean raining;
	
	private boolean physics = OpenClassic.getGame().getConfig().getBoolean("physics.enabled", true);
	private Random random = new Random();
	private int physicsRandom = this.random.nextInt();
	private ArrayList<DelayedTick> delayedTicks = new ArrayList<DelayedTick>();
	
	private int[] highest;
	private NBTData data;
	
	public ClassicLevel() {
		this.highest = new int[0];
	}
	
	public ClassicLevel(LevelInfo info) {
		this.name = info.getName();
		this.author = "";
		this.creationTime = System.currentTimeMillis();

		this.spawn = info.getSpawn();
		if(this.spawn != null) {
			this.spawn.setLevel(this);
		}

		this.width = info.getWidth();
		this.height = info.getHeight();
		this.depth = info.getDepth();
		this.waterLevel = (short) (this.height / 2);
		this.blocks = new byte[this.width * this.depth * this.height];
		this.highest = new int[this.width * this.depth];
		Arrays.fill(this.highest, this.height);

		this.data = new NBTData(this.name);
		this.data.load(OpenClassic.getGame().getDirectory().getPath() + "/levels/" + this.name + ".nbt");
	}
	
	public Random getRandom() {
		return this.random;
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		if(this.name != null && !this.name.equals("")) return;

		this.name = name;
		this.data = new NBTData(this.name);
		this.data.load(OpenClassic.getGame().getDirectory().getPath() + "/levels/" + this.name + ".nbt");
	}

	@Override
	public String getAuthor() {
		return this.author;
	}

	public void setAuthor(String author) {
		if(this.author != null && !this.author.equals("")) return;

		this.author = author;
	}

	@Override
	public long getCreationTime() {
		return this.creationTime;
	}

	public void setCreationTime(long time) {
		if(this.creationTime != 0) return;

		this.creationTime = time;
	}

	@Override
	public Position getSpawn() {
		return this.spawn;
	}

	@Override
	public void setSpawn(Position spawn) {
		Position old = this.spawn;
		this.spawn = spawn;
		EventManager.callEvent(new SpawnChangeEvent(this, old));
	}

	@Override
	public short getWidth() {
		return this.width;
	}

	@Override
	public short getHeight() {
		return this.height;
	}

	@Override
	public short getDepth() {
		return this.depth;
	}

	@Override
	public short getWaterLevel() {
		return this.waterLevel;
	}
	
	@Override
	public short getGroundLevel() {
		return (short) (this.getWaterLevel() - 2);
	}
	
	@Override
	public byte[] getBlocks() {
		return Arrays.copyOf(this.blocks, this.blocks.length);
	}

	public void setData(int width, int height, int depth, byte blocks[]) {
		this.width = (short) width;
		this.height = (short) height;
		this.depth = (short) depth;
		this.waterLevel = (short) (this.height / 2);

		this.blocks = blocks;
		this.highest = new int[width * depth];
		Arrays.fill(this.highest, this.height);
		this.calcHighest(0, 0, width, depth);
	}
	
	private void calcHighest(int x, int z, int width, int depth) {
		for(int cx = x; cx < x + width; cx++) {
			for(int cz = z; cz < z + depth; cz++) {
				int highest = this.highest[cx + cz * this.width];
				for(int blocker = this.height - 1; blocker > 0; blocker--) {
					BlockType block = this.getBlockTypeAt(cx, blocker, cz);
					if(block != null && block.isOpaque()) {
						this.highest[cx + cz * this.width] = blocker;
						if(highest != blocker) {
							int lower = highest < blocker ? highest : blocker;
							highest = highest > blocker ? highest : blocker;
							this.highestUpdated(cx, cz, lower, highest);
						}
						
						break;
					}
				}
			}
		}
	}
	
	protected void highestUpdated(int x, int z, int lower, int highest) {
	}

	@Override
	public int getSkyColor() {
		return this.skyColor;
	}

	@Override
	public void setSkyColor(int color) {
		this.skyColor = color;
	}

	@Override
	public int getFogColor() {
		return this.fogColor;
	}

	@Override
	public void setFogColor(int color) {
		this.fogColor = color;
	}

	@Override
	public int getCloudColor() {
		return this.cloudColor;
	}

	@Override
	public void setCloudColor(int color) {
		this.cloudColor = color;
	}
	
	@Override
	public boolean isRaining() {
		return this.raining;
	}
	
	@Override
	public void setRaining(boolean raining) {
		this.raining = raining;
	}
	
	@Override
	public NBTData getData() {
		return this.data;
	}
	
	public boolean isGenerating() {
		return this.generating;
	}

	public void setGenerating(boolean generating) {
		this.generating = generating;
	}
	
	@Override
	public boolean getPhysicsEnabled() {
		return this.physics;
	}

	@Override
	public void setPhysicsEnabled(boolean enabled) {
		this.physics = enabled;
	}

	@Override
	public byte getBlockIdAt(Position pos) {
		return this.getBlockIdAt(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ());
	}

	@Override
	public byte getBlockIdAt(int x, int y, int z) {
		if(x < 0 || y < 0 || z < 0 || x >= this.width || y >= this.height || z >= this.depth) return 0;

		return this.blocks[CoordUtil.coordsToBlockIndex(this, x, y, z)];
	}

	@Override
	public BlockType getBlockTypeAt(Position pos) {
		return this.getBlockTypeAt(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ());
	}

	@Override
	public BlockType getBlockTypeAt(int x, int y, int z) {
		if(x < 0 || y < 0 || z < 0 || x >= this.width || y >= this.height || z >= this.depth) {
			return VanillaBlock.AIR;
		}

		return Blocks.fromId(this.getBlockIdAt(x, y, z));
	}

	@Override
	public Block getBlockAt(Position pos) {
		if(pos.getBlockX() < 0 || pos.getBlockY() < 0 || pos.getBlockZ() < 0 || pos.getBlockX() >= this.width || pos.getBlockY() >= this.height || pos.getBlockZ() >= this.depth) return null;

		return new Block(pos);
	}

	@Override
	public Block getBlockAt(int x, int y, int z) {
		return this.getBlockAt(new Position(this, x, y, z));
	}

	@Override
	public boolean setBlockIdAt(Position pos, byte type) {
		return this.setBlockIdAt(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ(), type);
	}

	@Override
	public boolean setBlockIdAt(Position pos, byte type, boolean physics) {
		return this.setBlockIdAt(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ(), type, physics);
	}

	@Override
	public boolean setBlockIdAt(int x, int y, int z, byte type) {
		return this.setBlockIdAt(x, y, z, type, true);
	}

	@Override
	public boolean setBlockIdAt(int x, int y, int z, byte type, boolean physics) {
		if(x < 0 || y < 0 || z < 0 || x >= this.width || y >= this.height || z >= this.depth) {
			return false;
		}

		if(!this.generating && type == VanillaBlock.AIR.getId() && (x == 0 || x == this.getWidth() - 1 || z == 0 || z == this.getDepth() - 1) && y <= this.getWaterLevel() - 1 && y > this.getWaterLevel() - 3) {
			type = VanillaBlock.WATER.getId();
		}

		this.blocks[CoordUtil.coordsToBlockIndex(this, x, y, z)] = type;
		if(physics && !this.generating) {
			for(BlockFace face : BlockFace.values()) {
				Block block = this.getBlockAt(x + face.getModX(), y + face.getModY(), z + face.getModZ());
				if(block != null && block.getType() != null && block.getType().getPhysics() != null) {
					block.getType().getPhysics().onNeighborChange(block, this.getBlockAt(x, y, z));
				}
			}
		}

		this.calcHighest(x, z, 1, 1);
		return true;
	}

	@Override
	public boolean setBlockAt(Position pos, BlockType type) {
		return this.setBlockAt(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ(), type);
	}

	@Override
	public boolean setBlockAt(Position pos, BlockType type, boolean physics) {
		return this.setBlockAt(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ(), type, physics);
	}
	
	@Override
	public boolean setBlockAt(int x, int y, int z, BlockType type) {
		return this.setBlockAt(x, y, z, type, true);
	}

	@Override
	public boolean setBlockAt(int x, int y, int z, BlockType type, boolean physics) {
		return this.setBlockIdAt(x, y, z, type.getId(), physics);
	}

	@Override
	public int getHighestBlockY(int x, int z) {
		return this.getHighestBlockY(x, z, this.getHeight());
	}

	@Override
	public int getHighestBlockY(int x, int z, int max) {
		for(int y = max; y >= 0; y--) {
			if(this.getBlockIdAt(x, y, z) != 0) {
				return y;
			}
		}

		return -1;
	}

	@Override
	public boolean isHighest(int x, int y, int z) {
		return this.getHighestBlockY(x, z) <= y;
	}
	
	@Override
	public boolean isLit(Position pos) {
		return this.isLit(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ());
	}

	@Override
	public boolean isLit(int x, int y, int z) {
		if(x < 0 || y < 0 || z < 0 || x >= this.width || y >= this.height || z >= this.depth) {
			return true;
		}
		
		return y >= this.highest[x + z * this.width];
	}
	
	@Override
	public float getBrightness(Position pos) {
		return this.getBrightness(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ());
	}
	
	@Override
	public float getBrightness(int x, int y, int z) {
		BlockType block = this.getBlockTypeAt(x, y, z);
		return block != null && block.getBrightness() > 0 ? block.getBrightness() : this.isLit(x, y, z) ? 1 : 0.6f;
	}
	
	@Override
	public void delayTick(Position pos) {
		BlockType block = this.getBlockTypeAt(pos);
		if(block != null) {
			DelayedTick next = new DelayedTick(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ(), block.getTickDelay());
			this.delayedTicks.add(next);
		}
	}
	
	@Override
	public boolean growTree(int x, int y, int z) {
		int logHeight = this.random.nextInt(3) + 4;
		boolean freespace = true;

		for(int currY = y; currY <= y + 1 + logHeight; currY++) {
			byte leaf = 1;
			if(currY == y) {
				leaf = 0;
			}

			if(currY >= y + 1 + logHeight - 2) {
				leaf = 2;
			}

			for(int currX = x - leaf; currX <= x + leaf && freespace; currX++) {
				for(int currZ = z - leaf; currZ <= z + leaf && freespace; currZ++) {
					if(currX >= 0 && currY >= 0 && currZ >= 0 && currX < this.getWidth() && currY < this.getHeight() && currZ < this.getDepth()) {
						if(this.getBlockTypeAt(currX, currY, currZ) != VanillaBlock.AIR) {
							freespace = false;
						}
					} else {
						freespace = false;
					}
				}
			}
		}

		if(!freespace) {
			return false;
		} else if(this.getBlockTypeAt(x, y, z) == VanillaBlock.GRASS && y < this.getHeight() - logHeight - 1) {
			this.setBlockAt(x, y - 1, z, VanillaBlock.DIRT);
			for(int count = y - 3 + logHeight; count <= y + logHeight; count++) {
				int baseDist = count - (y + logHeight);
				int leafMax = 1 - baseDist / 2;

				for(int currX = x - leafMax; currX <= x + leafMax; currX++) {
					int diffX = currX - x;

					for(int currZ = z - leafMax; currZ <= z + leafMax; currZ++) {
						int diffZ = currZ - z;
						if(Math.abs(diffX) != leafMax || Math.abs(diffZ) != leafMax || this.random.nextInt(2) != 0 && baseDist != 0) {
							this.setBlockAt(currX, count, currZ, VanillaBlock.LEAVES);
						}
					}
				}
			}

			for(int count = 0; count < logHeight; count++) {
				this.setBlockAt(x, y + count, z, VanillaBlock.LOG);
			}

			return true;
		} else {
			return false;
		}
	}
	
	public void tick() {
		if(this.getPhysicsEnabled()) {
			int wshift = 1;
			int dshift = 1;
			while(1 << wshift < this.getWidth()) {
				wshift++;
			}
	
			while(1 << dshift < this.getHeight()) {
				dshift++;
			}
	
			int size = this.delayedTicks.size();
			for(int ct = 0; ct < size; ct++) {
				DelayedTick next = this.delayedTicks.remove(0);
				if(next.getTicks() > 0) {
					next.decreaseTicks();
					this.delayedTicks.add(next);
				} else {
					BlockType block = this.getBlockTypeAt(next.getX(), next.getY(), next.getZ());
					if(block != null && block.getPhysics() != null && this.physicsAllowed(block)) {
						BlockPhysicsEvent event = EventManager.callEvent(new BlockPhysicsEvent(this.getBlockAt(next.getX(), next.getY(), next.getZ())));
						if(!event.isCancelled()) {
							block.getPhysics().update(this.getBlockAt(next.getX(), next.getY(), next.getZ()));
						}
					}
				}
			}
	
			int ticks = (this.getWidth() * this.getHeight() * this.getDepth()) / 200;
			for(int count = 0; count < ticks; count++) {
				this.physicsRandom = this.physicsRandom * 3 + 1013904223;
				int rand = this.physicsRandom >> 2;
				int x = rand & (this.getWidth() - 1);
				int z = rand >> wshift & (this.getDepth() - 1);
				int y = rand >> wshift + dshift & (this.getHeight() - 1);
				BlockType block = this.getBlockTypeAt(x, y, z);
				if(block != null && block.getPhysics() != null && this.getPhysicsEnabled()) {
					BlockPhysicsEvent event = EventManager.callEvent(new BlockPhysicsEvent(this.getBlockAt(x, y, z)));
					if(!event.isCancelled()) {
						block.getPhysics().update(this.getBlockAt(x, y, z));
					}
				}
			}
		}
	}
	
	private boolean physicsAllowed(BlockType block) {
		if(block.getPhysics() == null) {
			return false;
		}

		if(block.getPhysics() instanceof FallingBlockPhysics) {
			return OpenClassic.getGame().getConfig().getBoolean("physics.falling", true);
		}

		if(block.getPhysics() instanceof FlowerPhysics) {
			return OpenClassic.getGame().getConfig().getBoolean("physics.flower", true);
		}

		if(block.getPhysics() instanceof MushroomPhysics) {
			return OpenClassic.getGame().getConfig().getBoolean("physics.mushroom", true);
		}

		if(block.getPhysics() instanceof TreeGrowthPhysics) {
			return OpenClassic.getGame().getConfig().getBoolean("physics.trees", true);
		}

		if(block.getPhysics() instanceof SpongePhysics) {
			return OpenClassic.getGame().getConfig().getBoolean("physics.sponge", true);
		}

		if(block.getPhysics() instanceof LiquidPhysics) {
			return OpenClassic.getGame().getConfig().getBoolean("physics.liquid", true);
		}

		if(block.getPhysics() instanceof SpreadPhysics) {
			return OpenClassic.getGame().getConfig().getBoolean("physics.grass", true);
		}

		return true;
	}

}
