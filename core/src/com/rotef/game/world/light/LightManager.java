package com.rotef.game.world.light;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.rotef.game.Game;
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
		public boolean lit = false;
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

	// public class LightingSwipe {
	// public LightingState[][] states;
	// }

	private LightingMode lightingMode = LightingMode.Low;
	private int resolution = lightingMode.resolution;
	
	private LightingState[][] states;
	private int stateWidth;
	private int stateHeight;
	private int startX;
	private int startY;
	private int offScreenTiles = 10;

	private Color sunColor = Color.WHITE;

	private LightArray lights = new LightArray();
	private LightArray visibleLights = new LightArray();
	private World world;

	private LightMap lightMap;

	private Rectangle tmpRect = new Rectangle();

	public LightManager(World world) {
		this.world = world;
	}

	public void init(int width, int height) {
		this.lightingMode = Game.config.getLightingMode();
		this.resolution = lightingMode.resolution;
		boolean smoothFiltering = lightingMode.smoothFiltering;
		
		this.stateWidth = ((width / Tile.TILE_SIZE) + offScreenTiles * 2) * resolution;
		this.stateHeight = ((height / Tile.TILE_SIZE) + offScreenTiles * 2) * resolution;

		this.states = new LightingState[stateWidth][stateHeight];
		for (int x = 0; x < stateWidth; x++) {
			for (int y = 0; y < stateHeight; y++) {
				states[x][y] = new LightingState(x, y);
			}
		}

		if (lightMap == null) {
			this.lightMap = new LightMap(stateWidth, stateHeight, smoothFiltering);
		}
		this.lightMap.init(stateWidth, stateHeight, smoothFiltering);

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
		float w0 = (stateWidth / resolution) * Tile.TILE_SIZE;
		float h0 = (stateHeight / resolution) * Tile.TILE_SIZE;

		x0 -= difX * Tile.TILE_SIZE;
		y0 -= difY * Tile.TILE_SIZE;

		batch.draw(lightMap.getMap(), x0, y0, w0, h0, 0, 0, stateWidth, stateHeight, false, true);
	}

	public void update(WorldRenderer renderer, WorldViewport viewport) {
		if (lightMap == null) {
			int w = Gdx.graphics.getWidth();
			int h = Gdx.graphics.getHeight();
			init(w, h);
		}

		updateVisibleLights(viewport);

		startX = (int) (viewport.getX() / Tile.TILE_SIZE) - offScreenTiles;
		startY = (int) (viewport.getY() / Tile.TILE_SIZE) - offScreenTiles;

		for (int x = 0; x < stateWidth; x++) {
			for (int y = 0; y < stateHeight; y++) {
				LightingState state = states[x][y];

				int worldX = startX + (x / resolution);
				int worldY = startY + (y / resolution);

				Tile tile = world.getTile(worldX, worldY);

				if (tile != null && tile.isSolid()) {
					state.set(0.0f, 0.0f, 0.0f);
					state.lit = false;
				} else {
					int a = 10;
					float brightness = MathHelper.ensureRange(worldY - world.getSurface(), 0, a) / (float) a;
					brightness *= world.getTimeManager().getSunIntensity();
					
					state.set(sunColor.r * brightness, sunColor.g * brightness, sunColor.b * brightness);
					state.lit = brightness > 0.0f;
				}
			}
		}

		for (int x = 0; x < stateWidth; x++) {
			for (int y = 0; y < stateHeight; y++) {
				LightingState state = states[x][y];

				int worldX = startX + (x / resolution);
				int worldY = startY + (y / resolution);

				Tile tile = world.getTile(worldX, worldY);

				if (tile != null && tile.isSolid()) {
					float lit = 0.05f;

					int shineDist = 4 * resolution;

					LightingState s = null;
					double dist = Double.MAX_VALUE;
					for (int x0 = x - shineDist; x0 < x + shineDist; x0++) {
						for (int y0 = y - shineDist; y0 < y + shineDist; y0++) {
							if (x0 >= 0 && x0 < stateWidth && y0 >= 0 && y0 < stateHeight) {
								LightingState s0 = states[x0][y0];

								if (s0.lit) {
									double dist0 = MathHelper.distance(x, y, s0.x, s0.y);
									if (dist0 < dist) {
										s = s0;
										dist = dist0;
									}
								}
							}
						}
					}

					if (s != null) {
						lit += 1.0f - (float) (Math.min(dist, shineDist) / shineDist);
						lit = MathHelper.ensureRange(lit, 0.0f, 1.0f);

						state.set(s.r * lit, s.g * lit, s.b * lit);
					}
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