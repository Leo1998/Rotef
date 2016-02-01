package com.rotef.game.world.entity.skin;

import com.rotef.game.assets.Sprite;

public class BaseEntitySkin implements EntitySkin {

	private Sprite sprite;

	public BaseEntitySkin(Sprite sprite) {
		this.sprite = sprite;
	}

	@Override
	public Sprite getSprite() {
		return sprite;
	}

	@Override
	public int getWidth() {
		return sprite.getWidth();
	}

	@Override
	public int getHeight() {
		return sprite.getHeight();
	}

	@Override
	public boolean mirrorX() {
		return false;
	}

	@Override
	public boolean mirrorY() {
		return false;
	}

}
