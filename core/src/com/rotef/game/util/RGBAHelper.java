package com.rotef.game.util;

import com.badlogic.gdx.math.MathUtils;

/**
 * This class is only for RGBA8888 encoded colors!
 */
public class RGBAHelper {

	private RGBAHelper() {
	}

	public static int[] getRGBA(int hex) {
		int r = (hex & 0xff000000) >> 24;
		int g = (hex & 0xff0000) >> 16;
		int b = (hex & 0xff00) >> 8;
		int a = (hex & 0xff);
		return new int[] { r, g, b, a };
	}

	public static int getHex(int r, int g, int b) {
		return getHex(r, g, b, 255);
	}

	public static int getHex(int r, int g, int b, int a) {
		return r << 24 | g << 16 | b << 8 | a;
	}

	public static int applyIntensity(int col, float intensity) {
		int r = (col & 0xff000000) >> 24;
		int g = (col & 0xff0000) >> 16;
		int b = (col & 0xff00) >> 8;
		int a = (col & 0xff);

		r *= intensity;
		g *= intensity;
		b *= intensity;
		a *= intensity;

		return getHex(r, g, b, a);
	}

	public static int mix(int col1, int col2) {
		int r1 = (col1 & 0xff000000) >> 24;
		int g1 = (col1 & 0xff0000) >> 16;
		int b1 = (col1 & 0xff00) >> 8;
		int a1 = (col1 & 0xff);

		int r2 = (col2 & 0xff000000) >> 24;
		int g2 = (col2 & 0xff0000) >> 16;
		int b2 = (col2 & 0xff00) >> 8;
		int a2 = (col2 & 0xff);

		int r = MathUtils.clamp(r1 + r2, 0, 255);
		int g = MathUtils.clamp(g1 + g2, 0, 255);
		int b = MathUtils.clamp(b1 + b2, 0, 255);
		int a = MathUtils.clamp(a1 + a2, 0, 255);

		return getHex(r, g, b, a);
	}

}
