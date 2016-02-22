package com.rotef.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SmokeBackgroundRenderer extends BackgroundRenderer {

	private ParticleEffect effect;

	public SmokeBackgroundRenderer() {
		super();

		this.effect = new ParticleEffect();
		this.effect.load(Gdx.files.internal("particles/smoke.p"), Gdx.files.internal("particles"));
	}

	@Override
	public void render(SpriteBatch batch, float delta) {
		effect.draw(batch, delta);
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void dispose() {
		effect.dispose();
	}

}
