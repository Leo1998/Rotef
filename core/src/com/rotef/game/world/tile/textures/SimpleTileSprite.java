package com.rotef.game.world.tile.textures;

import com.rotef.game.assets.Sprite;
import com.rotef.game.world.tile.Tile;

public class SimpleTileSprite extends TileSprite {

	private Sprite sprite;

	public SimpleTileSprite(Tile tile, Sprite sprite) {
		super(tile);

		this.sprite = sprite;
	}

	@Override
	public Sprite getSprite() {
		return sprite;
	}

}
