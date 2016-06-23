package com.rotef.game.world;

public abstract class WorldChunkData {

	protected int[][] data = null;

	public int[][] getData() {
		return data;
	}

	public boolean isValid() {
		return data != null && data.length == 2 && data[0].length == WorldChunk.CHUNK_SIZE * WorldChunk.CHUNK_SIZE;
	}

}
