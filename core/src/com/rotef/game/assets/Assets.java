package com.rotef.game.assets;

import java.io.BufferedReader;
import java.io.IOException;

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

	public void loadFromAssetFile(FileHandle assetFile) {
		BufferedReader reader = new BufferedReader(assetFile.reader());

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("SPRITE ")) {
					String c = line.substring(7);

					FileHandle file = assetFile.sibling(c);
					Gdx.app.log("Assets", "Loading: " + file);

					loadSprite(file);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Sprite loadSprite(FileHandle file) {
		if (cache.canLocate(file.path())) {
			return cache.locate(file.path());
		}

		TextureRegion tex = new TextureRegion(new Texture(file));
		setupTexture(tex);
		Sprite sprite = new Sprite(tex);

		cache.add(file.path(), sprite);

		return sprite;
	}

	public Sprite getSprite(String key) {
		if (cache.canLocate(key)) {
			return cache.locate(key);
		}

		return null;
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
		texture.setRegion((x + .00000001f) * invTexWidth, (y + .00000001f) * invTexHeight,
				(x + width - .00000001f) * invTexWidth, (y + height - .00000001f) * invTexHeight);
	}

}
