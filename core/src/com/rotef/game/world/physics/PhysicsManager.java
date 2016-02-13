package com.rotef.game.world.physics;

import java.util.HashMap;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.rotef.game.world.World;
import com.rotef.game.world.WorldChunk;
import com.rotef.game.world.entity.Entity;
import com.rotef.game.world.entity.Foot;
import com.rotef.game.world.entity.PhysicsProperties;
import com.rotef.game.world.tile.Tile;

public class PhysicsManager implements ContactListener {

	/**
	 * Pixel Per Meters
	 */
	public static final int PPM = 64;

	private static final float TIME_STEP = 1 / 60f;
	private static final int VELOCITY_ITERATIONS = 6;
	private static final int POSITION_ITERATIONS = 2;

	static {
		Box2D.init();
	}

	private com.badlogic.gdx.physics.box2d.World physicsWorld;

	private Box2DDebugRenderer debugRenderer;

	private HashMap<Tile, Body> tileBodies = new HashMap<Tile, Body>();
	private HashMap<Entity, Body> entityBodies = new HashMap<Entity, Body>();

	private World world;

	private float accumulator = 0.0f;

	public PhysicsManager(World world) {
		this.world = world;
	}

	public void initialize() {
		this.physicsWorld = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, -9.81f), true);

		this.physicsWorld.setContactListener(this);
	}

	public void renderDebug(OrthographicCamera camera) {
		if (debugRenderer == null) {
			debugRenderer = new Box2DDebugRenderer();
		}

		debugRenderer.render(physicsWorld, camera.combined);
	}

	public void updatePhysics(float delta) {
		doPhysicsStep(delta);
	}

	private void doPhysicsStep(float delta) {
		// fixed time step
		// max frame time to avoid spiral of death (on slow devices)
		float frameTime = Math.min(delta, 0.25f);
		accumulator += frameTime;
		while (accumulator >= TIME_STEP) {
			physicsWorld.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
			accumulator -= TIME_STEP;
		}
	}

	public void onChunkLoaded(WorldChunk chunk) {
		for (int x = 0; x < WorldChunk.CHUNK_SIZE; x++) {
			for (int y = 0; y < WorldChunk.CHUNK_SIZE; y++) {
				Tile tile = chunk.getTile(x, y);

				if (tile != null && tile.isSolid()) {
					createTileBody(tile);
				}
			}
		}
	}

	public void onChunkUnloaded(WorldChunk chunk) {
		for (int x = 0; x < WorldChunk.CHUNK_SIZE; x++) {
			for (int y = 0; y < WorldChunk.CHUNK_SIZE; y++) {
				Tile tile = chunk.getTile(x, y);

				if (tile != null && tile.isSolid()) {
					removeTileBody(tile);
				}
			}
		}
	}

	public void onTileUpdate(WorldChunk chunk, Tile oldTile, Tile newTile, int xTile, int yTile) {
		if (oldTile.isSolid() && !newTile.isSolid()) {
			removeTileBody(oldTile);
		}

		if (!oldTile.isSolid() && newTile.isSolid()) {
			createTileBody(newTile);
		}
	}

	private Body createTileBody(Tile tile) {
		if (tileBodies.containsKey(tile)) {
			return tileBodies.get(tile);
		}

		BodyDef tileBodyDef = new BodyDef();
		tileBodyDef.position.set(new Vector2((tile.getXTile() / 2f) + 0.25F, (tile.getYTile() / 2f) + 0.25F));
		tileBodyDef.type = BodyType.StaticBody;

		Body tileBody = physicsWorld.createBody(tileBodyDef);

		ChainShape shape = new ChainShape();
		Vector2[] v = new Vector2[3];
		v[0] = new Vector2(-0.25f, -0.25f);
		v[1] = new Vector2(-0.25f, 0.25f);
		v[2] = new Vector2(0.25f, 0.25f);
		shape.createChain(v);

		FixtureDef fDef = new FixtureDef();
		fDef.shape = shape;
		fDef.density = 0.0f;

		tileBody.createFixture(fDef).setUserData(tile);

		shape.dispose();

		tileBodies.put(tile, tileBody);

		return tileBody;
	}

	private void removeTileBody(Tile tile) {
		if (!tileBodies.containsKey(tile)) {
			return;
		}

		Body body = tileBodies.get(tile);

		physicsWorld.destroyBody(body);

		tileBodies.remove(tile);
	}

	public void initializeEntity(Entity e) {
		Body body = createEntityBody(e);

		e.setBody(body);
	}

	public void removeEntity(Entity e) {
		removeEntityBody(e);

		e.setBody(null);
	}

	private Body createEntityBody(Entity entity) {
		if (entityBodies.containsKey(entity)) {
			return entityBodies.get(entity);
		}

		PhysicsProperties props = entity.getPhysicsProperties();

		BodyDef entityBodyDef = new BodyDef();
		entityBodyDef.position.set(new Vector2(entity.getSpawnX(), entity.getSpawnY()));
		entityBodyDef.type = BodyType.DynamicBody;
		entityBodyDef.fixedRotation = props.isFixedRotation();

		Body entityBody = physicsWorld.createBody(entityBodyDef);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(entity.getWidth() / 2, entity.getHeight() / 2);

		float squareMeters = entity.getWidth() * entity.getHeight();
		float density = props.getWeight() / squareMeters;

		{
			FixtureDef fDef = new FixtureDef();
			fDef.shape = shape;
			fDef.density = density;
			fDef.restitution = props.getRestitution();
			fDef.friction = props.getFriction();
			fDef.isSensor = false;

			entityBody.createFixture(fDef).setUserData(entity);
		}

		if (props.isRequireFootSensor()) {
			shape.setAsBox(entity.getWidth() / 3, 0.15f, new Vector2(0, -entity.getHeight() / 2), 0);

			Foot foot = new Foot(entity);
			entity.setFoot(foot);

			FixtureDef fDef = new FixtureDef();
			fDef.shape = shape;
			fDef.density = 0f;
			fDef.restitution = props.getRestitution();
			fDef.friction = props.getFriction();
			fDef.isSensor = true;

			entityBody.createFixture(fDef).setUserData(foot);
		}

		shape.dispose();

		entityBodies.put(entity, entityBody);

		return entityBody;
	}

	private void removeEntityBody(Entity e) {
		if (!entityBodies.containsKey(e)) {
			return;
		}

		Body body = entityBodies.get(e);

		physicsWorld.destroyBody(body);

		entityBodies.remove(e);
	}

	public void dispose() {
		tileBodies.clear();
		entityBodies.clear();
		physicsWorld.dispose();

		if (debugRenderer != null) {
			debugRenderer.dispose();
		}
	}

	public int getBodyCount() {
		return tileBodies.size() + entityBodies.size();
	}

	@Override
	public void beginContact(Contact c) {
		Fixture fa = c.getFixtureA();
		Fixture fb = c.getFixtureB();

		{
			Object o = fa.getUserData();
			if (o != null) {
				if (o instanceof Tile) {
					Tile tile = (Tile) o;

				} else if (o instanceof Entity) {
					Entity entity = (Entity) o;

				} else if (o instanceof Foot) {
					Foot foot = (Foot) o;

					foot.increaseNumContacts();
				}
			}
		}

		{
			Object o = fb.getUserData();
			if (o != null) {
				if (o instanceof Tile) {
					Tile tile = (Tile) o;

				} else if (o instanceof Entity) {
					Entity entity = (Entity) o;

				} else if (o instanceof Foot) {
					Foot foot = (Foot) o;

					foot.increaseNumContacts();
				}
			}
		}
	}

	@Override
	public void endContact(Contact c) {
		Fixture fa = c.getFixtureA();
		Fixture fb = c.getFixtureB();
		{
			Object o = fa.getUserData();
			if (o != null) {
				if (o instanceof Tile) {
					Tile tile = (Tile) o;

				} else if (o instanceof Entity) {
					Entity entity = (Entity) o;

				} else if (o instanceof Foot) {
					Foot foot = (Foot) o;

					foot.decreaseNumContacts();
				}
			}
		}

		{
			Object o = fb.getUserData();
			if (o != null) {
				if (o instanceof Tile) {
					Tile tile = (Tile) o;

				} else if (o instanceof Entity) {
					Entity entity = (Entity) o;

				} else if (o instanceof Foot) {
					Foot foot = (Foot) o;

					foot.decreaseNumContacts();
				}
			}
		}
	}

	@Override
	public void preSolve(Contact c, Manifold oldManifold) {
	}

	@Override
	public void postSolve(Contact c, ContactImpulse impulse) {
	}

}
