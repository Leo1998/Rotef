package com.rotef.game.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtils {

	private ExceptionUtils() {
	}

	public static String getStackTrace(Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));

		return sw.toString();
	}

}
