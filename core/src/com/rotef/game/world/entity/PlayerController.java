package com.rotef.game.world.entity;

import com.rotef.game.input.WorldInput;

public class PlayerController extends MobTask {

	public PlayerController(Mob mob) {
		super(mob);
	}

	@Override
	public void update(float delta) {
		WorldInput input = mob.getWorld().getRenderer().getInput();

		input.movePlayer(mob, delta);
	}

}
