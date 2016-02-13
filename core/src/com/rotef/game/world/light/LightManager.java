package com.rotef.game.world.light;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Rectangle;
import com.rotef.game.Game;
import com.rotef.game.renderer.WorldRenderer;
import com.rotef.game.renderer.WorldViewport;
import com.rotef.game.world.World;
import com.rotef.game.world.physics.PhysicsManager;

public class LightManager {

	private Rectangle tmpRect = new Rectangle();
	private SpriteBatch shadowRenderBatch;

	private LightArray lights = new LightArray();
	private LightArray visibleLights = new LightArray();
	private World world;

	private final int downScale;
	private LightMap lightMap;
	private Color ambientLight = new Color(0.01f, 0.01f, 0.01f, 1.0f);

	public LightManager(World world) {
		this.world = world;
		this.downScale = Game.config.getLightMapDownScale();
	}

	public void resize(int width, int height) {
		if (lightMap != null) {
			this.lightMap.init(width / downScale, height / downScale);
		}
	}

	public LightMap getLightMap() {
		return lightMap;
	}

	public void update(WorldRenderer renderer, WorldViewport viewport, float sunIntensity, ShaderProgram lightMapShader, ShaderProgram shadowMapShader) {
		if (shadowRenderBatch == null) {
			shadowRenderBatch = new SpriteBatch();
		}
		if (lightMap == null) {
			int w = Gdx.graphics.getWidth();
			int h = Gdx.graphics.getHeight();

			this.lightMap = new LightMap(w / downScale, h / downScale);
		}

		updateVisibleLights(viewport);

		updateShadowMaps(renderer, shadowMapShader);

		float xMap = viewport.getX() / PhysicsManager.PPM;
		float yMap = viewport.getY() / PhysicsManager.PPM;

		float wMap = viewport.getWidth() / PhysicsManager.PPM;
		float hMap = viewport.getHeight() / PhysicsManager.PPM;

		lightMap.render(lightMapShader, downScale, xMap, yMap, wMap, hMap, ambientLight, visibleLights, sunIntensity);
	}

	private void updateVisibleLights(WorldViewport viewport) {
		visibleLights.clear();

		for (int i = 0; i < lights.size; i++) {
			Light light = lights.get(i);
			if (lightVisible(light, viewport)) {
				visibleLights.add(light);
			}
		}
	}

	private void updateShadowMaps(WorldRenderer renderer, ShaderProgram shadowMapShader) {
		for (int i = 0; i < visibleLights.size; i++) {
			Light light = visibleLights.get(i);
			if (light.getShadowMap() == null) {
				light.setShadowMap(new ShadowMap(light));
			}
			ShadowMap map = light.getShadowMap();

			map.begin();

			renderer.renderOccluders(light, map.getCam());

			map.end(shadowRenderBatch, shadowMapShader);
		}
	}

	private boolean lightVisible(Light light, WorldViewport viewport) {
		float x = light.getX() * PhysicsManager.PPM;
		float y = light.getY() * PhysicsManager.PPM;
		float dist = light.getDistance() * PhysicsManager.PPM;

		tmpRect.set(x - dist, y - dist, dist * 2, dist * 2);

		return viewport.contains(tmpRect);
	}

	public void dispose() {
		if (shadowRenderBatch != null) {
			shadowRenderBatch.dispose();
		}
	}

	public void addLight(Light light) {
		lights.add(light);
	}

	public void removeLight(Light light) {
		lights.removeValue(light, true);
		light.disposeShadowMap();
	}

	public int getLightCount() {
		return lights.size;
	}

	public int getVisibleLightCount() {
		return visibleLights.size;
	}

	public World getWorld() {
		return world;
	}

}