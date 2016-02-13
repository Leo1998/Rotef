package com.rotef.game.world;

import com.badlogic.gdx.files.FileHandle;
import com.rotef.game.util.file.DFAException;
import com.rotef.game.util.file.DirectFileAccess;

public class ChunkLoader {

	private static final int WORLD_NAME_OFFSET = 0;
	private static final int WORLD_SIZE_OFFSET = World.WORLD_NAME_MAX_LENGTH;
	private static final int WORLD_CHUNK_DATA_OFFSET = WORLD_SIZE_OFFSET + Integer.BYTES * 2;

	private static final int WORLD_CHUNK_SIZE = WorldChunk.CHUNK_SIZE * WorldChunk.CHUNK_SIZE * Integer.BYTES;

	private FileHandle rootDir;
	private FileHandle dataFile;
	private FileHandle heightmapFile;

	private DirectFileAccess dfa;
	private DirectFileAccess dfaHeightmap;

	private boolean isFirstInit = false;

	private World world;

	public ChunkLoader(World world) {
		this.world = world;

		rootDir = world.getDescriptor().getDirectory();
		rootDir.mkdirs();

		dataFile = rootDir.child("world.bin");
		if (!dataFile.exists()) {
			dataFile.write(false);// creates new file
		}

		heightmapFile = rootDir.child("heightmap.bin");
		if (!heightmapFile.exists()) {
			heightmapFile.write(false);// creates new file
		}

		this.dfa = new DirectFileAccess(dataFile);
		this.dfaHeightmap = new DirectFileAccess(heightmapFile);

		if (!isInitialized()) {
			this.isFirstInit = true;
		}
	}

	private boolean isInitialized() {
		return dfa.length() > 0;
	}

	public void initialize() {
		if (!isInitialized()) {
			firstInit();
		}
	}

	private void firstInit() {
		try {
			writeWorldName(world.getDescriptor().getName());
			writeWorldSize(world.getWidth(), world.getHeight());
			reserveChunkData();
		} catch (DFAException e) {
			e.printStackTrace();
		}
	}

	private void reserveChunkData() throws DFAException {
		dfa.seek(WORLD_CHUNK_DATA_OFFSET);

		int chunkCount = (world.getWidth() / WorldChunk.CHUNK_SIZE) * (world.getHeight() / WorldChunk.CHUNK_SIZE);
		for (int i = 0; i < chunkCount; i++) {
			for (int j = 0; j < WorldChunk.CHUNK_SIZE * WorldChunk.CHUNK_SIZE; j++) {
				dfa.writeInt(0);
			}
		}
	}

	public int readWorldWidth() throws DFAException {
		dfa.seek(WORLD_SIZE_OFFSET);
		return dfa.readInt();
	}

	public int readWorldHeight() throws DFAException {
		dfa.seek(WORLD_SIZE_OFFSET + Integer.BYTES);
		return dfa.readInt();
	}

	public void writeWorldSize(int width, int height) throws DFAException {
		dfa.seek(WORLD_SIZE_OFFSET);
		dfa.writeInt(width);
		dfa.writeInt(height);
	}

	public String readWorldName() throws DFAException {
		dfa.seek(WORLD_NAME_OFFSET);
		return dfa.readString(World.WORLD_NAME_MAX_LENGTH);
	}

	public void writeWorldName(String name) throws DFAException {
		dfa.seek(WORLD_NAME_OFFSET);
		dfa.writeString(name, World.WORLD_NAME_MAX_LENGTH);
	}

	public WorldHeightmap readWorldHeightmap() throws DFAException {
		WorldHeightmap heightmap = new WorldHeightmap(world.getWidth());

		dfaHeightmap.seek(0);

		for (int x = 0; x < world.getWidth(); x++) {
			heightmap.setHeight(x, dfaHeightmap.readInt());
		}

		return heightmap;
	}

	public void writeWorldHeightmap(WorldHeightmap heightmap) throws DFAException {
		dfaHeightmap.seek(0);

		for (int x = 0; x < world.getWidth(); x++) {
			dfaHeightmap.writeInt(heightmap.getHeight(x));
		}
	}

	private void seekChunk(int chunkX, int chunkY) {
		int i = chunkX + chunkY * (world.getWidth() / WorldChunk.CHUNK_SIZE);

		dfa.seek(WORLD_CHUNK_DATA_OFFSET + i * WORLD_CHUNK_SIZE);
	}

	public RawChunkData readChunk(int chunkX, int chunkY) throws DFAException, InvalidChunkDataException {
		if (!hasChunk(chunkX, chunkY)) {
			return null;
		}

		seekChunk(chunkX, chunkY);

		int[] rawData = new int[WorldChunk.CHUNK_SIZE * WorldChunk.CHUNK_SIZE];

		for (int i = 0; i < rawData.length; i++) {
			rawData[i] = dfa.readInt();
		}

		RawChunkData data = new RawChunkData(rawData);

		if (!data.isValid()) {
			throw new InvalidChunkDataException("Invalid Chunk Data!");
		}

		return data;
	}

	public synchronized void saveChunk(WorldChunk chunk) throws DFAException {
		int chunkX = chunk.getChunkX();
		int chunkY = chunk.getChunkY();

		seekChunk(chunkX, chunkY);

		int[] rawData = chunk.getRawData();

		for (int i = 0; i < rawData.length; i++) {
			dfa.writeInt(rawData[i]);
		}

	}

	public boolean hasChunk(int chunkX, int chunkY) {
		if (chunkX < 0 || chunkX >= (world.getWidth() / WorldChunk.CHUNK_SIZE) || chunkY < 0 || chunkY >= (world.getHeight() / WorldChunk.CHUNK_SIZE)) {
			return false;
		}

		return true;
	}

	public boolean isFirstInit() {
		return isFirstInit;
	}

	public void dispose() {
		dfa.close();
		dfaHeightmap.close();
	}

}
