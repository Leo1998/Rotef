package com.rotef.game.world.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.rotef.game.renderer.WorldRenderer;
import com.rotef.game.template.Template;
import com.rotef.game.world.World;
import com.rotef.game.world.physics.PhysicsManager;

public abstract class Entity {

	public enum Type {

		Player(Player.class);

		//

		final Class<? extends Entity> clazz;

		Type(Class<? extends Entity> clazz) {
			this.clazz = clazz;
		}
	}

	private Template template;
	private final Type type;
	private final String name;

	private final World world;

	private long entityID = -1;

	private float spawnX;
	private float spawnY;

	private float width;
	private float height;

	protected Body body;
	protected PhysicsProperties physicsProperties = new PhysicsProperties();

	Entity(Template template, World world) {
		this.template = template;
		this.type = Entity.Type.valueOf(template.map.getString("type"));
		this.name = template.map.getString("name");

		this.width = template.map.getFloat("width");
		this.height = template.map.getFloat("height");

		this.world = world;
	}

	public void update(float delta) {
	}

	public abstract void render(WorldRenderer renderer, float x, float y, float w, float h);

	void setSpawnPosition(float x, float y) {
		this.spawnX = x;
		this.spawnY = y;
	}

	public Shape createShape() {
		PolygonShape shape = new PolygonShape();

		shape.setAsBox(width / 2, height / 2);

		return shape;
	}

	public Vector2 getPosition() {
		return body.getPosition();
	}

	public float getX() {
		return getPosition().x;
	}

	public float getY() {
		return getPosition().y;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public float getSpawnX() {
		return spawnX;
	}

	public float getSpawnY() {
		return spawnY;
	}

	public Template getTemplate() {
		return template;
	}

	public Type getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public World getWorld() {
		return world;
	}

	public EntityManager getEntityManager() {
		return getWorld().getEntityManager();
	}

	public PhysicsManager getPhysicsManager() {
		return getWorld().getPhysicsManager();
	}

	public PhysicsProperties getPhysicsProperties() {
		return physicsProperties;
	}

	public long getEntityID() {
		return entityID;
	}

	public void setEntityID(long entityID) {
		this.entityID = entityID;
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (entityID ^ (entityID >>> 32));
		result = prime * result + name.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Entity other = (Entity) obj;
		if (entityID != other.entityID)
			return false;
		if (!name.equals(other.name))
			return false;
		return true;
	}

}
