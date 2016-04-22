package com.rotef.game.world.entity;

public abstract class MobTask {

	protected LivingEntity mob;

	public MobTask(LivingEntity mob) {
		this.mob = mob;
	}

	public abstract void update(float delta);

}
