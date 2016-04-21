package com.rotef.game.world.tile;

import java.lang.reflect.Constructor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.rotef.game.template.Template;
import com.rotef.game.world.World;

public class TileManager {

	private Array<Template> templates = new Array<Template>();

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
		Template template = Template.loadTemplate(Gdx.files.internal(path));

		Gdx.app.log("TileManager", "Loaded Template (" + path + ", " + template.get("name") + ")");

		templates.add(template);
	}

	public Tile createTile(int id, World world, int xTile, int yTile) {
		return createTile(findTemplate(id), world, xTile, yTile);
	}

	public Tile createTile(String name, World world, int xTile, int yTile) {
		return createTile(findTemplate(name), world, xTile, yTile);
	}

	public Tile createTile(Template template, World world, int xTile, int yTile) {
		if (template != null) {
			try {
				Class<? extends Tile> clazz = Tile.Type.valueOf(template.getString("type")).clazz;

				Constructor<? extends Tile> c = clazz.getConstructor(Template.class, World.class, int.class, int.class);

				Tile tile = c.newInstance(template, world, xTile, yTile);

				return tile;
			} catch (Exception e) {
				Gdx.app.error("TileManager", "Failed creating a new Tile!", e);
				return null;
			}
		}
		return null;
	}

	private Template findTemplate(String name) {
		for (Template t : templates) {
			if (t.getString("name").equals(name)) {
				return t;
			}
		}

		return null;
	}

	private Template findTemplate(int id) {
		for (Template t : templates) {
			if (t.getInteger("id") == id) {
				return t;
			}
		}

		return null;
	}

}
