package com.rotef.game.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class LoadingScreen extends BaseScreen {

	private ProgressBar progressBar;
	private Label msgLabel;

	@Override
	public void show() {
		super.show();
	}

	@Override
	public void render(float delta) {
		super.render(delta);
	}

	public void sendStatus(String msg, float progress) {
		progressBar.setValue(progress);
		msgLabel.setText(msg);
	}

	@Override
	protected void makeContent() {
		Table t = new Table();

		Label label = new Label("Loading", UI.labelStyle);
		label.setFontScale(2.5f);
		t.add(label).center();

		t.row();

		msgLabel = new Label("", UI.labelStyle);
		msgLabel.setFontScale(1.25f);
		t.add(msgLabel).center();

		t.row();

		progressBar = new ProgressBar(0f, 1f, 0.01f, false, UI.progressBarStyle);
		progressBar.setValue(0.0f);
		t.add(progressBar).center().padTop(20f);

		rootTable.add(t).center();
	}

}
