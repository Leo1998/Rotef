package com.rotef.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.rotef.game.Game;

public abstract class BaseScreen implements Screen {

	protected Stage ui;
	protected SpriteBatch batch;
	protected Table rootTable;
	protected int width;
	protected int height;
	protected float uiScale = 1.0f;

	protected BackgroundRenderer background;

	@Override
	public void show() {
		this.width = Gdx.graphics.getWidth();
		this.height = Gdx.graphics.getHeight();
		this.updateUiScale();

		batch = new SpriteBatch();

		ui = new Stage(new ScalingViewport(Scaling.stretch, width / uiScale, height / uiScale, new OrthographicCamera()), batch);
		ui.setDebugAll(Game.config.isDebug());
		Gdx.input.setInputProcessor(ui);

		rootTable = new Table();
		rootTable.setFillParent(true);
		ui.addActor(rootTable);

		rootTable.addListener(new AnimatedListener());
		makeContent();
	}

	protected abstract void makeContent();

	protected void renderWorld(float delta) {
	}

	protected void renderAdditionalUI(float delta) {
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		ui.act(Gdx.graphics.getDeltaTime());

		if (background != null) {
			batch.setProjectionMatrix(ui.getCamera().combined);
			batch.begin();

			background.render(batch, delta);

			batch.end();
		}

		renderWorld(delta);

		ui.draw();

		batch.setProjectionMatrix(ui.getCamera().combined);
		batch.begin();
		renderAdditionalUI(delta);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		this.width = width;
		this.height = height;

		if (background != null) {
			background.resize(width, height);
		}

		updateUiScale();

		ui.getViewport().setWorldSize(width / uiScale, height / uiScale);
		ui.getViewport().update(width, height, true);
	}

	private void updateUiScale() {
		this.uiScale = Game.config.getUiSize();
	}

	@Override
	public void hide() {
		ui.dispose();
		batch.dispose();

		if (background != null) {
			background.dispose();
		}
	}

	@Override
	public void dispose() {
	}

	@Override
	public void pause() {
		Game.autoSave();
	}

	@Override
	public void resume() {
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public BackgroundRenderer getBackground() {
		return background;
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	public float getUiScale() {
		return uiScale;
	}
}
