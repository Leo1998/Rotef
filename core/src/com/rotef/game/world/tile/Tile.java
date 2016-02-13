package com.rotef.game.world.tile;

import com.rotef.game.world.World;
import com.rotef.game.world.tile.textures.TileSprite;

public abstract class Tile {

	public static final int TILE_SIZE = 32;
	public static final int TILE_TEXTURE_SIZE = 16;

	private final int id;
	private final World world;

	private int xTile;
	private int yTile;
	private boolean solid = false;

	private TileSprite sprite;

	private boolean hasTileAbove = false;
	private boolean hasTileRight = false;
	private boolean hasTileBeneath = false;
	private boolean hasTileLeft = false;

	Tile(int id, World world, int xTile, int yTile) {
		this.id = id;
		this.world = world;
		this.xTile = xTile;
		this.yTile = yTile;
	}

	/**
	 * Do not use this method it is only for updating internal stuff.
	 * 
	 * @param delta
	 */
	public void internalUpdate(float delta) {
		hasTileAbove = world.getTile(xTile, yTile + 1, false) != null;
		hasTileRight = world.getTile(xTile + 1, yTile, false) != null;
		hasTileBeneath = world.getTile(xTile, yTile - 1, false) != null;
		hasTileLeft = world.getTile(xTile - 1, yTile, false) != null;

		if (this.hasSprite()) {
			sprite.update();
		}
	}

	public abstract void update(float delta);

	public int getId() {
		return id;
	}

	public World getWorld() {
		return world;
	}

	public TileSprite getSprite() {
		return sprite;
	}

	public boolean hasSprite() {
		return sprite != null;
	}

	protected void setSprite(TileSprite sprite) {
		this.sprite = sprite;
	}

	public boolean hasTileAbove() {
		return hasTileAbove;
	}

	public boolean hasTileRight() {
		return hasTileRight;
	}

	public boolean hasTileBeneath() {
		return hasTileBeneath;
	}

	public boolean hasTileLeft() {
		return hasTileLeft;
	}

	public int getXTile() {
		return xTile;
	}

	public int getYTile() {
		return yTile;
	}

	public boolean isSolid() {
		return solid;
	}

	protected void setSolid(boolean solid) {
		this.solid = solid;
	}

}
