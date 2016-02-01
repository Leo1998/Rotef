package com.rotef.game.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.rotef.game.Game;
import com.rotef.game.world.WorldDescriptor;

public class MainMenuScreen extends BaseScreen {

	@Override
	public void show() {
		super.show();

		rootTable.setColor(1, 1, 1, 0.75f);
	}

	@Override
	protected void makeContent() {
		Label titleLabel = new Label("Rise Of The Endless Fire", UI.labelStyle);
		titleLabel.setFontScale(1.6f);
		rootTable.add(titleLabel).top().center().pad(40f);

		rootTable.row();

		final Table scrollTable = new Table();

		ScrollPane scrollPane = new ScrollPane(scrollTable, UI.scrollPaneStyle);
		rootTable.add(scrollPane).width(width / 2 / uiScale).height(height / 2 / uiScale).center().expandX();

		WorldDescriptor[] worlds = WorldDescriptor.getExistingWorlds();

		scrollTable.top();
		for (int i = 0; i < worlds.length; i++) {
			final WorldDescriptor desc = worlds[i];
			final String worldName = desc.getName();

			final Table t = new Table();
			float padding = 3.0f;

			Label label = new Label(worldName, UI.labelStyle);
			t.add(label).left().padRight(padding);

			TextButton enterButton = new TextButton("Enter", UI.textButtonStyle);
			enterButton.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					Game.openWorld(worldName);
				}
			});
			t.add(enterButton).center().padLeft(padding).padRight(padding);

			TextButton deleteButton = new TextButton("Delete", UI.textButtonStyle);
			deleteButton.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					desc.getDirectory().deleteDirectory();

					scrollTable.removeActor(t);
				}
			});
			t.add(deleteButton).right().padLeft(padding);

			scrollTable.add(t).expandX();
			scrollTable.row();
		}

		TextButton createButton = new TextButton("Create New World", UI.textButtonStyle);
		createButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Game.game.setScreen(new CreateWorldScreen());
			}
		});
		rootTable.row();
		rootTable.add(createButton).center();

		TextButton optionsButton = new TextButton("Options", UI.textButtonStyle);
		optionsButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Game.game.setScreen(new OptionsScreen());
			}
		});
		rootTable.row();

		rootTable.add(optionsButton).right().bottom();
	}

}
