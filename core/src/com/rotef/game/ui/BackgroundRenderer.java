package com.rotef.game.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class BackgroundRenderer {

	public BackgroundRenderer() {
	}

	public abstract void render(SpriteBatch batch, float delta);

	public abstract void resize(int width, int height);

	public abstract void dispose();

}
