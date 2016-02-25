package com.rotef.game.world.physics;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.rotef.game.world.WorldChunk;

public interface PartitioningAlgorithm {

	public Array<Rectangle> partition(WorldChunk chunk);

}
