package com.rotef.game.world.physics;

import java.util.HashMap;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.rotef.game.world.World;
import com.rotef.game.world.entity.Entity;
import com.rotef.game.world.entity.Foot;
import com.rotef.game.world.entity.LivingEntity;
import com.rotef.game.world.entity.PhysicsProperties;

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

	private HashMap<Entity, Body> entityBodies = new HashMap<Entity, Body>();

	private World world;

	private float accumulator = 0.0f;

	public PhysicsManager(World world) {
		this.world = world;

		this.physicsWorld = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, -9.81f), true);
		this.physicsWorld.setContactListener(this);
	}

	public void renderDebug(OrthographicCamera camera) {
		if (debugRenderer == null) {
			debugRenderer = new Box2DDebugRenderer();
		}

		Matrix4 debugMatrix = new Matrix4(camera.combined);

		debugMatrix.scale(PPM, PPM, 1f);

		debugRenderer.render(physicsWorld, debugMatrix);
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

		float squareMeters = entity.getWidth() * entity.getHeight();
		float density = props.getWeight() / squareMeters;

		{
			Shape shape = entity.createShape();

			FixtureDef fDef = new FixtureDef();
			fDef.shape = shape;
			fDef.density = density;
			fDef.restitution = props.getRestitution();
			fDef.friction = props.getFriction();
			fDef.isSensor = false;

			entityBody.createFixture(fDef).setUserData(entity);

			shape.dispose();
		}

		if (props.isRequireFootSensor() && entity instanceof LivingEntity) {
			PolygonShape shape = new PolygonShape();
			shape.setAsBox(entity.getWidth() / 3, 0.15f, new Vector2(0, -entity.getHeight() / 2), 0);

			LivingEntity livingEntity = (LivingEntity) entity;
			Foot foot = new Foot(livingEntity);
			livingEntity.setFoot(foot);

			FixtureDef fDef = new FixtureDef();
			fDef.shape = shape;
			fDef.density = 0f;
			fDef.restitution = props.getRestitution();
			fDef.friction = props.getFriction();
			fDef.isSensor = true;

			entityBody.createFixture(fDef).setUserData(foot);

			shape.dispose();
		}

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
		entityBodies.clear();
		physicsWorld.dispose();

		if (debugRenderer != null) {
			debugRenderer.dispose();
		}
	}

	@Override
	public void beginContact(Contact c) {
		Fixture fa = c.getFixtureA();
		Fixture fb = c.getFixtureB();

		{
			Object o = fa.getUserData();
			if (o != null) {
				if ("tile".equals(o)) {
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
				if ("tile".equals(o)) {
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
				if ("tile".equals(o)) {
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
				if ("tile".equals(o)) {
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

	public com.badlogic.gdx.physics.box2d.World getPhysicsWorld() {
		return physicsWorld;
	}

}
