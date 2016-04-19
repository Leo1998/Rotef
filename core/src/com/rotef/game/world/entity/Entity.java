package com.rotef.game.world.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.rotef.game.Game;
import com.rotef.game.assets.Sprite;
import com.rotef.game.renderer.WorldRenderer;
import com.rotef.game.world.World;
import com.rotef.game.world.light.Light;
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

	private final Type type;
	private final String name;
	private Sprite sprite;

	private final World world;

	private long entityID = -1;

	private float spawnX;
	private float spawnY;

	private float width;
	private float height;

	private Foot foot = null;
	protected Body body;
	protected PhysicsProperties physicsProperties = new PhysicsProperties();

	private Light attachedLight = null;

	public Entity(EntityTemplate template, World world) {
		this.type = template.getType();
		this.name = template.getName();
		this.sprite = Game.assets.getSprite(template.getSpritePath());

		this.width = sprite.getWidth() / PhysicsManager.PPM;
		this.height = sprite.getHeight() / PhysicsManager.PPM;

		this.world = world;
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

	public void render(WorldRenderer renderer, float x, float y, float w, float h) {
		renderer.renderObject(sprite, x, y, w, h);
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
