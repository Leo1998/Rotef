package com.rotef.game.world.light;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Rectangle;
import com.rotef.game.Game;
import com.rotef.game.renderer.WorldRenderer;
import com.rotef.game.renderer.WorldViewport;
import com.rotef.game.world.World;
import com.rotef.game.world.WorldChunk;
import com.rotef.game.world.physics.PhysicsManager;
import com.rotef.game.world.tile.Tile;

public class LightManager {

	private Rectangle tmpRect = new Rectangle();
	private SpriteBatch shadowRenderBatch;

	private LightArray lights = new LightArray();
	private LightArray visibleLights = new LightArray();
	private World world;

	private final int downScale;
	private LightMap[] lightMaps;
	private Color ambientLight = new Color(0.01f, 0.01f, 0.01f, 1.0f);

	public LightManager(World world) {
		this.world = world;
		this.downScale = Game.config.getLightMapDownScale();
	}

	public void initialize() {
		this.lightMaps = new LightMap[(world.getWidth() / WorldChunk.CHUNK_SIZE) * (world.getHeight() / WorldChunk.CHUNK_SIZE)];
	}

	public void onChunkLoaded(WorldChunk chunk) {
		int x = (WorldChunk.CHUNK_SIZE * PhysicsManager.PPM) / downScale;
		LightMap lightMap = new LightMap(x, x);

		setLightMap(chunk.getChunkX(), chunk.getChunkY(), lightMap);
	}

	public void onChunkUnloaded(WorldChunk chunk) {
		LightMap lightMap = getLightMap(chunk.getChunkX(), chunk.getChunkY());
		setLightMap(chunk.getChunkX(), chunk.getChunkY(), null);

		lightMap.dispose();
	}

	public LightMap getLightMap(int x, int y) {
		if (x < 0 || x >= (world.getWidth() / WorldChunk.CHUNK_SIZE) || y < 0 || y >= (world.getHeight() / WorldChunk.CHUNK_SIZE)) {
			return null;
		}

		return lightMaps[x + y * (world.getWidth() / WorldChunk.CHUNK_SIZE)];
	}

	private void setLightMap(int x, int y, LightMap lightMap) {
		if (x < 0 || x >= (world.getWidth() / WorldChunk.CHUNK_SIZE) || y < 0 || y >= (world.getHeight() / WorldChunk.CHUNK_SIZE)) {
			return;
		}

		lightMaps[x + y * (world.getWidth() / WorldChunk.CHUNK_SIZE)] = lightMap;
	}

	public void update(WorldRenderer renderer, WorldViewport viewport, float sunIntensity, ShaderProgram lightMapShader, ShaderProgram shadowMapShader) {
		if (shadowRenderBatch == null) {
			shadowRenderBatch = new SpriteBatch();
		}
		
		updateVisibleLights(viewport);

		updateShadowMaps(renderer, shadowMapShader);

		float x0 = viewport.getX();
		float y0 = viewport.getY();
		float x1 = x0 + viewport.getWidth();
		float y1 = y0 + viewport.getHeight();
		int chunkX0 = (int) (x0 / Tile.TILE_SIZE / WorldChunk.CHUNK_SIZE) - 1;
		int chunkY0 = (int) (y0 / Tile.TILE_SIZE / WorldChunk.CHUNK_SIZE) - 1;
		int chunkX1 = (int) (x1 / Tile.TILE_SIZE / WorldChunk.CHUNK_SIZE) + 1;
		int chunkY1 = (int) (y1 / Tile.TILE_SIZE / WorldChunk.CHUNK_SIZE) + 1;

		for (int xi = chunkX0; xi < chunkX1; xi++) {
			for (int yi = chunkY0; yi < chunkY1; yi++) {
				LightMap lightMap = getLightMap(xi, yi);
				if (lightMap != null) {
					lightMap.render(lightMapShader, downScale, xi * WorldChunk.CHUNK_SIZE, yi * WorldChunk.CHUNK_SIZE, ambientLight, visibleLights, sunIntensity);
				}
			}
		}
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