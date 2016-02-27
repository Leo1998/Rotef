package com.rotef.game.world.light;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.rotef.game.renderer.WorldViewport;
import com.rotef.game.world.World;
import com.rotef.game.world.WorldHeightmap;
import com.rotef.game.world.tile.Tile;

public class SunMap {

	private Pixmap pixmap;
	private Texture texture;
	private int width;

	public SunMap(int width) {
		init(width);
	}

	public void init(int width) {
		if (pixmap != null) {
			pixmap.dispose();
		}
		if (texture != null) {
			texture.dispose();
		}

		this.width = width;
		this.pixmap = new Pixmap(width, 1, Format.RGBA8888);
		this.texture = new Texture(width, 1, Format.RGBA8888);
		this.texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		this.texture.setWrap(TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);
	}

	public void update(WorldViewport viewport, World world) {
		WorldHeightmap heightmap = world.getHeightmap();
		int worldWidth = world.getWidth();
		int worldHeight = world.getHeight();

		int x0 = (int) (viewport.getX() / Tile.TILE_SIZE);
		if (x0 < 0)
			x0 = 0;

		pixmap.setColor(Color.WHITE);
		pixmap.fill();

		for (int x = x0; x < x0 + this.width && x < worldWidth; x++) {
			float height = heightmap.getHeight(x) / 2f;

			float invHeight = height / (worldHeight / 2f);

			pixmap.setColor(Color.rgba8888(invHeight, invHeight, invHeight, 1.0f));
			pixmap.drawPixel(x - x0, 0);
		}

		texture.draw(pixmap, 0, 0);
	}

	public void dispose() {
		if (pixmap != null) {
			pixmap.dispose();
		}
		if (texture != null) {
			texture.dispose();
		}
	}

	public Texture getTexture() {
		return texture;
	}

	public int getWidth() {
		return width;
	}

}
