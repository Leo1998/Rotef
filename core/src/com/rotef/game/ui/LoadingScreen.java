package com.rotef.game.ui;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.rotef.game.Game;
import com.rotef.game.util.LoadingThread;

public class LoadingScreen extends BaseScreen {

	private volatile LoadingThread thread;
	private boolean needsStart = true;

	private ProgressBar progressBar;

	public LoadingScreen(LoadingThread thread) {
		super();

		this.thread = thread;
	}

	@Override
	public void show() {
		super.show();
	}

	private void update() {
		if (needsStart) {
			needsStart = false;

			thread.launch();
		}

		progressBar.setValue(thread.getProgress());

		Object result = thread.getResult();
		if (result != null && result instanceof Screen) {
			Game.game.setScreen((Screen) result);
		}
	}

	@Override
	public void render(float delta) {
		update();

		super.render(delta);
	}

	@Override
	protected void makeContent() {
		Table t = new Table();

		Label label = new Label("Loading", UI.labelStyle);
		label.setFontScale(2.5f);
		t.add(label).center();

		t.row();

		progressBar = new ProgressBar(0f, 1f, 0.01f, false, UI.progressBarStyle);
		progressBar.setValue(0.0f);
		t.add(progressBar).center().padTop(20f);

		rootTable.add(t).center();
	}

}
