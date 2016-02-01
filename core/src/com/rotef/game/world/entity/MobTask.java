package com.rotef.game.world.entity;

public abstract class MobTask {

	protected Mob mob;

	public MobTask(Mob mob) {
		this.mob = mob;
	}

	public abstract void update(float delta);

}
