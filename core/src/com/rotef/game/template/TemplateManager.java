package com.rotef.game.template;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.rotef.game.Game;

public class TemplateManager {

	public enum Type {
		Tile, Entity;
	}

	private static final Array<Template> tiles = new Array<Template>();
	private static final Array<Template> entities = new Array<Template>();

	public static void load() {
		Gdx.app.log("TemplateManager", "Loading Templates...");

		loadTemplate("common/tiles/dirt.tile", Type.Tile);
		loadTemplate("common/tiles/water.tile", Type.Tile);
		loadTemplate("common/tiles/stone.tile", Type.Tile);
		loadTemplate("common/tiles/lava.tile", Type.Tile);
		loadTemplate("common/tiles/torch.tile", Type.Tile);

		loadTemplate("common/entity/player.entity", Type.Entity);
	}

	public static void preloadSprites() {
		Gdx.app.log("TemplateManager", "Preloading Sprites...");

		for (Template t : getTemplates(Type.Tile)) {
			String spritePath = t.map.getString("spritePath");
			if (spritePath != null && !spritePath.isEmpty()) {
				Game.assets.loadSprite(Gdx.files.internal(spritePath));
			}
		}
		for (Template t : getTemplates(Type.Entity)) {
			String spritePath = t.map.getString("spritePath");
			if (spritePath != null && !spritePath.isEmpty()) {
				Game.assets.loadSprite(Gdx.files.internal(spritePath));
			}
		}
	}

	private static void loadTemplate(String path, Type type) {
		loadTemplate(Gdx.files.internal(path), type);
	}

	private static void loadTemplate(FileHandle file, Type type) {
		Template template = new Template(file);

		Gdx.app.log("TemplateManager", "Loaded Template (" + file.path() + ", " + type.name() + ")");

		Array<Template> list = getTemplates(type);
		list.add(template);
	}

	public static Array<Template> getTemplates(Type type) {
		if (type == Type.Tile) {
			return tiles;
		} else if (type == Type.Entity) {
			return entities;
		}

		return null;
	}

	public static Template findByName(Type type, String name) {
		Array<Template> list = getTemplates(type);

		for (Template t : list) {
			if (t.map.getString("name").equals(name)) {
				return t;
			}
		}

		return null;
	}

}
