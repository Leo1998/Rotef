package com.rotef.game.renderer;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class WorldViewport {

	private float x;
	private float y;
	private float width;
	private float height;

	private Rectangle viewBounds = new Rectangle();

	public WorldViewport() {
		this.x = 0;
		this.y = 0;
		this.width = 0;
		this.height = 0;
		updateBounds();
	}

	private void updateBounds() {
		viewBounds.set(x, y, width, height);
	}

	public boolean contains(Rectangle rect) {
		return viewBounds.contains(rect) || viewBounds.overlaps(rect);
	}

	public boolean contains(Vector2 point) {
		return viewBounds.contains(point);
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
		updateBounds();
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
		updateBounds();
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
		updateBounds();
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
		updateBounds();
	}

	public void set(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		updateBounds();
	}

}
