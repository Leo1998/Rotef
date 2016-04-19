package com.rotef.game.world.entity;

import com.rotef.game.world.World;
import com.rotef.game.world.tile.Tile;

public class Player extends Mob {

	private float miningDistance = 3.5f;

	// playerDef.addValue("walkingSpeed", "3.6");
	// playerDef.addValue("life", "100");
	// playerDef.addValue("miningDistance", "3.5");

	public Player(EntityTemplate template, World world) {
		super(template, world);
	}

	@Override
	public void update(float delta) {
		super.update(delta);
	}

	public void mine(float x, float y) {
		int xt = (int) (x * 2);
		int yt = (int) (y * 2);
		Tile tile = getWorld().getTile(xt, yt);

		if (tile != null) {
			float dst = calcDst(x, y, getX(), getY());

			if (dst <= getMiningDistance()) {
				getWorld().setTile(xt, yt, 0);
			}
		}
	}

	private float calcDst(float x0, float y0, float x1, float y1) {
		float kx = x0 - x1;
		float ky = y0 - y1;

		return (float) Math.sqrt(kx * kx + ky * ky);
	}

	public float getMiningDistance() {
		return miningDistance;
	}

	public void setMiningDistance(float miningDistance) {
		this.miningDistance = miningDistance;
	}

}
