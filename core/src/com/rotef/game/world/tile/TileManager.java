package com.rotef.game.world.tile;

import java.lang.reflect.Constructor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.rotef.game.world.World;

public class TileManager {

	private Array<TileTemplate> templates = new Array<TileTemplate>();

	public TileManager() {
		loadTemplate("tiles/dirt.json");
		loadTemplate("tiles/water.json");
		loadTemplate("tiles/stone.json");

		// {
		// TileDef dirtDef = new TileDef(TileSolid.class, 1);
		// dirtDef.addValue("sprite", "tiles/dirt.png");
		//
		// tileDefs.put(dirtDef.getId(), dirtDef);
		// }
		// {
		// TileDef waterDef = new TileDef(TileFluid.class, 2);
		// waterDef.addValue("tex", "tiles/water.png");
		//
		// tileDefs.put(waterDef.getId(), waterDef);
		// }
		// {
		// TileDef stoneDef = new TileDef(TileSolid.class, 3);
		// stoneDef.addValue("sprite", "tiles/stone.png");
		//
		// tileDefs.put(stoneDef.getId(), stoneDef);
		// }
	}

	private void loadTemplate(String path) {
		TileTemplate template = TileTemplate.loadTemplate(Gdx.files.internal(path));

		Gdx.app.log("TileManager", "Loaded TileTemplate (" + path + ", " + template.getName() + ")");

		templates.add(template);
	}

	public Tile createTile(int id, World world, int xTile, int yTile) {
		return createTile(findTemplate(id), world, xTile, yTile);
	}

	public Tile createTile(String name, World world, int xTile, int yTile) {
		return createTile(findTemplate(name), world, xTile, yTile);
	}

	public Tile createTile(TileTemplate template, World world, int xTile, int yTile) {
		if (template != null) {
			try {
				Class<? extends Tile> clazz = template.getType().clazz;

				Constructor<? extends Tile> c = clazz.getConstructor(TileTemplate.class, World.class, int.class, int.class);

				Tile tile = c.newInstance(template, world, xTile, yTile);

				return tile;
			} catch (Exception e) {
				Gdx.app.error("TileManager", "Failed creating a new Tile!", e);
				return null;
			}
		}
		return null;
	}

	private TileTemplate findTemplate(String name) {
		for (TileTemplate t : templates) {
			if (t.getName().equals(name)) {
				return t;
			}
		}

		return null;
	}

	private TileTemplate findTemplate(int id) {
		for (TileTemplate t : templates) {
			if (t.getId() == id) {
				return t;
			}
		}

		return null;
	}

}
