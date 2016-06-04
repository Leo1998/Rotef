package com.rotef.game.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

public abstract class WorldView {

	protected Table rootTable;
	protected int width;
	protected int height;

	public WorldView(int width, int height) {
		this.width = width;
		this.height = height;
		this.rootTable = new Table();

		makeContent();
	}

	protected abstract void makeContent();

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
