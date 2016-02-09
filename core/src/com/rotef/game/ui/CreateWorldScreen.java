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
import com.rotef.game.world.World;
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

		final TextButton cancelButton = new TextButton("Cancel", UI.textButtonStyle);
		cancelButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Game.game.setScreen(new MainMenuScreen());
			}
		});
		rootTable.add(cancelButton).left();

		final Label titleLabel = new Label("Create New World", UI.labelStyle);
		titleLabel.setFontScale(2.5f);
		rootTable.add(titleLabel).center().expandX().padTop(10f).padBottom(10f);
		rootTable.row();

		final TextField nameTextField = new TextField("New World", UI.textFieldStyle);
		nameTextField.setMaxLength(World.WORLD_NAME_MAX_LENGTH);
		rootTable.add(nameTextField).width(width / 2 / uiScale).center().colspan(2).padTop(100f);
		rootTable.row();

		final Label errorLabel = new Label("", UI.labelStyle);
		errorLabel.setColor(Color.RED);
		rootTable.add(errorLabel).center().colspan(2).padTop(8f);
		rootTable.row();

		final TextButton createButton = new TextButton("Create World", UI.textButtonStyle);
		createButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				String worldName = nameTextField.getText();
				nameTextField.getOnscreenKeyboard().show(false);

				boolean valid = true;
				String errorMessage = "";

				if (worldName == null || worldName.isEmpty() || worldName.length() > World.WORLD_NAME_MAX_LENGTH) {
					valid = false;
					errorMessage = "World Name can't be empty or longer than " + World.WORLD_NAME_MAX_LENGTH + " characters!";
				}

				WorldDescriptor[] worlds = WorldDescriptor.getExistingWorlds();
				for (int i = 0; i < worlds.length; i++) {
					if (worlds[i].getName().equals(worldName)) {
						valid = false;
						errorMessage = "This World already exists!";
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

}