package com.rotef.game.world.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Array;
import com.rotef.game.template.Template;
import com.rotef.game.world.World;

public abstract class LivingEntity extends Entity {

	public enum Direction {
		Right(), Left();
	}

	private Foot foot = null;

	private Array<MobTask> tasks = new Array<MobTask>();
	private int life = 10;
	private float walkingSpeed = 3.6f;
	private Direction direction = Direction.Right;

	private float lastJumpTime = 0.0f;

	LivingEntity(Template template, World world) {
		super(template, world);

		this.life = template.map.getInt("life");
		this.walkingSpeed = template.map.getFloat("walkingSpeed");

		// setup some more friction, 80 kg weight and fix rotation
		this.physicsProperties = new PhysicsProperties(80.0f, 0.0f, 0.99f, true, true);
	}

	@Override
	public void update(float delta) {
		super.update(delta);

		if (hasFoot()) {
			for (Fixture f : body.getFixtureList()) {
				f.setFriction(isGrounded() ? physicsProperties.getFriction() : 0f);
			}
		}
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

	public void walk(float val) {
		Vector2 vel = body.getLinearVelocity();

		if (val > 0) {
			direction = Direction.Right;
		} else if (val < 0) {
			direction = Direction.Left;
		}

		body.setLinearVelocity(val * walkingSpeed, vel.y);
	}

	public void jump() {
		float time = getWorld().getWorldTime();

		if (time - lastJumpTime > 0.35f) {
			if (isGrounded()) {
				body.setLinearVelocity(body.getLinearVelocity().x, 0f);

				lastJumpTime = time;

				body.applyForceToCenter(0, body.getMass() * 400, true);
			}
		}
	}

	public void addTask(MobTask task) {
		tasks.add(task);
	}

	public void processTasks(float delta) {
		for (int i = 0; i < tasks.size; i++) {
			MobTask task = tasks.get(i);
			task.update(delta);
		}
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public void loseLife(int life) {
		this.life -= life;
	}

	public void regenLife(int life) {
		this.life += life;
	}

	public float getWalkingSpeed() {
		return walkingSpeed;
	}

	protected void setWalkingSpeed(float walkingSpeed) {
		this.walkingSpeed = walkingSpeed;
	}

	public Direction getDirection() {
		return direction;
	}

}
