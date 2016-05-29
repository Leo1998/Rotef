package com.rotef.game.world.light;

public enum LightingMode {

	Low(1, false), Medium(2, false), High(2, true);

	public final int resolution;
	public final boolean smoothFiltering;

	LightingMode(int resolution, boolean smoothFiltering) {
		this.resolution = resolution;
		this.smoothFiltering = smoothFiltering;
	}

}
