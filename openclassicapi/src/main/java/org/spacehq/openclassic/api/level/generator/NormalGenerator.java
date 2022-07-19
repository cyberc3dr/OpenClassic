package org.spacehq.openclassic.api.level.generator;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.block.VanillaBlock;
import org.spacehq.openclassic.api.level.Level;
import org.spacehq.openclassic.api.level.generator.noise.CombinedNoise;
import org.spacehq.openclassic.api.level.generator.noise.OctaveNoise;
import org.spacehq.openclassic.api.math.MathHelper;

import java.util.ArrayList;
import java.util.Random;

/**
 * Generates a standard Minecraft map.
 */
public final class NormalGenerator extends Generator {

	private Random random = new Random();
	private int[] floodData = new int[1048576];
	private int progressCounter = 0;

	@Override
	public void generate(Level level, byte data[]) {
		this.setStep(OpenClassic.getGame().getTranslator().translate("level.raising"));
		CombinedNoise raiseNoise1 = new CombinedNoise(new OctaveNoise(this.random, 8), new OctaveNoise(this.random, 8));
		CombinedNoise raiseNoise2 = new CombinedNoise(new OctaveNoise(this.random, 8), new OctaveNoise(this.random, 8));
		OctaveNoise raiseOctaves = new OctaveNoise(this.random, 6);
		int[] flatNoise = new int[level.getWidth() * level.getDepth()];
		for(int x = 0; x < level.getWidth(); x++) {
			this.setProgress(x * 100 / (level.getWidth() - 1));
			for(int z = 0; z < level.getDepth(); z++) {
				double val1 = raiseNoise1.compute((x * 1.3F), (z * 1.3F)) / 6D + -4;
				double val2 = raiseNoise2.compute((x * 1.3F), (z * 1.3F)) / 5D + 6;
				if(raiseOctaves.compute(x, z) / 8D > 0) {
					val2 = val1;
				}

				double noise = Math.max(val1, val2) / 2D;
				if(noise < 0) {
					noise *= 0.8D;
				}

				flatNoise[x + z * level.getWidth()] = (int) noise;
			}
		}

		this.setStep(OpenClassic.getGame().getTranslator().translate("level.eroding"));
		CombinedNoise erodeNoise1 = new CombinedNoise(new OctaveNoise(this.random, 8), new OctaveNoise(this.random, 8));
		CombinedNoise erodeNoise2 = new CombinedNoise(new OctaveNoise(this.random, 8), new OctaveNoise(this.random, 8));
		for(int x = 0; x < level.getWidth(); x++) {
			this.setProgress(x * 100 / (level.getWidth() - 1));
			for(int z = 0; z < level.getDepth(); z++) {
				double val1 = erodeNoise1.compute((x << 1), (z << 1)) / 8;
				int val2 = erodeNoise2.compute((x << 1), (z << 1)) > 0 ? 1 : 0;
				if(val1 > 2) {
					int val = ((flatNoise[x + z * level.getWidth()] - val2) / 2 << 1) + val2;
					flatNoise[x + z * level.getWidth()] = val;
				}
			}
		}

		this.setStep(OpenClassic.getGame().getTranslator().translate("level.soiling"));
		OctaveNoise soilNoise = new OctaveNoise(this.random, 8);
		for(int x = 0; x < level.getWidth(); x++) {
			this.setProgress(x * 100 / (level.getWidth() - 1));
			for(int z = 0; z < level.getDepth(); z++) {
				int val = (int) (soilNoise.compute(x, z) / 24) - 4;
				int base = flatNoise[x + z * level.getWidth()] + level.getWaterLevel();
				int height = base + val;
				flatNoise[x + z * level.getWidth()] = Math.max(base, height);
				if(flatNoise[x + z * level.getWidth()] > level.getHeight() - 2) {
					flatNoise[x + z * level.getWidth()] = level.getHeight() - 2;
				}

				if(flatNoise[x + z * level.getWidth()] < 1) {
					flatNoise[x + z * level.getWidth()] = 1;
				}

				for(int y = 0; y < level.getHeight(); y++) {
					byte block = 0;
					if(y <= base) {
						block = VanillaBlock.DIRT.getId();
					}

					if(y <= height) {
						block = VanillaBlock.STONE.getId();
					}

					if(y == 0) {
						block = VanillaBlock.LAVA.getId();
					}

					data[(y * level.getDepth() + z) * level.getWidth() + x] = block;
				}
			}
		}

		this.setStep(OpenClassic.getGame().getTranslator().translate("level.carving"));
		int caves = level.getWidth() * level.getHeight() * level.getDepth() / 256 / 64 << 1;
		for(int cave = 0; cave < caves; cave++) {
			this.setProgress(cave * 100 / (caves - 1) / 4);
			float baseX = this.random.nextFloat() * level.getWidth();
			float baseY = this.random.nextFloat() * level.getHeight();
			float baseZ = this.random.nextFloat() * level.getDepth();
			int total = (int) ((this.random.nextFloat() + this.random.nextFloat()) * 200.0F);
			float theta1 = this.random.nextFloat() * (float) Math.PI * 2;
			float theta1Mod = 0;
			float theta2 = this.random.nextFloat() * (float) Math.PI * 2;
			float theta2Mod = 0;
			float rad = this.random.nextFloat() * this.random.nextFloat();

			for(int count = 0; count < total; count++) {
				baseX += MathHelper.sin(theta1) * MathHelper.cos(theta2);
				baseZ += MathHelper.cos(theta1) * MathHelper.cos(theta2);
				baseY += MathHelper.sin(theta2);
				theta1 += theta1Mod * 0.18f;
				theta1Mod = theta1Mod + (this.random.nextFloat() - this.random.nextFloat());
				theta2 = (theta2 + theta2Mod * 0.5f) * 0.375f;
				theta2Mod = theta2Mod + (this.random.nextFloat() - this.random.nextFloat());
				if(this.random.nextFloat() >= 0.25f) {
					float cx = baseX + (this.random.nextFloat() * 4 - 2) * 0.2f;
					float cy = baseY + (this.random.nextFloat() * 4 - 2) * 0.2f;
					float cz = baseZ + (this.random.nextFloat() * 4 - 2) * 0.2f;
					float radius = (level.getHeight() - cy) / level.getHeight();
					radius = 1.2F + (radius * 3.5f + 1) * rad;
					radius = MathHelper.sin(count * (float) Math.PI / total) * radius;
					for(int bx = (int) (cx - radius); bx <= (int) (cx + radius); bx++) {
						for(int by = (int) (cy - radius); by <= (int) (cy + radius); by++) {
							for(int bz = (int) (cz - radius); bz <= (int) (cz + radius); bz++) {
								float dx = bx - cx;
								float dy = by - cy;
								float dz = bz - cz;
								if(dx * dx + dy * dy * 2 + dz * dz < radius * radius && bx >= 1 && by >= 1 && bz >= 1 && bx < level.getWidth() - 1 && by < level.getHeight() - 1 && bz < level.getDepth() - 1) {
									int key = (by * level.getDepth() + bz) * level.getWidth() + bx;
									if(data[key] == VanillaBlock.STONE.getId()) {
										data[key] = 0;
									}
								}
							}
						}
					}
				}
			}
		}

		this.populateOre(level, VanillaBlock.COAL_ORE.getId(), 90, 1, data);
		this.populateOre(level, VanillaBlock.IRON_ORE.getId(), 70, 2, data);
		this.populateOre(level, VanillaBlock.GOLD_ORE.getId(), 50, 3, data);
		this.setStep(OpenClassic.getGame().getTranslator().translate("level.watering"));
		this.setProgress(0);
		for(int x = 0; x < level.getWidth(); x++) {
			this.flood(level, x, level.getHeight() / 2 - 1, 0, VanillaBlock.WATER.getId(), data);
			this.flood(level, x, level.getHeight() / 2 - 1, level.getDepth() - 1, VanillaBlock.WATER.getId(), data);
		}

		for(int z = 0; z < level.getDepth(); z++) {
			this.flood(level, 0, level.getHeight() / 2 - 1, z, VanillaBlock.WATER.getId(), data);
			this.flood(level, level.getWidth() - 1, level.getHeight() / 2 - 1, z, VanillaBlock.WATER.getId(), data);
		}

		int waterFloods = level.getWidth() * level.getDepth() / 8000;
		for(int flood = 0; flood < waterFloods; flood++) {
			if(flood % 100 == 0) {
				this.setProgress(flood * 100 / (waterFloods - 1));
			}

			int x = this.random.nextInt(level.getWidth());
			int y = level.getWaterLevel() - 1 - this.random.nextInt(2);
			int z = this.random.nextInt(level.getDepth());
			if(data[(y * level.getDepth() + z) * level.getWidth() + x] == 0) {
				this.flood(level, x, y, z, VanillaBlock.WATER.getId(), data);
			}
		}

		this.setProgress(100);
		this.setStep(OpenClassic.getGame().getTranslator().translate("level.melting"));
		int lavaFloods = level.getWidth() * level.getDepth() * level.getHeight() / 20000;
		for(int flood = 0; flood < lavaFloods; flood++) {
			if(flood % 100 == 0) {
				this.setProgress(flood * 100 / (lavaFloods - 1));
			}

			int x = this.random.nextInt(level.getWidth());
			int y = (int) (this.random.nextFloat() * this.random.nextFloat() * (level.getWaterLevel() - 3));
			int z = this.random.nextInt(level.getDepth());
			if(data[(y * level.getDepth() + z) * level.getWidth() + x] == 0) {
				this.flood(level, x, y, z, VanillaBlock.LAVA.getId(), data);
			}
		}

		this.setProgress(100);
		this.setStep(OpenClassic.getGame().getTranslator().translate("level.growing"));
		OctaveNoise growNoise1 = new OctaveNoise(this.random, 8);
		OctaveNoise growNoise2 = new OctaveNoise(this.random, 8);
		for(int x = 0; x < level.getWidth(); x++) {
			this.setProgress(x * 100 / (level.getWidth() - 1));
			for(int z = 0; z < level.getDepth(); z++) {
				boolean sandy = growNoise1.compute(x, z) > 8;
				boolean gravelWater = growNoise2.compute(x, z) > 12;
				int y = flatNoise[x + z * level.getWidth()];
				int key = (y * level.getDepth() + z) * level.getWidth() + x;
				int block = data[((y + 1) * level.getDepth() + z) * level.getWidth() + x] & 255;
				if((block == VanillaBlock.WATER.getId() || block == VanillaBlock.STATIONARY_WATER.getId()) && y <= level.getDepth() / 2 - 1 && gravelWater) {
					data[key] = VanillaBlock.GRAVEL.getId();
				}

				if(block == 0) {
					byte id = VanillaBlock.GRASS.getId();
					if(y <= level.getHeight() / 2 - 1 && sandy) {
						id = VanillaBlock.SAND.getId();
					}

					data[key] = id;
				}
			}
		}

		this.setStep(OpenClassic.getGame().getTranslator().translate("level.planting"));
		int flowers = level.getWidth() * level.getDepth() / 3000;
		for(int flower = 0; flower < flowers; flower++) {
			this.setProgress(flower * 50 / (flowers - 1));
			int type = this.random.nextInt(2);
			int x = this.random.nextInt(level.getWidth());
			int z = this.random.nextInt(level.getDepth());

			for(int xc = 0; xc < 10; xc++) {
				int fx = x;
				int fz = z;
				for(int zc = 0; zc < 5; zc++) {
					fx += this.random.nextInt(6) - this.random.nextInt(6);
					fz += this.random.nextInt(6) - this.random.nextInt(6);
					if((type < 2 || this.random.nextInt(4) == 0) && fx >= 0 && fz >= 0 && fx < level.getWidth() && fz < level.getDepth()) {
						int y = flatNoise[fx + fz * level.getWidth()] + 1;
						if((data[(y * level.getDepth() + fz) * level.getWidth() + fx] & 255) == 0) {
							int key = (y * level.getDepth() + fz) * level.getWidth() + fx;
							if((data[((y - 1) * level.getDepth() + fz) * level.getWidth() + fx] & 255) == VanillaBlock.GRASS.getId()) {
								if(type == 0) {
									data[key] = VanillaBlock.DANDELION.getId();
								} else if(type == 1) {
									data[key] = VanillaBlock.ROSE.getId();
								}
							}
						}
					}
				}
			}
		}

		int shrooms = level.getWidth() * level.getHeight() * level.getDepth() / 2000;
		for(int shroom = 0; shroom < shrooms; shroom++) {
			int type = this.random.nextInt(2);
			this.setProgress(shroom * 50 / (shrooms - 1) + 50);
			int x = this.random.nextInt(level.getWidth());
			int y = this.random.nextInt(level.getHeight());
			int z = this.random.nextInt(level.getDepth());
			for(int xc = 0; xc < 20; xc++) {
				int mx = x;
				int my = y;
				int mz = z;
				for(int zc = 0; zc < 5; zc++) {
					mx += this.random.nextInt(6) - this.random.nextInt(6);
					my += this.random.nextInt(2) - this.random.nextInt(2);
					mz += this.random.nextInt(6) - this.random.nextInt(6);
					if((type < 2 || this.random.nextInt(4) == 0) && mx >= 0 && mz >= 0 && my >= 1 && mx < level.getWidth() && mz < level.getDepth() && my < flatNoise[mx + mz * level.getWidth()] - 1 && (data[(my * level.getDepth() + mz) * level.getWidth() + mx] & 255) == 0) {
						int key = (my * level.getDepth() + mz) * level.getWidth() + mx;
						if((data[((my - 1) * level.getDepth() + mz) * level.getWidth() + mx] & 255) == VanillaBlock.STONE.getId()) {
							if(type == 0) {
								data[key] = VanillaBlock.BROWN_MUSHROOM.getId();
							} else if(type == 1) {
								data[key] = VanillaBlock.RED_MUSHROOM.getId();
							}
						}
					}
				}
			}
		}

		int trees = level.getWidth() * level.getDepth() / 4000;
		for(int tree = 0; tree < trees; tree++) {
			this.setProgress(tree * 50 / (trees - 1) + 50);
			int x = this.random.nextInt(level.getWidth());
			int z = this.random.nextInt(level.getDepth());
			for(int xc = 0; xc < 20; xc++) {
				int tx = x;
				int tz = z;
				for(int zc = 0; zc < 20; zc++) {
					tx += this.random.nextInt(6) - this.random.nextInt(6);
					tz += this.random.nextInt(6) - this.random.nextInt(6);
					if(tx >= 0 && tz >= 0 && tx < level.getWidth() && tz < level.getDepth()) {
						int y = flatNoise[tx + tz * level.getWidth()] + 1;
						if(this.random.nextInt(4) == 0) {
							this.growTree(level, data, tx, y, tz);
						}
					}
				}
			}
		}
	}

	private boolean growTree(Level level, byte blocks[], int x, int y, int z) {
		int height = this.random.nextInt(3) + 4;
		boolean spaceFree = true;
		for(int by = y; by <= y + 1 + height; by++) {
			byte radius = 1;
			if(by == y) {
				radius = 0;
			}

			if(by >= y + 1 + height - 2) {
				radius = 2;
			}

			for(int bx = x - radius; bx <= x + radius && spaceFree; bx++) {
				for(int bz = z - radius; bz <= z + radius && spaceFree; bz++) {
					if(bx >= 0 && by >= 0 && bz >= 0 && bx < level.getWidth() && by < level.getHeight() && bz < level.getDepth()) {
						if((blocks[(by * level.getDepth() + bz) * level.getWidth() + bx] & 255) != 0) {
							spaceFree = false;
						}
					} else {
						spaceFree = false;
					}
				}
			}
		}

		if(!spaceFree) {
			return false;
		} else if((blocks[((y - 1) * level.getDepth() + z) * level.getWidth() + x] & 255) == VanillaBlock.GRASS.getId() && y < level.getHeight() - height - 1) {
			blocks[((y - 1) * level.getDepth() + z) * level.getWidth() + x] = VanillaBlock.DIRT.getId();
			for(int ly = y - 3 + height; ly <= y + height; ly++) {
				int baseDist = ly - (y + height);
				int radius = 1 - baseDist / 2;
				for(int lx = x - radius; lx <= x + radius; lx++) {
					int xdist = lx - x;
					for(int lz = z - radius; lz <= z + radius; lz++) {
						int zdist = lz - z;
						if(Math.abs(xdist) != radius || Math.abs(zdist) != radius || this.random.nextInt(2) != 0 && baseDist != 0) {
							blocks[(ly * level.getDepth() + lz) * level.getWidth() + lx] = VanillaBlock.LEAVES.getId();
						}
					}
				}
			}

			for(int ly = 0; ly < height; ly++) {
				blocks[((y + ly) * level.getDepth() + z) * level.getWidth() + x] = VanillaBlock.LOG.getId();
			}

			return true;
		} else {
			return false;
		}
	}

	private void populateOre(Level level, byte id, int chance, int stage, byte data[]) {
		int ores = level.getWidth() * level.getHeight() * level.getDepth() / 256 / 64 * chance / 100;
		for(int ore = 0; ore < ores; ore++) {
			this.setProgress(ore * 100 / (ores - 1) / 4 + stage * 100 / 4);
			float bx = this.random.nextFloat() * level.getWidth();
			float by = this.random.nextFloat() * level.getHeight();
			float bz = this.random.nextFloat() * level.getDepth();
			int total = (int) ((this.random.nextFloat() + this.random.nextFloat()) * 75 * chance / 100f);
			float theta1 = this.random.nextFloat() * MathHelper.TWO_PI;
			float theta1Mod = 0;
			float theta2 = this.random.nextFloat() * MathHelper.TWO_PI;
			float theta2Mod = 0;

			for(int count = 0; count < total; count++) {
				bx += MathHelper.sin(theta1) * MathHelper.cos(theta2);
				bz += MathHelper.cos(theta1) * MathHelper.cos(theta2);
				by += MathHelper.sin(theta2);
				theta1 += theta1Mod * 0.2f;
				theta1Mod = (theta1Mod * 0.9f) + (this.random.nextFloat() - this.random.nextFloat());
				theta2 = (theta2 + theta2Mod * 0.5f) * 0.5f;
				theta2Mod = (theta2Mod * 0.9f) + (this.random.nextFloat() - this.random.nextFloat());
				float radius = MathHelper.sin(count * MathHelper.PI / total) * chance / 100f + 1;
				for(int ox = (int) (bx - radius); ox <= (int) (bx + radius); ox++) {
					for(int oy = (int) (by - radius); oy <= (int) (by + radius); oy++) {
						for(int oz = (int) (bz - radius); oz <= (int) (bz + radius); oz++) {
							float dx = ox - bx;
							float dy = oy - by;
							float dz = oz - bz;
							if(dx * dx + dy * dy * 2 + dz * dz < radius * radius && ox >= 1 && oy >= 1 && oz >= 1 && ox < level.getWidth() - 1 && oy < level.getHeight() - 1 && oz < level.getDepth() - 1) {
								int key = (oy * level.getDepth() + oz) * level.getWidth() + ox;
								if(data[key] == VanillaBlock.STONE.getId()) {
									data[key] = id;
								}
							}
						}
					}
				}
			}
		}
	}

	private void flood(Level level, int x, int y, int z, byte id, byte blocks[]) {
		ArrayList<int[]> datas = new ArrayList<int[]>();
		int xshift = 1;
		int zshift = 1;
		while(1 << xshift < level.getWidth()) {
			xshift++;
		}

		while(1 << zshift < level.getDepth()) {
			zshift++;
		}

		int maxX = level.getWidth() - 1;
		int maxZ = level.getDepth() - 1;
		int counter = 1;
		this.floodData[0] = ((y << zshift) + z << xshift) + x;
		int flatArea = level.getWidth() * level.getDepth();
		while(counter > 0) {
			counter--;
			int tail = this.floodData[counter];
			if(counter == 0 && datas.size() > 0) {
				this.floodData = datas.remove(datas.size() - 1);
				counter = this.floodData.length;
			}

			int zval = tail >> xshift & maxZ;
			int xval = tail >> xshift + zshift;
			int xmin = tail & maxX;
			int xmax = tail & maxX;
			while(xmin > 0 && blocks[tail - 1] == 0) {
				xmin--;
				tail--;
			}

			while(xmax < level.getWidth() && blocks[tail + xmax - xmin] == 0) {
				xmax++;
			}

			boolean zMinComplete = false;
			boolean zMaxComplete = false;
			boolean xComplete = false;
			while(xmin < xmax) {
				blocks[tail] = id;
				boolean air = false;
				if(zval > 0) {
					air = blocks[tail - level.getWidth()] == 0;
					if(air && !zMinComplete) {
						if(counter == this.floodData.length) {
							datas.add(this.floodData);
							this.floodData = new int[1048576];
							counter = 0;
						}

						this.floodData[counter++] = tail - level.getWidth();
					}

					zMinComplete = air;
				}

				if(zval < level.getDepth() - 1) {
					air = blocks[tail + level.getWidth()] == 0;
					if(air && !zMaxComplete) {
						if(counter == this.floodData.length) {
							datas.add(this.floodData);
							this.floodData = new int[1048576];
							counter = 0;
						}

						this.floodData[counter++] = tail + level.getWidth();
					}

					zMaxComplete = air;
				}

				if(xval > 0) {
					byte endId = blocks[tail - flatArea];
					if((id == VanillaBlock.LAVA.getId() || id == VanillaBlock.STATIONARY_LAVA.getId()) && (endId == VanillaBlock.WATER.getId() || endId == VanillaBlock.STATIONARY_WATER.getId())) {
						blocks[tail - flatArea] = VanillaBlock.STONE.getId();
					}

					air = endId == 0;
					if(air && !xComplete) {
						if(counter == this.floodData.length) {
							datas.add(this.floodData);
							this.floodData = new int[1048576];
							counter = 0;
						}

						this.floodData[counter++] = tail - flatArea;
					}

					xComplete = air;
				}

				tail++;
				xmin++;
			}
		}
	}

	private void setStep(String step) {
		if(OpenClassic.getClient() != null) {
			OpenClassic.getClient().getProgressBar().setText(step);
			OpenClassic.getClient().getProgressBar().render();
		}
	}

	private void setProgress(int progress) {
		this.progressCounter++;
		if(OpenClassic.getClient() != null && this.progressCounter % 10 == 0) {
			OpenClassic.getClient().getProgressBar().setProgress(progress);
			OpenClassic.getClient().getProgressBar().renderBar();
		}
	}
}
