package com.rotef.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.rotef.game.Game;
import com.rotef.game.world.WorldDescriptor;

public class CreateWorldScreen extends BaseScreen {

	@Override
	public void show() {
		super.show();

		rootTable.setColor(1, 1, 1, 0.75f);
	}

	@Override
	protected void makeContent() {
		rootTable.top();
		rootTable.columnDefaults(0).padRight(20f);

		final TextButton cancelButton = new TextButton(Game.language.get("cancel"), UI.skin);
		cancelButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Game.game.setScreen(new MainMenuScreen());
			}
		});
		rootTable.add(cancelButton).left();

		final Label titleLabel = new Label(Game.language.get("createNewWorld"), UI.skin);
		titleLabel.setFontScale(2.5f);
		rootTable.add(titleLabel).center().expandX().padTop(10f).padBottom(10f);
		rootTable.row();

		final TextField nameTextField = new TextField(findUnusedWorldName(), UI.skin);
		rootTable.add(nameTextField).width(width / 2 / uiScale).center().colspan(2).padTop(100f);
		rootTable.row();

		final Label errorLabel = new Label("", UI.skin);
		errorLabel.setColor(Color.RED);
		rootTable.add(errorLabel).center().colspan(2).padTop(8f);
		rootTable.row();

		final TextButton createButton = new TextButton(Game.language.get("create"), UI.skin);
		createButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				String worldName = nameTextField.getText();
				nameTextField.getOnscreenKeyboard().show(false);

				boolean valid = true;
				String errorMessage = "";

				WorldDescriptor[] worlds = WorldDescriptor.getExistingWorlds();
				for (int i = 0; i < worlds.length; i++) {
					if (worlds[i].getName().equals(worldName)) {
						valid = false;
						errorMessage = Game.language.get("worldAlreadyExists_error");
					}
				}

				if (valid) {
					Game.openWorld(worldName);
				} else {
					errorLabel.setText(errorMessage);
					errorLabel.getColor().a = 0.0f;
					errorLabel.addAction(Actions.fadeIn(0.5f));
				}
			}
		});
		rootTable.add(createButton).center().colspan(2).padTop(8f);
		rootTable.row();
	}

	private String findUnusedWorldName() {
		WorldDescriptor[] worlds = WorldDescriptor.getExistingWorlds();

		final String defaultName = Game.language.get("defaultWorldName");

		String tryName = defaultName;

		int n = 0;
		while (true) {
			boolean exists = false;
			for (int i = 0; i < worlds.length; i++) {
				if (worlds[i].getName().equals(tryName)) {
					exists = true;
					break;
				}
			}

			if (exists) {
				tryName = defaultName + " " + n++;
			} else {
				break;
			}
		}

		return tryName;
	}

}
