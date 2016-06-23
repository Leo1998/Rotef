package com.rotef.game.world.tile;

import java.lang.reflect.Constructor;

import com.badlogic.gdx.Gdx;
import com.rotef.game.template.Template;
import com.rotef.game.template.TemplateManager;
import com.rotef.game.template.TemplateManager.Type;
import com.rotef.game.world.Layer;
import com.rotef.game.world.World;

public class TileManager {

	public TileManager() {
	}

	public Tile createTile(int id, World world, Layer layer, int xTile, int yTile) {
		return createTile(findTemplateById(id), world, layer, xTile, yTile);
	}

	public Tile createTile(String name, World world, Layer layer, int xTile, int yTile) {
		return createTile(TemplateManager.findByName(Type.Tile, name), world, layer, xTile, yTile);
	}

	public Tile createTile(Template template, World world, Layer layer, int xTile, int yTile) {
		if (template != null) {
			try {
				Class<? extends Tile> clazz = Tile.Type.valueOf(template.getString("type")).clazz;

				Constructor<? extends Tile> c = clazz.getConstructor(Template.class, World.class, Layer.class, int.class, int.class);

				Tile tile = c.newInstance(template, world, layer, xTile, yTile);

				return tile;
			} catch (Exception e) {
				Gdx.app.error("TileManager", "Failed creating a new Tile!", e);
				return null;
			}
		}
		return null;
	}

	private Template findTemplateById(int id) {
		for (Template t : TemplateManager.getTemplates(Type.Tile)) {
			if (t.getInteger("id") == id) {
				return t;
			}
		}

		return null;
	}

}
