package com.rotef.game.world.generator;

import com.rotef.game.util.noise.PerlinNoiseGenerator;

public class OverworldGenerator extends Generator {

	public OverworldGenerator(GeneratorThread generatorThread) {
		super(generatorThread);
	}

	@Override
	public int[] generateMap(int width, int height) {
		int[] map = new int[width * height];

		double xScale = 32D;
		int featureSize = 32;
		int groundLevel = 170;

		int oceanLevel = groundLevel - 10;

		double caveScale = 11D;
		double caveOffset = 0.25D;

		PerlinNoiseGenerator heightNoise = new PerlinNoiseGenerator(random);
		PerlinNoiseGenerator caveNoise = new PerlinNoiseGenerator(random);

		for (double x = 0; x < width; x++) {
			double heightVal = heightNoise.noise(x / xScale, 4, 0.25, true);
			int yLevel = groundLevel + Math.round((float) heightVal * featureSize);

			// generate basic world
			for (int y = 0; y < yLevel; y++) {
				int tile = 3;

				int dirtLevel = 6 + random.nextInt(2) - 1;
				if (yLevel - y < dirtLevel) {
					tile = 1; // generate dirt
				}

				double caveVal = caveNoise.noise(x / caveScale, y / caveScale, 4, 0.1, true);

				double localCaveOffset = 0;
				if (yLevel - y < 10) {
					localCaveOffset = (yLevel - y) / 20D;
				}
				if (caveVal < caveOffset + localCaveOffset) {
					map[(int) (x + y * width)] = tile;
				}
			}

			// generate oceans and lakes
			if (yLevel <= oceanLevel) {
				for (int y = yLevel; y < oceanLevel; y++) {
					map[(int) (x + y * width)] = 2;
				}
			}

			sendProgress((float) (x / width));
		}

		sendProgress(1.0f);

		return map;
	}

}