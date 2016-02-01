package com.rotef.game.world;

public class RawChunkData extends WorldChunkData {

	public RawChunkData(int[] chunkData) {
		this.data = new int[WorldChunk.CHUNK_SIZE * WorldChunk.CHUNK_SIZE];

		System.arraycopy(chunkData, 0, this.data, 0, this.data.length);
	}

}
