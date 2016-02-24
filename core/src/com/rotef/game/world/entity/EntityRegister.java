package com.rotef.game.world.entity;

import java.util.HashMap;
import java.util.Map;

import com.rotef.game.world.World;

public class EntityRegister {

	private Map<Integer, EntityDef> entityDefs = new HashMap<Integer, EntityDef>();

	public EntityRegister() {
		{
			EntityDef playerDef = new EntityDef(Player.class, 1);
			playerDef.addValue("skin", "mob/player-skin.dat");
			playerDef.addValue("walkingSpeed", "3.6");
			playerDef.addValue("life", "100");
			playerDef.addValue("miningDistance", "3.5");

			entityDefs.put(playerDef.getId(), playerDef);
		}
	}

	public Entity createEntity(int id, World world) {
		if (!entityDefs.containsKey(id))
			return null;

		EntityDef def = entityDefs.get(id);

		return def.createEntity(id, world);
	}

}
