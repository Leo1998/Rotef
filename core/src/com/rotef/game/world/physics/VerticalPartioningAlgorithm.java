package com.rotef.game.world.physics;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.rotef.game.world.WorldChunk;
import com.rotef.game.world.tile.Tile;

public class VerticalPartioningAlgorithm implements PartitioningAlgorithm {

	@Override
	public Array<Rectangle> partition(WorldChunk chunk) {
		Array<Rectangle> rects = new Array<Rectangle>();

		for (int x = 0; x < WorldChunk.CHUNK_SIZE; x++) {
			int x0 = x;
			int y0 = -1;
			int w0 = 1;
			int h0 = 0;

			for (int y = 0; y < WorldChunk.CHUNK_SIZE; y++) {
				Tile tile = chunk.getTile(x, y);

				if (tile != null && tile.isSolid()) {
					if (y0 == -1) {
						y0 = y;
					}
					h0++;
				} else if (h0 != 0) {
					rects.add(new Rectangle(x0, y0, w0, h0));
					y0 = -1;
					h0 = 0;
				}
			}

			if (h0 != 0) {
				rects.add(new Rectangle(x0, y0, w0, h0));
			}
		}

		return rects;
	}

}
