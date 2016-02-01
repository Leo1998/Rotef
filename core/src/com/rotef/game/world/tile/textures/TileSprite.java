package com.rotef.game.world.tile.textures;

import com.rotef.game.assets.Sprite;
import com.rotef.game.world.tile.Tile;

public abstract class TileSprite {

	protected final Tile tile;

	public TileSprite(Tile tile) {
		this.tile = tile;
	}

	public abstract Sprite getSprite();

	public void update() {
	}

	public Tile getTile() {
		return tile;
	}

}
