package com.rotef.game.util.noise;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class NoiseDebugScreen implements Screen {

	private OrthographicCamera uiCamera;
	private SpriteBatch uiBatch;

	private Pixmap pixmap;
	private Texture tex;

	@Override
	public void show() {
		this.uiBatch = new SpriteBatch();
		this.uiCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	@Override
	public void render(float delta) {
		if (pixmap == null || Gdx.input.isKeyJustPressed(Keys.SPACE)) {
			pixmap = new Pixmap(256, 256, Format.RGBA8888);
			pixmap.setColor(Color.BLUE);

			PerlinNoiseGenerator generator = new PerlinNoiseGenerator(new Random());

			for (double x = 0; x < 256; x++) {
				double value = generator.noise(x / 64, 6, 1 / 2, true);

				pixmap.drawLine((int) x, 128, (int) x, (int) (value * 128 + 128));
			}

			tex = new Texture(pixmap);
		}

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		uiCamera.update();
		uiBatch.setProjectionMatrix(uiCamera.combined);
		uiBatch.begin();

		uiBatch.draw(tex, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		uiBatch.end();
	}

	@Override
	public void resize(int width, int height) {
		uiCamera.setToOrtho(false, width, height);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		this.uiBatch.dispose();
	}

}
