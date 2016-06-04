package com.rotef.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.rotef.game.Game;
import com.rotef.game.world.WorldDescriptor;

public class MainMenuScreen extends BaseScreen {

	@Override
	public void show() {
		super.show();
	}

	@Override
	protected void makeContent() {
		Label titleLabel = new Label(Game.language.get("title"), UI.skin);
		titleLabel.setFontScale(1.6f);

		rootTable.add(titleLabel).top().center().pad(40f);

		rootTable.row();

		final Table scrollTable = new Table();

		ScrollPane scrollPane = new ScrollPane(scrollTable, UI.skin);
		rootTable.add(scrollPane).width(width / 2 / uiScale).height(height / 2 / uiScale).center().expandX();

		WorldDescriptor[] worlds = WorldDescriptor.getExistingWorlds();

		scrollTable.top();
		for (int i = 0; i < worlds.length; i++) {
			final WorldDescriptor desc = worlds[i];
			final String worldName = desc.getName();

			final Table t = new Table();
			float padding = 3.0f;

			Label label = new Label(worldName, UI.skin);
			t.add(label).left().padRight(padding);

			TextButton enterButton = new TextButton(Game.language.get("enterWorld"), UI.skin);
			enterButton.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					Game.openWorld(worldName);
				}
			});
			t.add(enterButton).center().padLeft(padding).padRight(padding);

			TextButton deleteButton = new TextButton(Game.language.get("deleteWorld"), UI.skin);
			deleteButton.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					desc.delete();
					scrollTable.removeActor(t);
				}
			});
			t.add(deleteButton).right().padLeft(padding);

			scrollTable.add(t).expandX();
			scrollTable.row();
		}

		TextButton createButton = new TextButton(Game.language.get("createNewWorld"), UI.skin);
		createButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Game.game.setScreen(new CreateWorldScreen());
			}
		});
		rootTable.row();
		rootTable.add(createButton).padTop(10f).center();

		TextButton optionsButton = new TextButton(Game.language.get("options"), UI.skin);
		optionsButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Game.game.setScreen(new OptionsScreen());
			}
		});
		rootTable.row();

		rootTable.add(optionsButton).padTop(10f).center();

		TextButton exitButton = new TextButton(Game.language.get("exit"), UI.skin);
		exitButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.app.exit();
			}
		});
		rootTable.row();
		rootTable.add(exitButton).padTop(10f).center();

	}

}
