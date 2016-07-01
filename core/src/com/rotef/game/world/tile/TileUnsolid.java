package com.rotef.game.world.tile;

import com.rotef.game.template.Template;
import com.rotef.game.world.Layer;
import com.rotef.game.world.World;

public class TileUnsolid extends Tile {

	public TileUnsolid(Template template, World world, Layer layer, int xTile, int yTile) {
		super(template, world, layer, xTile, yTile);

		this.setSolid(false);
	}

	@Override
	public void update(float delta) {

	}

}
