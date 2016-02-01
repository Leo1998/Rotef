package com.rotef.game.world.tile;

import com.rotef.game.world.World;

public class TileSolid extends Tile {

	public TileSolid(int id, World world, int xTile, int yTile) {
		super(id, world, xTile, yTile);

		this.setSolid(true);
	}

	@Override
	public void update(float delta) {
	}

}
