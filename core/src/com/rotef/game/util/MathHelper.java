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

	public static double distance(double x0, double y0, double x1, double y1) {
		double kx = (x0 - x1);
		double ky = (y0 - y1);
		return Math.abs(Math.sqrt((kx * kx) + (ky * ky)));
	}

	public static float round(float value, int places) {
		float m = (float) Math.pow(10, places);

		return Math.round(value * m) / m;
	}
}
