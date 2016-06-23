package com.rotef.game.world.tile;

import com.rotef.game.template.Template;
import com.rotef.game.world.Layer;
import com.rotef.game.world.World;

public class TileSolid extends Tile {

	public TileSolid(Template template, World world, Layer layer, int xTile, int yTile) {
		super(template, world, layer, xTile, yTile);

		this.setSolid(true);
	}

	@Override
	public void update(float delta) {
	}

}
