package com.rotef.game.world.entity;

import com.badlogic.gdx.graphics.Color;
import com.rotef.game.world.World;

public class Player extends Mob {

	private String name;

	public Player(int id, World world) {
		super(id, world);

		this.attachLight(Color.WHITE, 5);
	}

	@Override
	public void update(float delta) {
		super.update(delta);
	}

	public String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}

}
