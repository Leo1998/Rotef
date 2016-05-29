package com.rotef.game.util;

public class MathHelper {

	private MathHelper() {
	}

	public static int ensureRange(int value, int min, int max) {
		return Math.min(Math.max(value, min), max);
	}

	public static float ensureRange(float value, float min, float max) {
		return Math.min(Math.max(value, min), max);
	}

	public static double ensureRange(double value, double min, double max) {
		return Math.min(Math.max(value, min), max);
	}

	public static double distance(int x0, int y0, int x1, int y1) {
		int kx = (x0 - x1);
		int ky = (y0 - y1);
		return Math.abs(Math.sqrt((kx * kx) + (ky * ky)));
	}
}
