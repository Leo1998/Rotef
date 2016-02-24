package com.rotef.game.world.entity;

import com.rotef.game.input.WorldInput;

public class PlayerController extends MobTask {

	public PlayerController(Mob mob) {
		super(mob);
	}

	@Override
	public void update(float delta) {
		if (!(mob instanceof Player)) {
			return;
		}

		Player player = (Player) mob;

		WorldInput input = player.getWorld().getScreen().getInput();

		input.handlePlayer(player, delta);
	}

}
