package com.mojang.minecraft.entity.model;

public class ModelManager {

	private HumanoidModel humanoid = new HumanoidModel(0);
	private HumanoidModel humanoidWithArmor = new HumanoidModel(1);
	private CreeperModel creeper = new CreeperModel();
	private SkeletonModel skeleton = new SkeletonModel();
	private ZombieModel zombie = new ZombieModel();
	private AnimalModel pig = new PigModel();
	private AnimalModel sheep = new SheepModel();
	private SpiderModel spider = new SpiderModel();
	private SheepFurModel sheepFur = new SheepFurModel();

	public Model getModel(String name) {
		if(name.equals("humanoid")) {
			return this.humanoid;
		} else if(name.equals("humanoid.armor")) {
			return this.humanoidWithArmor;
		} else if(name.equals("creeper")) {
			return this.creeper;
		} else if(name.equals("skeleton")) {
			return this.skeleton;
		} else if(name.equals("zombie")) {
			return this.zombie;
		} else if(name.equals("pig")) {
			return this.pig;
		} else if(name.equals("sheep")) {
			return this.sheep;
		} else if(name.equals("spider")) {
			return this.spider;
		} else if(name.equals("sheep.fur")) {
			return this.sheepFur;
		}
		
		return null;
	}
	
}
