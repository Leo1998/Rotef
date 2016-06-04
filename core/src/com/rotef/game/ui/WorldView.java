package com.rotef.game.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public abstract class WorldView {

	protected Table rootTable;
	private int width;
	private int height;

	public WorldView(int width, int height) {
		this.width = width;
		this.height = height;
		this.rootTable = new Table();

	}

	public abstract void makeContent(Stage stage);

	public Table getRootTable() {
		return rootTable;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

}
