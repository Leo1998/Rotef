package com.rotef.game.world.entity;

import com.rotef.game.world.World;
import com.rotef.game.world.tile.Tile;

public class Player extends Mob {

	private String name;
	private float miningDistance;

	public Player(int id, World world) {
		super(id, world);
	}

	@Override
	public void update(float delta) {
		super.update(delta);
	}

	public void mine(float x, float y) {
		int xt = (int) (x * 2);
		int yt = (int) (y * 2);
		Tile tile = getWorld().getTile(xt, yt, false);

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

	public String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}

	public float getMiningDistance() {
		return miningDistance;
	}

	public void setMiningDistance(float miningDistance) {
		this.miningDistance = miningDistance;
	}

}
