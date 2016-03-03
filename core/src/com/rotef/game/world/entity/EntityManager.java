package com.rotef.game.world.entity;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import com.badlogic.gdx.utils.Array;
import com.rotef.game.world.World;
import com.rotef.game.world.physics.PhysicsManager;

public class EntityManager {

	private HashMap<Long, Entity> entities = new HashMap<Long, Entity>();
	private Array<Long> givenEntityIDs = new Array<Long>();

	private World world;
	private EntityRegister register;

	public EntityManager(World world) {
		this.world = world;

		this.register = new EntityRegister();
	}

	public Entity spawnEntity(int id, float x, float y) {
		Entity entity = register.createEntity(id, world);
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
			if (e instanceof Mob) {
				Mob mob = (Mob) e;
				mob.processTasks(delta);
			}
		}
	}

	public World getWorld() {
		return world;
	}

}
