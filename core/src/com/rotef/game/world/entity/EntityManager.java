package com.rotef.game.world.entity;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.rotef.game.template.Template;
import com.rotef.game.template.TemplateManager;
import com.rotef.game.template.TemplateManager.Type;
import com.rotef.game.world.World;
import com.rotef.game.world.physics.PhysicsManager;

public class EntityManager {

	private HashMap<Long, Entity> entities = new HashMap<Long, Entity>();
	private Array<Long> givenEntityIDs = new Array<Long>();

	private World world;

	public EntityManager(World world) {
		this.world = world;
	}

	public Entity spawnEntity(String name, float x, float y) {
		Template template = TemplateManager.findByName(Type.Entity, name);

		Entity entity = createEntity(template, world);
		if (entity == null)
			return null;

		long entityID = newUnusedEntityID();
		entity.setEntityID(entityID);

		entity.setSpawnPosition(x, y);

		entities.put(entityID, entity);
		givenEntityIDs.add(entityID);

		PhysicsManager physicsManager = world.getPhysicsManager();
		physicsManager.initializeEntity(entity);

		return entity;
	}

	private Entity createEntity(Template template, World world) {
		if (template != null) {
			try {
				Class<? extends Entity> clazz = Entity.Type.valueOf(template.map.getString("type")).clazz;

				Constructor<? extends Entity> c = clazz.getConstructor(Template.class, World.class);

				Entity entity = c.newInstance(template, world);

				return entity;
			} catch (Exception e) {
				Gdx.app.error("EntityManager", "Failed creating a new Entity!", e);
				return null;
			}
		}

		return null;
	}

	public void removeEntity(long entityID) {
		removeEntity(this.getEntity(entityID));
	}

	public void removeEntity(Entity entity) {
		entities.remove(entity);
		givenEntityIDs.removeValue(entity.getEntityID(), true);

		PhysicsManager physicsManager = world.getPhysicsManager();
		physicsManager.removeEntity(entity);
	}

	public Collection<Entity> getEntities() {
		return Collections.unmodifiableCollection(entities.values());
	}

	public Entity getEntity(long entityID) {
		return entities.get(entityID);
	}

	public long newUnusedEntityID() {
		long i = 1;
		while (true) {
			if (!givenEntityIDs.contains(i, true)) {
				return i;
			}
			i++;
		}
	}

	public void update(float delta) {
		for (Entity e : entities.values()) {
			e.update(delta);
			if (e instanceof LivingEntity) {
				LivingEntity mob = (LivingEntity) e;
				mob.processTasks(delta);
			}
		}
	}

	public World getWorld() {
		return world;
	}

}
