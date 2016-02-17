package com.rotef.game.world.entity;

public class PhysicsProperties {

	private float weight;
	private float restitution;
	private float friction;

	private boolean fixedRotation = false;
	private boolean requireFootSensor = false;

	public PhysicsProperties() {
		this(0.0f, 0.0f, 0.0f, false, false);
	}

	public PhysicsProperties(float weight, float restitution, float friction, boolean fixedRotation,
			boolean requireFootSensor) {
		super();
		this.weight = weight;
		this.restitution = restitution;
		this.friction = friction;
		this.fixedRotation = fixedRotation;
		this.requireFootSensor = requireFootSensor;
	}

	public float getWeight() {
		return weight;
	}

	public float getRestitution() {
		return restitution;
	}

	public float getFriction() {
		return friction;
	}

	public boolean isFixedRotation() {
		return fixedRotation;
	}

	public boolean isRequireFootSensor() {
		return requireFootSensor;
	}

}
