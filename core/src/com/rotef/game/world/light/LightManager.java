package com.rotef.game.world.light;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.rotef.game.Game;
import com.rotef.game.renderer.WorldRenderer;
import com.rotef.game.renderer.WorldViewport;
import com.rotef.game.util.MathHelper;
import com.rotef.game.world.Layer;
import com.rotef.game.world.World;
import com.rotef.game.world.tile.Tile;

public class LightManager {

	private LightingMode lightingMode = LightingMode.Low;
	private int resolution = lightingMode.resolution;

	private LightingState[] states;
	private int stateWidth;
	private int stateHeight;
	private int startX;
	private int startY;
	private int offScreenTiles = 6;

	private Color sunColor = Color.WHITE;
	private float ambient = 0.053f;

	private World world;
	private LightMap lightMap;

	private LightingSwipeExecuter executer;

	private LightingSwipeProcessor.Task lightingPass1 = new LightingSwipeProcessor.Task() {
		@Override
		public void process(LightingSwipe swipe) {
			for (int x = swipe.x; x < swipe.x + swipe.width; x++) {
				for (int y = swipe.y; y < swipe.y + swipe.height; y++) {
					LightingState state = states[x + y * stateWidth];

					int worldX = startX + (x / resolution);
					int worldY = startY + (y / resolution);

					Tile tile = world.getTile(Layer.Foreground, worldX, worldY);

					if (tile != null) {
						Color lightColor = tile.getLightColor();
						if (!lightColor.equals(Color.BLACK)) {
							state.set(lightColor.r, lightColor.g, lightColor.b);
							state.setLit(true);
						} else {
							state.set(0.0f, 0.0f, 0.0f);
							state.setLit(false);
						}
					} else {
						int a = 25;
						float brightness = 1.0f - MathHelper.ensureRange(world.getHighestTileAt(worldX) - worldY, 0, a) / (float) a;
						brightness *= world.getTimeManager().getSunIntensity();
						brightness = MathHelper.ensureRange(brightness, 0.0f, 1.0f);

						state.setLit(brightness > 0.0f);
						state.set(sunColor.r * brightness, sunColor.g * brightness, sunColor.b * brightness);
					}
				}
			}
		}
	};

	private LightingSwipeProcessor.Task lightingPass2 = new LightingSwipeProcessor.Task() {
		@Override
		public void process(LightingSwipe swipe) {
			for (int x = swipe.x; x < swipe.x + swipe.width; x++) {
				for (int y = swipe.y; y < swipe.y + swipe.height; y++) {
					LightingState state = states[x + y * stateWidth];

					if (!state.isLit()) {
						float r = 0.0f;
						float g = 0.0f;
						float b = 0.0f;

						int shineDist = 3 * resolution;

						LightingState s = null;
						double dist = Double.MAX_VALUE;
						for (int x0 = x - shineDist; x0 < x + shineDist; x0++) {
							for (int y0 = y - shineDist; y0 < y + shineDist; y0++) {
								if (x0 >= 0 && x0 < stateWidth && y0 >= 0 && y0 < stateHeight) {
									LightingState s0 = states[x0 + y0 * stateWidth];

									if (s0.isLit()) {
										double dist0 = MathHelper.distance(x, y, s0.getX(), s0.getY());

										if (dist0 < dist) {
											s = s0;
											dist = dist0;
										}
									}
								}
							}
						}

						if (s != null) {
							float lit = 1.0f - (float) (Math.min(dist, shineDist) / shineDist);
							lit = MathHelper.ensureRange(lit, 0.0f, 1.0f);

							r += s.getR() * lit;
							g += s.getG() * lit;
							b += s.getB() * lit;
						}

						r = MathHelper.ensureRange(r, ambient, 1.0f);
						g = MathHelper.ensureRange(g, ambient, 1.0f);
						b = MathHelper.ensureRange(b, ambient, 1.0f);
						state.set(r, g, b);
					}
				}
			}
		}
	};

	public LightManager(World world) {
		this.world = world;
	}

	public void init(int width, int height) {
		this.lightingMode = Game.config.getLightingMode();
		this.resolution = lightingMode.resolution;
		boolean smoothFiltering = lightingMode.smoothFiltering;
		int lightingCores = Game.config.getLightingCores();

		this.stateWidth = ((width / Tile.TILE_SIZE) + offScreenTiles * 2) * resolution;
		this.stateHeight = ((height / Tile.TILE_SIZE) + offScreenTiles * 2) * resolution;

		this.states = new LightingState[stateWidth * stateHeight];
		for (int x = 0; x < stateWidth; x++) {
			for (int y = 0; y < stateHeight; y++) {
				states[x + y * stateWidth] = new LightingState(x, y);
			}
		}

		if (executer != null) {
			executer.dispose();
		}
		this.executer = new LightingSwipeExecuter(stateWidth, stateHeight, lightingCores);

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

		startX = (int) (viewport.getX() / Tile.TILE_SIZE) - offScreenTiles;
		startY = (int) (viewport.getY() / Tile.TILE_SIZE) - offScreenTiles;

		executer.executeLightingPass(lightingPass1);

		executer.executeLightingPass(lightingPass2);

		// for (int x = 0; x < stateWidth; x++) {
		// for (int y = 0; y < stateHeight; y++) {
		// LightingState state = states[x][y];
		//
		// if (state.lit) {
		// int shineDist = 2 * resolution;
		//
		// for (int x0 = x - shineDist; x0 < x + shineDist; x0++) {
		// for (int y0 = y - shineDist; y0 < y + shineDist; y0++) {
		// if (x0 >= 0 && x0 < stateWidth && y0 >= 0 && y0 < stateHeight) {
		// LightingState s0 = states[x0][y0];
		//
		// if (!s0.lit) {
		// double dist0 = MathHelper.distance(x, y, s0.x, s0.y);
		//
		// float lit = 1.0f - (float) (Math.min(dist0, shineDist) / shineDist);
		// lit = MathHelper.ensureRange(lit, 0.0f, 1.0f);
		//
		// s0.r += state.r * lit;
		// s0.g += state.g * lit;
		// s0.b += state.b * lit;
		//
		// s0.r = MathHelper.ensureRange(s0.r, 0.0f, 1.0f);
		// s0.g = MathHelper.ensureRange(s0.g, 0.0f, 1.0f);
		// s0.b = MathHelper.ensureRange(s0.b, 0.0f, 1.0f);
		// }
		// }
		// }
		// }
		// }
		// }
		// }

		lightMap.setLightData(states);
	}

	public void dispose() {
		if (lightMap != null) {
			lightMap.dispose();
		}
	}

	public World getWorld() {
		return world;
	}

	public LightingState[] getStates() {
		return states;
	}

}