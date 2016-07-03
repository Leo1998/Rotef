package com.rotef.game.world.entity;

import com.rotef.game.Game;
import com.rotef.game.assets.Sprite;
import com.rotef.game.renderer.WorldRenderer;
import com.rotef.game.template.Template;
import com.rotef.game.world.Layer;
import com.rotef.game.world.World;
import com.rotef.game.world.tile.Tile;

public class Player extends LivingEntity {

	private float miningDistance = 3.5f;

	private Sprite sprite;

	public Player(Template template, World world) {
		super(template, world);

		this.sprite = Game.assets.getSprite(template.map.getString("spritePath"));
	}

	@Override
	public void render(WorldRenderer renderer, float x, float y, float w, float h) {
		if (this.getDirection() == Direction.Left) {
			x += w;
			w = -w;
		}

		renderer.renderObject(sprite, x, y, w, h);
	}

	@Override
	public void update(float delta) {
		super.update(delta);
	}

	public void mine(float x, float y) {
		Layer layer = Layer.Foreground;

		int xt = (int) (x * 2);
		int yt = (int) (y * 2);
		Tile tile = getWorld().getTile(layer, xt, yt);

		if (tile != null) {
			float dst = calcDst(x, y, getX(), getY());

			if (dst <= getMiningDistance()) {
				getWorld().setTile(layer, xt, yt, 0);
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
