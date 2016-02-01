package com.rotef.game.util;

public class StringUtils {

	public static String limitLength(String s, int maxLength) {
		int l = (s.length() < maxLength) ? s.length() : maxLength;
		return s.substring(0, l);
	}

}
