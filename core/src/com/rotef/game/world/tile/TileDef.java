package com.rotef.game.world.tile;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.rotef.game.Game;
import com.rotef.game.world.World;
import com.rotef.game.world.tile.textures.DynamicTileSprite;
import com.rotef.game.world.tile.textures.SimpleTileSprite;

public class TileDef {

	private final Class<? extends Tile> clazz;
	private final int id;
	private final HashMap<String, String> valueMap;

	public TileDef(Class<? extends Tile> clazz, int id) {
		this.clazz = clazz;
		this.id = id;

		this.valueMap = new HashMap<String, String>();
	}

	public Class<? extends Tile> getClazz() {
		return clazz;
	}

	public int getId() {
		return id;
	}

	public Tile createTile(int id, World world, int xTile, int yTile) {
		try {
			Constructor<? extends Tile> c = clazz.getConstructor(int.class, World.class, int.class, int.class);

			Tile tile = c.newInstance(id, world, xTile, yTile);

			if (getValue("tex") != null) {
				tile.setSprite(new SimpleTileSprite(tile, Game.assets.getSprite(getValue("tex"))));
			} else if (getValue("sprite") != null) {
				tile.setSprite(new DynamicTileSprite(tile, Game.assets.getSprite(getValue("sprite"))));
			}

			return tile;
		} catch (NoSuchMethodException e) {
			Gdx.app.error("TileDef",
					"Every Tile needs to implement the Constructor Tile(int id, World world, int xTile, int yTile)!");
			e.printStackTrace();
			return null;
		} catch (SecurityException e) {
			throw new RuntimeException("The Security Level is to low!");
		} catch (InstantiationException e) {
			Gdx.app.error("TileDef", "InstantiationException!");
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			Gdx.app.error("TileDef", "IllegalAccessException!");
			e.printStackTrace();
			return null;
		} catch (IllegalArgumentException e) {
			Gdx.app.error("TileDef", "IllegalArgumentException!");
			e.printStackTrace();
			return null;
		} catch (InvocationTargetException e) {
			Gdx.app.error("TileDef", "InvocationTargetException!");
			e.printStackTrace();
			return null;
		}
	}

	public String getValue(String key) {
		return valueMap.get(key);
	}

	public void addValue(String key, String value) {
		valueMap.put(key, value);
	}

}
