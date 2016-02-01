package com.rotef.game.assets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Sprite {

	private TextureRegion tex;
	private Color tint = Color.WHITE;

	public Sprite(TextureRegion tex) {
		this.tex = tex;
	}

	public TextureRegion getTex() {
		return tex;
	}

	public int getWidth() {
		return tex.getRegionWidth();
	}

	public int getHeight() {
		return tex.getRegionHeight();
	}

	public Color getTint() {
		return tint;
	}

	public void setTint(Color tint) {
		this.tint = tint;
	}

}
