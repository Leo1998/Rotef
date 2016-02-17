package com.rotef.game.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.rotef.game.Game;

public class OptionsScreen extends BaseScreen {

	@Override
	public void show() {
		super.show();

		rootTable.setColor(1, 1, 1, 0.75f);
	}

	@Override
	protected void makeContent() {
		float padding = 10.0f;

		rootTable.top();
		rootTable.columnDefaults(0).padRight(20f);

		final TextButton backButton = new TextButton("Back", UI.textButtonStyle);
		backButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Game.game.setScreen(new MainMenuScreen());
			}
		});
		rootTable.add(backButton).left();

		final Label titleLabel = new Label("Options", UI.labelStyle);
		titleLabel.setFontScale(3.5f);
		rootTable.add(titleLabel).center().expandX().padTop(padding).padBottom(padding);
		rootTable.row();

		Table t1 = new Table();
		rootTable.add(t1).colspan(2).padTop(35f);

		final CheckBox fullscreenCheckbox = new CheckBox("Fullscreen", UI.checkBoxStyle);
		fullscreenCheckbox.setChecked(Game.config.isFullscreen());
		fullscreenCheckbox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Game.config.setFullscreen(fullscreenCheckbox.isChecked());
				Game.applyConfig();
			}
		});
		t1.add(fullscreenCheckbox).padBottom(25f);
		t1.row();

		final CheckBox vSyncCheckbox = new CheckBox("VSync", UI.checkBoxStyle);
		vSyncCheckbox.setChecked(Game.config.isVSync());
		vSyncCheckbox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Game.config.setVSync(vSyncCheckbox.isChecked());
				Game.applyConfig();
			}
		});
		t1.add(vSyncCheckbox).padBottom(25f);
		t1.row();

		final CheckBox debugCheckbox = new CheckBox("Debug", UI.checkBoxStyle);
		debugCheckbox.setChecked(Game.config.isDebug());
		debugCheckbox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Game.config.setDebug(debugCheckbox.isChecked());
				Game.applyConfig();
			}
		});
		t1.add(debugCheckbox).padBottom(25f);
		t1.row();

		final Label lightMapDownScaleSliderLabel = new Label("LightMapDownScale: " + Game.config.getLightMapDownScale(),
				UI.labelStyle);
		final Slider lightMapResSlider = new Slider(1, 4, 1, false, UI.sliderStyle);
		lightMapResSlider.setValue(Game.config.getLightMapDownScale());
		lightMapResSlider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Game.config.setLightMapDownScale((int) lightMapResSlider.getValue());
				Game.applyConfig();

				lightMapDownScaleSliderLabel.setText("LightMapDownScale: " + Game.config.getLightMapDownScale());
			}
		});
		t1.add(lightMapResSlider);
		t1.add(lightMapDownScaleSliderLabel).padLeft(5f);
		t1.row();

		final Label uiSizeSliderLabel = new Label("UI Size: " + Game.config.getUiSize(), UI.labelStyle);
		final Slider uiSizeSlider = new Slider(1.0f, 2.5f, 0.2f, false, UI.sliderStyle);
		uiSizeSlider.setValue(Game.config.getUiSize());
		uiSizeSlider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Game.config.setUiSize(uiSizeSlider.getValue());

				uiSizeSliderLabel.setText("UI Size: " + Game.config.getUiSize());
			}
		});
		t1.add(uiSizeSlider);
		t1.add(uiSizeSliderLabel).padLeft(5f);
		t1.row();
	}

}
