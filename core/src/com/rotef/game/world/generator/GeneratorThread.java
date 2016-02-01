package com.rotef.game.world.generator;

import java.lang.reflect.Constructor;

import com.rotef.game.util.LoadingThread;

public class GeneratorThread extends LoadingThread {

	private Generator generator;
	private int width;
	private int height;

	public GeneratorThread(Class<? extends Generator> clazz, int width, int height) throws GeneratorException {
		this.generator = createGeneratorInstance(clazz);
		this.width = width;
		this.height = height;
	}

	private Generator createGeneratorInstance(Class<? extends Generator> clazz) throws GeneratorException {
		try {
			Constructor<? extends Generator> constr = clazz.getConstructor(GeneratorThread.class);

			return constr.newInstance(this);
		} catch (Exception e) {
			throw new GeneratorException(e);
		}
	}

	@Override
	protected Object runProcess() {
		int[] map = generator.generateMap(width, height);

		assert map.length == width * height;

		return map;
	}

}
