package com.rotef.game.world;

public enum Layer {

	Foreground(0), Background(1);

	public final int index;

	Layer(int index) {
		this.index = index;
	}

	public static Layer get(int index) {
		if (index == 0) {
			return Layer.Foreground;
		} else if (index == 1) {
			return Layer.Background;
		}

		return null;
	}
}
