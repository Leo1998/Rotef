package com.rotef.game.world.generator;

import java.util.Random;

public abstract class Generator {

	protected Random random;

	protected GeneratorThread generatorThread;

	public Generator(GeneratorThread generatorThread) {
		this.generatorThread = generatorThread;
		this.random = new Random();
	}

	public abstract int[] generateMap(int width, int height);

	protected synchronized void sendProgress(float progress) {
		generatorThread.setProgress(progress);
	}

}
