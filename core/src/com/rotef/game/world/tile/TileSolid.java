package com.rotef.game.world.tile;

import com.rotef.game.world.World;

public class TileSolid extends Tile {

	public TileSolid(TileTemplate template, World world, int xTile, int yTile) {
		super(template, world, xTile, yTile);

		this.setSolid(true);
	}

	@Override
	public void update(float delta) {
	}

}
