package com.rotef.game.world.loader;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.rotef.game.world.RawChunkData;
import com.rotef.game.world.World;
import com.rotef.game.world.WorldChunk;
import com.rotef.game.world.WorldChunkData;

public class WorldChunkSerializer extends Serializer<WorldChunk> {

	private World world;

	public WorldChunkSerializer(World world) {
		this.world = world;
	}

	@Override
	public void write(Kryo kryo, Output output, WorldChunk o) {
		output.writeInt(o.getChunkX());
		output.writeInt(o.getChunkY());

		o.save();
		WorldChunkData data = o.getSaveData();
		if (data != null) {
			for (int l = 0; l < 2; l++) {
				output.writeInts(data.getData()[l]);
			}
		} else {

		}
		o.clearSaveData();
	}

	@Override
	public WorldChunk read(Kryo kryo, Input input, Class<WorldChunk> type) {
		int chunkX = input.readInt();
		int chunkY = input.readInt();
		int[] foreground = input.readInts(WorldChunk.CHUNK_SIZE * WorldChunk.CHUNK_SIZE);
		int[] background = input.readInts(WorldChunk.CHUNK_SIZE * WorldChunk.CHUNK_SIZE);
		int[][] data = new int[][] { foreground, background };

		return new WorldChunk(chunkX, chunkY, world, new RawChunkData(data));
	}
}
