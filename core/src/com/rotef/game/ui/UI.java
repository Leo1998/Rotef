package com.rotef.game.ui;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class UI {

	public static BitmapFont font;

	public static ScrollPaneStyle scrollPaneStyle;
	public static TouchpadStyle touchpadStyle;
	public static TextButtonStyle textButtonStyle;
	public static LabelStyle labelStyle;
	public static ProgressBarStyle progressBarStyle;
	public static CheckBoxStyle checkBoxStyle;
	public static SliderStyle sliderStyle;
	public static TextFieldStyle textFieldStyle;

	private static Map<String, TextureRegion> atlas = new HashMap<String, TextureRegion>();

	public static void load() {
		font = loadTTFFont("common/ui/font.ttf");

		loadUITexture("pane.png");
		loadUITexture("button-up.png");
		loadUITexture("button-down.png");
		loadUITexture("progressbar-background.png");
		loadUITexture("progressbar-knob.png");
		loadUITexture("checkbox-on.png");
		loadUITexture("checkbox-off.png");
		loadUITexture("slider-background.png");
		loadUITexture("slider-knob.png");
		loadUITexture("textField-background.png");
		loadUITexture("textField-cursor.png");

		scrollPaneStyle = new ScrollPaneStyle();
		scrollPaneStyle.background = new TextureRegionDrawable(getTexture("pane"));

		textButtonStyle = new TextButtonStyle();
		textButtonStyle.font = font;
		textButtonStyle.up = new TextureRegionDrawable(getTexture("button-up"));
		textButtonStyle.down = new TextureRegionDrawable(getTexture("button-down"));
		textButtonStyle.over = textButtonStyle.down;

		labelStyle = new LabelStyle();
		labelStyle.font = font;
		labelStyle.fontColor = Color.WHITE;

		progressBarStyle = new ProgressBarStyle();
		progressBarStyle.background = new TextureRegionDrawable(getTexture("progressbar-background"));
		progressBarStyle.knobBefore = new TextureRegionDrawable(getTexture("progressbar-knob"));

		checkBoxStyle = new CheckBoxStyle();
		checkBoxStyle.font = font;
		checkBoxStyle.checkboxOn = new TextureRegionDrawable(getTexture("checkbox-on"));
		checkBoxStyle.checkboxOff = new TextureRegionDrawable(getTexture("checkbox-off"));

		sliderStyle = new SliderStyle();
		sliderStyle.background = new TextureRegionDrawable(getTexture("slider-background"));
		sliderStyle.knob = new TextureRegionDrawable(getTexture("slider-knob"));

		textFieldStyle = new TextFieldStyle();
		textFieldStyle.font = font;
		textFieldStyle.fontColor = Color.WHITE;
		textFieldStyle.background = new TextureRegionDrawable(getTexture("textField-background"));
		textFieldStyle.cursor = new TextureRegionDrawable(getTexture("textField-cursor"));
	}

	private static BitmapFont loadTTFFont(String path) {
		FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal(path));

		int dp = 16;
		int fontSize = Math.round(dp * Gdx.graphics.getDensity());

		FreeTypeFontParameter param = new FreeTypeFontParameter();
		param.size = fontSize;
		param.genMipMaps = true;

		BitmapFont font = gen.generateFont(param);

		gen.dispose();
		return font;
	}

	private static void loadUITexture(String filename) {
		FileHandle file = Gdx.files.internal("common/ui/" + filename);

		TextureRegion texture = new TextureRegion(new Texture(file));

		atlas.put(file.nameWithoutExtension(), texture);
	}

	public static TextureRegion getTexture(String key) {
		return atlas.get(key);
	}

	public static void renderString(SpriteBatch batch, String msg, int x, int y) {
		font.draw(batch, msg, x, y);
	}

}
