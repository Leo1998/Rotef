package com.rotef.game.world.light;

public class LightingState {

	private float r = 0f;
	private float g = 0f;
	private float b = 0f;
	private boolean lit = false;
	private final int x;
	private final int y;

	public LightingState(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public float getR() {
		return r;
	}

	public float getG() {
		return g;
	}

	public float getB() {
		return b;
	}

	public void set(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public boolean isLit() {
		return lit;
	}

	public void setLit(boolean lit) {
		this.lit = lit;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

}
