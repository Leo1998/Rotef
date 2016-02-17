package com.rotef.game.world.generator;

import java.lang.reflect.Constructor;
import java.util.Random;

import com.rotef.game.util.StatusListener;

public abstract class Generator {

	public static Generator createGeneratorInstance(Class<? extends Generator> clazz, StatusListener listener)
			throws GeneratorException {
		try {
			Constructor<? extends Generator> constr = clazz.getConstructor(StatusListener.class);

			return constr.newInstance(listener);
		} catch (Exception e) {
			throw new GeneratorException(e);
		}
	}

	protected Random random;

	private StatusListener listener;

	protected Generator(StatusListener listener) {
		this.listener = listener;

		this.random = new Random();
	}

	public abstract int[] generateMap(int width, int height);

	protected synchronized void sendProgress(String msg, float progress) {
		listener.status(msg, progress);
	}

}
