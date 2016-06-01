package com.rotef.game.world.light;

public class LightingState {

	public float r = 0f;
	public float g = 0f;
	public float b = 0f;
	public boolean lit = false;
	public final int x;
	public final int y;

	public LightingState(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void set(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
}
