package com.rotef.game.world;

import com.badlogic.gdx.Gdx;
import com.rotef.game.util.file.DFAException;
import com.rotef.game.world.tile.Tile;

public class WorldChunk {

	public static final int CHUNK_SIZE = 8;

	private int chunkX;
	private int chunkY;
	private Tile[] tiles;

	private World world;

	public WorldChunk(int chunkX, int chunkY, World world, WorldChunkData data) {
		this.chunkX = chunkX;
		this.chunkY = chunkY;
		this.world = world;

		build(data);
	}

	public void build(WorldChunkData data) {
		tiles = new Tile[WorldChunk.CHUNK_SIZE * WorldChunk.CHUNK_SIZE];

		if (!data.isValid()) {
			Gdx.app.error("WorldChunk(x:" + chunkX + " y:" + chunkY + ")", "Invalid chunk data!");
			return;
		}

		for (int x = 0; x < CHUNK_SIZE; x++) {
			for (int y = 0; y < CHUNK_SIZE; y++) {
				int id = data.getData()[x + y * CHUNK_SIZE];

				int xTile = chunkX * CHUNK_SIZE + x;
				int yTile = chunkY * CHUNK_SIZE + y;
				setTile(x, y, world.getTileRegister().createTile(id, world, xTile, yTile), true);
			}
		}
	}

	/**
	 * the same as calling world.unloadChunk(chunkX, chunkY);
	 */
	public void unload() {
		world.unloadChunk(chunkX, chunkY);
	}

	public synchronized void save() {
		ChunkLoader chunkLoader = world.getChunkLoader();

		try {
			chunkLoader.saveChunk(this);
		} catch (DFAException e) {
			Gdx.app.error("WorldChunk(x:" + chunkX + " y:" + chunkY + ")", "Failed to save chunk data!");
		}
	}

	public int getTileId(int xTile, int yTile) {
		Tile tile = getTile(xTile, yTile);

		if (tile != null) {
			return tile.getId();
		}

		return 0;
	}

	public Tile getTile(int xTile, int yTile) {
		if (xTile >= 0 && xTile < CHUNK_SIZE && yTile >= 0 && yTile < CHUNK_SIZE) {
			return tiles[xTile + yTile * CHUNK_SIZE];
		}

		return null;
	}

	public void setTile(int xTile, int yTile, Tile tile) {
		setTile(xTile, yTile, tile, false);
	}

	public void setTile(int xTile, int yTile, Tile tile, boolean skipTileUpdate) {
		if (xTile >= 0 && xTile < CHUNK_SIZE && yTile >= 0 && yTile < CHUNK_SIZE) {
			Tile oldTile = tiles[xTile + yTile * CHUNK_SIZE];

			if (!skipTileUpdate) {
				world.onTileUpdate(this, oldTile, tile, xTile, yTile);
			}

			tiles[xTile + yTile * CHUNK_SIZE] = tile;
		}
	}

	public void update(float delta) {
		for (int x = 0; x < CHUNK_SIZE; x++) {
			for (int y = 0; y < CHUNK_SIZE; y++) {
				Tile tile = getTile(x, y);
				if (tile != null) {
					tile.internalUpdate(delta);
					tile.update(delta);
				}
			}
		}
	}

	public int getChunkX() {
		return chunkX;
	}

	public int getChunkY() {
		return chunkY;
	}

	public World getWorld() {
		return world;
	}

	/**
	 * Generates an array storing the tiles as ids.
	 * 
	 * @return rawData
	 */
	public int[] getRawData() {
		int[] rawData = new int[CHUNK_SIZE * CHUNK_SIZE];

		for (int y = 0; y < CHUNK_SIZE; y++) {
			for (int x = 0; x < CHUNK_SIZE; x++) {
				rawData[x + y * CHUNK_SIZE] = getTileId(x, y);
			}
		}

		return rawData;
	}

}
