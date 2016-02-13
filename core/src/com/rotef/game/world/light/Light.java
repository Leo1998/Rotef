package com.rotef.game.world.light;

import com.badlogic.gdx.graphics.Color;

public class Light {

	private float x;
	private float y;
	private float distance;

	private Color color;

	private ShadowMap shadowMap;

	public Light(float x, float y, float distance, Color color) {
		this.x = x;
		this.y = y;
		this.distance = distance;
		this.color = color;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	ShadowMap getShadowMap() {
		return shadowMap;
	}

	void setShadowMap(ShadowMap shadowMap) {
		this.shadowMap = shadowMap;
	}

	void disposeShadowMap() {
		this.shadowMap.dispose();
		this.shadowMap = null;
	}

}
