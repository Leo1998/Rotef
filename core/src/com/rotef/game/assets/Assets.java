package com.rotef.game.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {

	private AssetCache cache;

	public Assets() {
		this.cache = new AssetCache();
	}

	public Sprite loadSprite(FileHandle file) {
		return loadSprite(file, file.path());
	}

	public Sprite loadSprite(FileHandle file, String key) {
		if (cache.canLocate(key)) {
			return cache.locate(key);
		}

		Gdx.app.log("Assets", "Loading: " + file);

		TextureRegion tex = new TextureRegion(new Texture(file));
		setupTexture(tex);
		Sprite sprite = new Sprite(tex);

		cache.add(key, sprite);

		return sprite;
	}

	public Sprite getSprite(String key) {
		if (cache.canLocate(key)) {
			return cache.locate(key);
		} else {
			return loadSprite(Gdx.files.internal(key));
		}
	}

	private static void setupTexture(TextureRegion texture) {
		texture.getTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

		fixBleeding(texture);
	}

	private static void fixBleeding(TextureRegion texture) {
		float x = texture.getRegionX();
		float y = texture.getRegionY();
		float width = texture.getRegionWidth();
		float height = texture.getRegionHeight();
		float invTexWidth = 1f / texture.getTexture().getWidth();
		float invTexHeight = 1f / texture.getTexture().getHeight();
		texture.setRegion((x + .00000001f) * invTexWidth, (y + .00000001f) * invTexHeight, (x + width - .00000001f) * invTexWidth, (y + height - .00000001f) * invTexHeight);
	}

}
