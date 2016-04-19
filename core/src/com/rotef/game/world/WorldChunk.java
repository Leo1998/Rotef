package com.rotef.game.world;

import com.badlogic.gdx.Gdx;
import com.rotef.game.world.physics.PhysicsSpatial;
import com.rotef.game.world.tile.Tile;

public final class WorldChunk {

	public static final int CHUNK_SIZE = 8;

	private int chunkX;
	private int chunkY;
	private Tile[] tiles;
	private boolean active = false;
	private PhysicsSpatial physicsSpatial = null;

	private World world;

	private WorldChunkData saveData;

	public WorldChunk(int chunkX, int chunkY, World world, WorldChunkData buildData) {
		this.chunkX = chunkX;
		this.chunkY = chunkY;
		this.world = world;

		build(buildData);
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

				setTile(x, y, world.getTileManager().createTile(id, world, xTile, yTile), true);
			}
		}
	}

	public void save() {
		// TODO only save if nessessary

		int[] rawData = new int[CHUNK_SIZE * CHUNK_SIZE];

		for (int y = 0; y < CHUNK_SIZE; y++) {
			for (int x = 0; x < CHUNK_SIZE; x++) {
				rawData[x + y * CHUNK_SIZE] = getTileId(x, y);
			}
		}

		this.saveData = new RawChunkData(rawData);
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

	/**
	 * 
	 * 
	 * @param xTile
	 *            in chunk coordinates
	 * @param yTile
	 *            in chunk coordinates
	 * @param tile
	 * @param skipTileUpdate
	 */
	public void setTile(int xTile, int yTile, Tile tile, boolean skipTileUpdate) {
		if (xTile >= 0 && xTile < CHUNK_SIZE && yTile >= 0 && yTile < CHUNK_SIZE) {
			Tile oldTile = tiles[xTile + yTile * CHUNK_SIZE];

			tiles[xTile + yTile * CHUNK_SIZE] = tile;

			if (!skipTileUpdate) {
				tileUpdate(oldTile, tile, xTile + chunkX * CHUNK_SIZE, yTile + chunkY * CHUNK_SIZE);
			}
		}
	}

	private void tileUpdate(Tile oldTile, Tile tile, int xTile, int yTile) {
		world.onTileUpdate(this, oldTile, tile, xTile, yTile);

		physicsSpatial.update();
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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		if (!this.active && active) {
			activate();
		} else if (this.active && !active) {
			deactivate();
		}

		this.active = active;
	}

	private void activate() {
		physicsSpatial = new PhysicsSpatial(this);
	}

	private void deactivate() {
		physicsSpatial.dispose();
		physicsSpatial = null;
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

	public WorldChunkData getSaveData() {
		return saveData;
	}

	public void clearSaveData() {
		this.saveData = null;
	}

}
