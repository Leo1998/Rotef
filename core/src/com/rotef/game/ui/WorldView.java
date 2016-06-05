package com.rotef.game.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

public abstract class WorldView {

	private WorldScreen screen;
	private int width;
	private int height;
	private String title;
	private Table rootTable;

	private float dragX = -1, dragY = -1;

	public WorldView(WorldScreen screen, int width, int height, String title) {
		this.screen = screen;
		this.width = width;
		this.height = height;
		this.title = title;
	}

	public void makeContent(Stage stage) {
		this.rootTable = new Table();
		this.rootTable.setSize(width, height);

		Table bar = new Table();
		bar.setBackground(UI.skin.getDrawable("grey"));

		final Table draggingSpace = new Table();
		draggingSpace.setTouchable(Touchable.enabled);
		draggingSpace.addListener(new DragListener() {
			@Override
			public void dragStart(InputEvent event, float x, float y, int pointer) {
				this.setTapSquareSize(5f);
				dragX = x;
				dragY = y;
			}

			@Override
			public void drag(InputEvent event, float x, float y, int pointer) {
				rootTable.moveBy(x - dragX, y - dragY);

				if (rootTable.getX() < 0) {
					rootTable.setX(0);
				}
				if (rootTable.getY() < 0) {
					rootTable.setY(0);
				}
				if (rootTable.getX() + rootTable.getWidth() >= rootTable.getParent().getWidth()) {
					rootTable.setX(rootTable.getParent().getWidth() - rootTable.getWidth());
				}
				if (rootTable.getY() + rootTable.getHeight() >= rootTable.getParent().getHeight()) {
					rootTable.setY(rootTable.getParent().getHeight() - rootTable.getHeight());
				}
			}

			@Override
			public void dragStop(InputEvent event, float x, float y, int pointer) {
				dragX = -1;
				dragY = -1;
			}

		});

		final Label titleLabel = new Label(title, UI.skin);
		draggingSpace.add(titleLabel);

		bar.add(draggingSpace).expand().fill();

		final ImageButton closeButton = new ImageButton(UI.skin.getDrawable("icon-close"));
		closeButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				close();
			}
		});
		bar.add(closeButton).right();

		Table contentTable = new Table();
		makeContentInternal(stage, contentTable);

		rootTable.top();
		rootTable.add(bar).expandX().fillX();

		rootTable.row();
		rootTable.add(contentTable).expand().fill();

	}

	protected abstract void makeContentInternal(Stage stage, Table rootTable);

	private void close() {
		screen.closeView(this);
	}

	public abstract void dispose();

	public Table getRootTable() {
		return rootTable;
	}

	public WorldScreen getScreen() {
		return screen;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

}
