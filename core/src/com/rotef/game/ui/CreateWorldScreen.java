package com.rotef.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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

		final TextButton cancelButton = new TextButton(Game.language.get("cancel"), UI.textButtonStyle);
		cancelButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Game.game.setScreen(new MainMenuScreen());
			}
		});
		rootTable.add(cancelButton).left();

		final Label titleLabel = new Label(Game.language.get("createNewWorld"), UI.labelStyle);
		titleLabel.setFontScale(2.5f);
		rootTable.add(titleLabel).center().expandX().padTop(10f).padBottom(10f);
		rootTable.row();

		final TextField nameTextField = new TextField(findUnusedWorldName(), UI.textFieldStyle);
		rootTable.add(nameTextField).width(width / 2 / uiScale).center().colspan(2).padTop(100f);
		rootTable.row();

		final Label errorLabel = new Label("", UI.labelStyle);
		errorLabel.setColor(Color.RED);
		rootTable.add(errorLabel).center().colspan(2).padTop(8f);
		rootTable.row();

		final TextButton createButton = new TextButton(Game.language.get("create"), UI.textButtonStyle);
		createButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
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
