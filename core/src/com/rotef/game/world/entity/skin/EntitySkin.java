package com.rotef.game.world.entity.skin;

import com.rotef.game.assets.Sprite;

public interface EntitySkin {

	public Sprite getSprite();

	public int getWidth();

	public int getHeight();

	public boolean mirrorX();

	public boolean mirrorY();

}
