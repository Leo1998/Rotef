package com.rotef.game.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.rotef.game.Game;
import com.rotef.game.util.MathHelper;

public class OptionsScreen extends BaseScreen {

	@Override
	protected void makeContent() {
		float padding = 10.0f;

		rootTable.top();
		rootTable.columnDefaults(0).padRight(20f);

		final TextButton backButton = new TextButton(Game.language.get("back"), UI.skin);
		backButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Game.game.setScreen(new MainMenuScreen());
			}
		});
		rootTable.add(backButton).left();

		final Label titleLabel = new Label(Game.language.get("options"), UI.skin);
		titleLabel.setFontScale(3.5f);
		rootTable.add(titleLabel).center().expandX().padTop(padding).padBottom(padding);
		rootTable.row();

		Table t1 = new Table();
		rootTable.add(t1).colspan(2).padTop(35f);

		final CheckBox fullscreenCheckbox = new CheckBox(Game.language.get("fullscreen"), UI.skin);
		fullscreenCheckbox.setChecked(Game.config.isFullscreen());
		fullscreenCheckbox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Game.config.setFullscreen(fullscreenCheckbox.isChecked());
				Game.applyConfig();
			}
		});
		t1.add(fullscreenCheckbox).align(Align.left).padBottom(25f);
		t1.row();

		final CheckBox vSyncCheckbox = new CheckBox(Game.language.get("vsync"), UI.skin);
		vSyncCheckbox.setChecked(Game.config.isVSync());
		vSyncCheckbox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Game.config.setVSync(vSyncCheckbox.isChecked());
				Game.applyConfig();
			}
		});
		t1.add(vSyncCheckbox).align(Align.left).padBottom(25f);
		t1.row();

		final CheckBox debugCheckbox = new CheckBox(Game.language.get("debugMode"), UI.skin);
		debugCheckbox.setChecked(Game.config.isDebug());
		debugCheckbox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Game.config.setDebug(debugCheckbox.isChecked());
				Game.applyConfig();
			}
		});
		t1.add(debugCheckbox).align(Align.left).padBottom(25f);
		t1.row();

		final Label uiSizeSliderLabel = new Label(Game.language.get("uiSize") + ": " + Game.config.getUiSize(), UI.skin);
		final Slider uiSizeSlider = new Slider(1.0f, 2.0f, 0.01f, false, UI.skin);
		uiSizeSlider.setValue(Game.config.getUiSize());
		uiSizeSlider.setAnimateDuration(0.1f);
		uiSizeSlider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Game.config.setUiSize(MathHelper.round(uiSizeSlider.getValue(), 2));

				uiSizeSliderLabel.setText(Game.language.get("uiSize") + ": " + Game.config.getUiSize());
			}
		});
		t1.add(uiSizeSlider);
		t1.add(uiSizeSliderLabel).padLeft(5f);
		t1.row();
	}

}
