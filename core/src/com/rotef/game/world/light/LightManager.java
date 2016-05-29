package com.rotef.game.world.light;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.rotef.game.renderer.WorldRenderer;
import com.rotef.game.renderer.WorldViewport;
import com.rotef.game.util.MathHelper;
import com.rotef.game.world.World;
import com.rotef.game.world.physics.PhysicsManager;
import com.rotef.game.world.tile.Tile;

public class LightManager {

	public class LightingState {
		public float r = 0f;
		public float g = 0f;
		public float b = 0f;

		public final int x;
		public final int y;

		public LightingState(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public void set(float r, float g, float b) {
			this.r = r;
			this.g = g;
			this.b = b;
		}
	}

	private Array<LightingState> litStates = new Array<>();
	private LightingState[][] states;
	private int offScreenTiles = 10;
	private int tileWidth;
	private int tileHeight;
	private int startX;
	private int startY;

	private LightArray lights = new LightArray();
	private LightArray visibleLights = new LightArray();
	private World world;

	private LightMap lightMap;

	private Rectangle tmpRect = new Rectangle();

	public LightManager(World world) {
		this.world = world;
	}

	public void resize(int width, int height) {
		this.tileWidth = (width / Tile.TILE_SIZE) + offScreenTiles * 2;
		this.tileHeight = (height / Tile.TILE_SIZE) + offScreenTiles * 2;

		this.states = new LightingState[tileWidth][tileHeight];
		for (int x = 0; x < tileWidth; x++) {
			for (int y = 0; y < tileHeight; y++) {
				states[x][y] = new LightingState(x, y);
			}
		}

		if (lightMap == null) {
			this.lightMap = new LightMap(tileWidth, tileHeight);
		}
		this.lightMap.init(tileWidth, tileHeight);

	}

	public LightMap getLightMap() {
		return lightMap;
	}

	public void renderLightMap(SpriteBatch batch, float x, float y, float w, float h) {
		float tileXFloat = x / Tile.TILE_SIZE;
		float tileYFloat = y / Tile.TILE_SIZE;
		float difX = tileXFloat - (int) tileXFloat;
		float difY = tileYFloat - (int) tileYFloat;

		float x0 = x - offScreenTiles * Tile.TILE_SIZE;
		float y0 = y - offScreenTiles * Tile.TILE_SIZE;
		float w0 = tileWidth * Tile.TILE_SIZE;
		float h0 = tileHeight * Tile.TILE_SIZE;

		x0 -= difX * Tile.TILE_SIZE;
		y0 -= difY * Tile.TILE_SIZE;

		batch.draw(lightMap.getMap(), x0, y0, w0, h0, 0, 0, tileWidth, tileHeight, false, true);
	}

	public void update(WorldRenderer renderer, WorldViewport viewport) {
		if (lightMap == null) {
			int w = Gdx.graphics.getWidth();
			int h = Gdx.graphics.getHeight();
			resize(w, h);
		}

		updateVisibleLights(viewport);

		litStates.clear();

		startX = (int) (viewport.getX() / Tile.TILE_SIZE) - offScreenTiles;
		startY = (int) (viewport.getY() / Tile.TILE_SIZE) - offScreenTiles;

		// float wMap = viewport.getWidth() / PhysicsManager.PPM;
		// float hMap = viewport.getHeight() / PhysicsManager.PPM;

		for (int x = 0; x < tileWidth; x++) {
			for (int y = 0; y < tileHeight; y++) {
				LightingState state = states[x][y];

				int worldX = startX + x;
				int worldY = startY + y;

				Tile tile = world.getTile(worldX, worldY);

				if (tile != null && tile.isSolid()) {
					state.set(0.0f, 0.0f, 0.0f);
				} else {
					int a = 10;
					float brightness = MathHelper.ensureRange(worldY - world.getSurface(), 0, a) / (float) a;
					state.set(brightness, brightness, brightness);

					if (brightness > 0.0f) {
						litStates.add(state);
					}
				}
			}
		}

		for (int x = 0; x < tileWidth; x++) {
			for (int y = 0; y < tileHeight; y++) {
				LightingState state = states[x][y];

				int worldX = startX + x;
				int worldY = startY + y;

				Tile tile = world.getTile(worldX, worldY);

				if (tile != null && tile.isSolid()) {
					float lit = 0.0f;

					double dist = Double.MAX_VALUE;
					for (LightingState s : litStates) {
						double dist0 = MathHelper.distance(x, y, s.x, s.y);
						if (dist0 < dist) {
							dist = dist0;
						}
					}

					lit = (float) (1 / dist);

					state.set(lit, lit, lit);
				}
			}
		}

		lightMap.setLightData(states);
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

	private boolean lightVisible(Light light, WorldViewport viewport) {
		float x = light.getX() * PhysicsManager.PPM;
		float y = light.getY() * PhysicsManager.PPM;
		float dist = light.getDistance() * PhysicsManager.PPM;

		tmpRect.set(x - dist, y - dist, dist * 2, dist * 2);

		return viewport.contains(tmpRect);
	}

	public void dispose() {
		if (lightMap != null) {
			lightMap.dispose();
		}
	}

	public void addLight(Light light) {
		lights.add(light);
	}

	public void removeLight(Light light) {
		lights.removeValue(light, true);
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

	public LightingState[][] getStates() {
		return states;
	}

}