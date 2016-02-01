package com.rotef.game.world.entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.rotef.game.Game;
import com.rotef.game.world.World;
import com.rotef.game.world.entity.skin.BaseEntitySkin;
import com.rotef.game.world.entity.skin.MobSkin;

public class EntityDef {

	private final Class<? extends Entity> clazz;
	private final int id;
	private final HashMap<String, String> valueMap;

	public EntityDef(Class<? extends Entity> clazz, int id) {
		this.clazz = clazz;
		this.id = id;

		this.valueMap = new HashMap<String, String>();
	}

	public Class<? extends Entity> getClazz() {
		return clazz;
	}

	public int getId() {
		return id;
	}

	public Entity createEntity(int id, World world) {
		try {
			Constructor<? extends Entity> c = clazz.getConstructor(int.class, World.class);

			Entity entity = c.newInstance(id, world);

			if (getValue("tex") != null) {
				entity.setSkin(new BaseEntitySkin(Game.assets.getSprite(getValue("tex"))));
			}

			if (entity instanceof Mob) {
				Mob mob = (Mob) entity;
				if (getValue("skin") != null) {
					entity.setSkin(new MobSkin(Gdx.files.internal(getValue("skin")), mob));

				}
				if (getValue("walkingSpeed") != null) {
					mob.setWalkingSpeed(Float.valueOf(getValue("walkingSpeed")));
				}
				if (getValue("life") != null) {
					mob.setLife(Integer.valueOf(getValue("life")));
				}
			}

			return entity;
		} catch (NoSuchMethodException e) {
			Gdx.app.error("EntityDef", "Every Entity needs to implement the Constructor Entity(int id, World world)!");
			e.printStackTrace();
			return null;
		} catch (SecurityException e) {
			throw new RuntimeException("The Security Level is to low!");
		} catch (InstantiationException e) {
			Gdx.app.error("EntityDef", "InstantiationException!");
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			Gdx.app.error("EntityDef", "IllegalAccessException!");
			e.printStackTrace();
			return null;
		} catch (IllegalArgumentException e) {
			Gdx.app.error("EntityDef", "IllegalArgumentException!");
			e.printStackTrace();
			return null;
		} catch (InvocationTargetException e) {
			Gdx.app.error("EntityDef", "InvocationTargetException!");
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
