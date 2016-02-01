package com.rotef.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public abstract class BackgroundRenderer {

	private OrthographicCamera camera;
	private TextureRegion region;
	private ShaderProgram shader;

	private SpriteBatch batch;

	private float time = 0.0f;

	public BackgroundRenderer(ShaderProgram shader, TextureRegion region) {
		this.shader = shader;
		this.region = region;
		this.camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.batch = new SpriteBatch();

		Gdx.app.log("BackgroundRenderer", "" + shader.getLog());
	}

	public void render(float delta) {
		this.time += delta;

		int w = Gdx.graphics.getWidth();
		int h = Gdx.graphics.getHeight();

		camera.update();

		batch.setShader(shader);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		shader.setUniformf("u_time", this.time);
		batch.draw(region, 0, 0, w, h);

		batch.end();
	}

	public void resize(int width, int height) {
		camera.setToOrtho(false, width, height);
	}

	public void dispose() {
		shader.dispose();
	}

}
