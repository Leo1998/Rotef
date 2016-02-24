package com.rotef.game.world.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.rotef.game.assets.Sprite;
import com.rotef.game.world.World;
import com.rotef.game.world.entity.skin.BaseEntitySkin;
import com.rotef.game.world.entity.skin.EntitySkin;
import com.rotef.game.world.light.Light;
import com.rotef.game.world.physics.PhysicsManager;

public abstract class Entity {

	private final int id;
	private final long entityID;
	private final World world;

	private EntitySkin skin;
	/**
	 * The width in meters
	 */
	private float width;
	/**
	 * The height in meters
	 */
	private float height;

	private float spawnX;
	private float spawnY;

	private Foot foot = null;
	protected Body body;
	protected PhysicsProperties physicsProperties = new PhysicsProperties();

	private Light attachedLight = null;

	public Entity(int id, World world) {
		this.id = id;
		this.world = world;
		this.entityID = this.getEntityManager().newUnusedEntityID();
	}

	public void update(float delta) {
		if (hasFoot()) {
			for (Fixture f : body.getFixtureList()) {
				f.setFriction(isGrounded() ? physicsProperties.getFriction() : 0f);
			}
		}

		if (hasAttachedLight()) {
			Light light = getAttachedLight();
			light.setX(this.getX());
			light.setY(this.getY());
		}
	}

	void setSpawnPosition(float x, float y) {
		this.spawnX = x;
		this.spawnY = y;
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

	public int getId() {
		return id;
	}

	public long getEntityID() {
		return entityID;
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	public EntitySkin getSkin() {
		return skin;
	}

	public void setSkin(EntitySkin skin) {
		this.skin = skin;

		this.width = skin.getWidth() / PhysicsManager.PPM;
		this.height = skin.getHeight() / PhysicsManager.PPM;
	}

	protected void createBaseSkin(Sprite sprite) {
		setSkin(new BaseEntitySkin(sprite));
	}

	public boolean isGrounded() {
		return hasFoot() && foot.getNumContacts() > 0;
	}

	public Foot getFoot() {
		return foot;
	}

	public boolean hasFoot() {
		return foot != null;
	}

	public void setFoot(Foot foot) {
		this.foot = foot;
	}

	public Light getAttachedLight() {
		return attachedLight;
	}

	public boolean hasAttachedLight() {
		return getAttachedLight() != null;
	}

	protected void attachLight(Color color, float distance) {
		this.attachedLight = new Light(getSpawnX(), getSpawnY(), distance, color);

		getWorld().getLightManager().addLight(attachedLight);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (entityID ^ (entityID >>> 32));
		result = prime * result + id;
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
		if (id != other.id)
			return false;
		return true;
	}

}
