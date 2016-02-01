package com.rotef.game.world.tile;

import java.util.HashMap;
import java.util.Map;

import com.rotef.game.world.World;

public class TileRegister {

	private Map<Integer, TileDef> tileDefs = new HashMap<Integer, TileDef>();

	public TileRegister() {
		{
			TileDef dirtDef = new TileDef(TileSolid.class, 1);
			dirtDef.addValue("sprite", "tiles/dirt.png");

			tileDefs.put(dirtDef.getId(), dirtDef);
		}
		{
			TileDef waterDef = new TileDef(TileFluid.class, 2);
			waterDef.addValue("tex", "tiles/water.png");

			tileDefs.put(waterDef.getId(), waterDef);
		}
		{
			TileDef stoneDef = new TileDef(TileSolid.class, 3);
			stoneDef.addValue("sprite", "tiles/stone.png");

			tileDefs.put(stoneDef.getId(), stoneDef);
		}
	}

	public Tile createTile(int id, World world, int xTile, int yTile) {
		if (!tileDefs.containsKey(id))
			return null;

		TileDef def = tileDefs.get(id);

		return def.createTile(id, world, xTile, yTile);
	}

}
