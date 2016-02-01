package com.rotef.game.world;

public class WorldHeightmap {

	private int[] heightValues;

	public WorldHeightmap(int width) {
		this.heightValues = new int[width];
	}

	public int getWidth() {
		return heightValues.length;
	}

	public int[] getValues() {
		return heightValues;
	}

	public int getHeight(int x) {
		return heightValues[x];
	}

	public void setHeight(int x, int val) {
		heightValues[x] = val;
	}

}
