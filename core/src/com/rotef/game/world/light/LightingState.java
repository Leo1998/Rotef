package com.rotef.game.world.light;

public class LightingState {

	public float r = 0f;
	public float g = 0f;
	public float b = 0f;

	public float r2 = 0f;
	public float g2 = 0f;
	public float b2 = 0f;

	private final int x;
	private final int y;

	public LightingState(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void set(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public void set2(float r, float g, float b) {
		this.r2 = r;
		this.g2 = g;
		this.b2 = b;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean isLit() {
		return r2 > 0.0f || g2 > 0.0f || b2 > 0.0f;
	}

	public void clear() {
		r = 0.0f;
		g = 0.0f;
		b = 0.0f;
		r2 = 0.0f;
		g2 = 0.0f;
		b2 = 0.0f;
	}

}
