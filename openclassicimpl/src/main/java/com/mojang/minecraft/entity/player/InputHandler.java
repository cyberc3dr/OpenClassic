package com.mojang.minecraft.entity.player;

import org.spacehq.openclassic.api.OpenClassic;

public class InputHandler {

	public float xxa = 0;
	public float yya = 0;
	public boolean jumping = false;
	public boolean speed = false;
	public boolean flyDown = false;
	private int flyDelay = 0;
	public boolean toggleFly = false;

	private boolean[] keyStates = new boolean[10];

	public InputHandler() {
	}

	public void setKeyState(int key, boolean pressed) {
		byte index = -1;

		if(key == OpenClassic.getClient().getBindings().getBinding("options.keys.forward").getKey()) {
			index = 0;
		}

		if(key == OpenClassic.getClient().getBindings().getBinding("options.keys.back").getKey()) {
			index = 1;
		}

		if(key == OpenClassic.getClient().getBindings().getBinding("options.keys.left").getKey()) {
			index = 2;
		}

		if(key == OpenClassic.getClient().getBindings().getBinding("options.keys.right").getKey()) {
			index = 3;
		}

		if(key == OpenClassic.getClient().getBindings().getBinding("options.keys.jump").getKey()) {
			index = 4;
		}

		if(key == OpenClassic.getClient().getBindings().getBinding("options.keys.speedhack").getKey()) {
			index = 5;
		}

		if(key == OpenClassic.getClient().getBindings().getBinding("options.keys.fly-down").getKey()) {
			index = 6;
		}

		if(index >= 0) {
			this.keyStates[index] = pressed;
		}
	}

	public void resetKeys() {
		for(int index = 0; index < 10; index++) {
			this.keyStates[index] = false;
		}
	}

	public void updateMovement() {
		if(this.flyDelay > 0) {
			this.flyDelay--;
		}

		this.xxa = 0;
		this.yya = 0;

		if(this.keyStates[0]) {
			this.yya--;
		}

		if(this.keyStates[1]) {
			this.yya++;
		}

		if(this.keyStates[2]) {
			this.xxa--;
		}

		if(this.keyStates[3]) {
			this.xxa++;
		}

		this.jumping = this.keyStates[4];
		this.speed = this.keyStates[5];
		this.flyDown = this.keyStates[6];
	}

	public void keyPress(int key) {
		if(key == OpenClassic.getClient().getBindings().getBinding("options.keys.jump").getKey()) {
			if(this.flyDelay == 0) {
				this.flyDelay = 10;
			} else {
				this.toggleFly = true;
				this.flyDelay = 0;
			}
		}
	}
}
