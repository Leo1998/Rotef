package com.rotef.game.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.Buffer;

public class BufferDisposer {

	private BufferDisposer() {
	}

	public static void disposeDirectBuffer(Buffer buffer) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException {
		Method cleanerMethod = buffer.getClass().getMethod("cleaner");
		cleanerMethod.setAccessible(true);
		Object cleaner = cleanerMethod.invoke(buffer);
		Method cleanMethod = cleaner.getClass().getMethod("clean");
		cleanMethod.setAccessible(true);
		cleanMethod.invoke(cleaner);

	}

}
