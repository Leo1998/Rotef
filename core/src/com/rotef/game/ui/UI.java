package com.rotef.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class UI {

	public static Skin skin;

	public static void load() {
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("common/ui/uiskin.atlas"));

		skin = new Skin(Gdx.files.internal("common/ui/uiskin.json"), atlas);
	}

	public static void renderString(SpriteBatch batch, String msg, int x, int y) {
		skin.getFont("default-font").draw(batch, msg, x, y);
	}

}
