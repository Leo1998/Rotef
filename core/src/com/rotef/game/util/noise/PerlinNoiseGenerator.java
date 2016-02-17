package com.rotef.game.util.noise;

import java.util.Random;

public class PerlinNoiseGenerator extends NoiseGenerator {
	protected static final int grad3[][] = { { 1, 1, 0 }, { -1, 1, 0 }, { 1, -1, 0 }, { -1, -1, 0 }, { 1, 0, 1 },
			{ -1, 0, 1 }, { 1, 0, -1 }, { -1, 0, -1 }, { 0, 1, 1 }, { 0, -1, 1 }, { 0, 1, -1 }, { 0, -1, -1 } };

	protected PerlinNoiseGenerator() {
		// Hash lookup table as defined by Ken Perlin.
		int p[] = { 151, 160, 137, 91, 90, 15, 131, 13, 201, //
				95, 96, 53, 194, 233, 7, 225, 140, 36, 103, 30, 69, 142, 8, 99, 37, //
				240, 21, 10, 23, 190, 6, 148, 247, 120, 234, 75, 0, 26, 197, 62, //
				94, 252, 219, 203, 117, 35, 11, 32, 57, 177, 33, 88, 237, 149, 56, //
				87, 174, 20, 125, 136, 171, 168, 68, 175, 74, 165, 71, 134, 139, //
				48, 27, 166, 77, 146, 158, 231, 83, 111, 229, 122, 60, 211, 133, //
				230, 220, 105, 92, 41, 55, 46, 245, 40, 244, 102, 143, 54, 65, 25, //
				63, 161, 1, 216, 80, 73, 209, 76, 132, 187, 208, 89, 18, 169, 200, //
				196, 135, 130, 116, 188, 159, 86, 164, 100, 109, 198, 173, 186, 3, //
				64, 52, 217, 226, 250, 124, 123, 5, 202, 38, 147, 118, 126, 255, //
				82, 85, 212, 207, 206, 59, 227, 47, 16, 58, 17, 182, 189, 28, 42, //
				223, 183, 170, 213, 119, 248, 152, 2, 44, 154, 163, 70, 221, 153, //
				101, 155, 167, 43, 172, 9, 129, 22, 39, 253, 19, 98, 108, 110, 79, //
				113, 224, 232, 178, 185, 112, 104, 218, 246, 97, 228, 251, 34, 242, //
				193, 238, 210, 144, 12, 191, 179, 162, 241, 81, 51, 145, 235, 249, //
				14, 239, 107, 49, 192, 214, 31, 181, 199, 106, 157, 184, 84, 204, //
				176, 115, 121, 50, 45, 127, 4, 150, 254, 138, 236, 205, 93, 222, //
				114, 67, 29, 24, 72, 243, 141, 128, 195, 78, 66, 215, 61, 156, 180 };

		for (int i = 0; i < 512; i++) {
			permutation[i] = p[i & 255];
		}
	}

	public PerlinNoiseGenerator(long seed) {
		this(new Random(seed));
	}

	public PerlinNoiseGenerator(Random rand) {
		offsetX = rand.nextDouble() * 256;
		offsetY = rand.nextDouble() * 256;
		offsetZ = rand.nextDouble() * 256;

		for (int i = 0; i < 256; i++) {
			permutation[i] = rand.nextInt(256);
		}

		for (int i = 0; i < 256; i++) {
			int pos = rand.nextInt(256 - i) + i;
			int old = permutation[i];

			permutation[i] = permutation[pos];
			permutation[pos] = old;
			permutation[i + 256] = permutation[i];
		}
	}

	@Override
	public double noise(double x, double y, double z) {
		x += offsetX;
		y += offsetY;
		z += offsetZ;

		int floorX = floor(x);
		int floorY = floor(y);
		int floorZ = floor(z);

		// Find unit cube containing the point
		int X = floorX & 255;
		int Y = floorY & 255;
		int Z = floorZ & 255;

		// Get relative xyz coordinates of the point within the cube
		x -= floorX;
		y -= floorY;
		z -= floorZ;

		// Compute fade curves for xyz
		double fX = fade(x);
		double fY = fade(y);
		double fZ = fade(z);

		// Hash coordinates of the cube corners
		int A = permutation[X] + Y;
		int AA = permutation[A] + Z;
		int AB = permutation[A + 1] + Z;
		int B = permutation[X + 1] + Y;
		int BA = permutation[B] + Z;
		int BB = permutation[B + 1] + Z;

		return lerp(fZ,
				lerp(fY, lerp(fX, grad(permutation[AA], x, y, z), grad(permutation[BA], x - 1, y, z)),
						lerp(fX, grad(permutation[AB], x, y - 1, z), grad(permutation[BB], x - 1, y - 1, z))),
				lerp(fY, lerp(fX, grad(permutation[AA + 1], x, y, z - 1), grad(permutation[BA + 1], x - 1, y, z - 1)),
						lerp(fX, grad(permutation[AB + 1], x, y - 1, z - 1),
								grad(permutation[BB + 1], x - 1, y - 1, z - 1))));
	}
}
