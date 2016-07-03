package com.rotef.game.world.physics;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.rotef.game.world.WorldChunk;

public class PhysicsSpatial {

	private Array<Body> bodies = new Array<Body>();
	private WorldChunk chunk;

	public PhysicsSpatial(WorldChunk chunk) {
		this.chunk = chunk;

		init();
	}

	private void init() {
		PhysicsManager physicsManager = chunk.getWorld().getPhysicsManager();

		PartitioningAlgorithm algo = new VerticalPartioningAlgorithm();

		Array<Rectangle> rects = algo.partition(chunk);

		for (Rectangle r : rects) {
			createTileBody(r, physicsManager.getPhysicsWorld());
		}
	}

	public void update() {
		dispose();

		init();
	}

	private Body createTileBody(Rectangle r, com.badlogic.gdx.physics.box2d.World physicsWorld) {
		float xp = (((chunk.getChunkX() * WorldChunk.CHUNK_SIZE)) + r.getX() + (r.getWidth() / 2)) / 2;
		float yp = (((chunk.getChunkY() * WorldChunk.CHUNK_SIZE)) + r.getY() + (r.getHeight() / 2)) / 2;

		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(new Vector2(xp, yp));
		bodyDef.type = BodyType.StaticBody;

		Body body = physicsWorld.createBody(bodyDef);

		float w2 = r.getWidth() / 2 / 2;
		float h2 = r.getHeight() / 2 / 2;
		ChainShape shape = new ChainShape();
		Vector2[] v = new Vector2[5];
		v[0] = new Vector2(-w2, -h2);
		v[1] = new Vector2(-w2, h2);
		v[2] = new Vector2(w2, h2);
		v[3] = new Vector2(w2, -h2);
		v[4] = new Vector2(-w2, -h2);
		shape.createChain(v);

		FixtureDef fDef = new FixtureDef();
		fDef.shape = shape;
		fDef.density = 0.0f;

		body.createFixture(fDef).setUserData("tile");

		shape.dispose();

		bodies.add(body);

		return body;
	}

	public Array<Body> getBodies() {
		return bodies;
	}

	public WorldChunk getChunk() {
		return chunk;
	}

	public void dispose() {
		PhysicsManager physicsManager = chunk.getWorld().getPhysicsManager();

		for (Body b : bodies) {
			physicsManager.getPhysicsWorld().destroyBody(b);
		}

		bodies.clear();
	}

}
