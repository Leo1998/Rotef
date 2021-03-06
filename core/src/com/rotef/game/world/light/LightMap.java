package com.rotef.game.world.light;

import java.nio.ByteBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.utils.BufferUtils;
import com.rotef.game.util.BufferDisposer;

public class LightMap {

	private int width;
	private int height;
	private Texture map;
	private ByteBuffer buffer;

	public LightMap(int width, int height, boolean smoothFiltering) {
		init(width, height, smoothFiltering);
	}

	public void init(int width, int height, boolean smoothFiltering) {
		if (map != null) {
			map.dispose();
		}
		if (buffer != null) {
			try {
				BufferDisposer.disposeDirectBuffer(buffer);
			} catch (Exception e) {
			}
		}

		this.width = width;
		this.height = height;

		this.buffer = BufferUtils.newByteBuffer(width * height * 3);

		this.map = new Texture(width, height, Format.RGB888);
		map.setWrap(TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);
		if (smoothFiltering) {
			map.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		} else {
			map.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		}
	}

	public void setLightData(LightingState[] states) {
		map.bind();

		buffer.position(0);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				LightingState state = states[x + y * width];

				buffer.put(convertColorChannel(state.r));
				buffer.put(convertColorChannel(state.g));
				buffer.put(convertColorChannel(state.b));
			}
		}
		buffer.flip();

		int format = Format.toGlFormat(Format.RGB888);
		int type = Format.toGlType(Format.RGB888);
		Gdx.gl.glTexImage2D(GL20.GL_TEXTURE_2D, 0, format, width, height, 0, format, type, buffer);
	}

	private byte convertColorChannel(float c) {
		int i = (int) (c * 255);

		byte b = (byte) i;

		return b;
	}

	public void dispose() {
		if (map != null) {
			map.dispose();
		}
		if (buffer != null) {
			try {
				BufferDisposer.disposeDirectBuffer(buffer);
			} catch (Exception e) {
			}
		}
	}

	public Texture getMap() {
		return map;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

}
