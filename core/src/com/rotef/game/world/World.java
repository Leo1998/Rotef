package com.rotef.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.rotef.game.Game;
import com.rotef.game.Version;
import com.rotef.game.renderer.WorldScreen;
import com.rotef.game.renderer.WorldViewport;
import com.rotef.game.util.StatusListener;
import com.rotef.game.world.entity.EntityManager;
import com.rotef.game.world.entity.Player;
import com.rotef.game.world.entity.PlayerController;
import com.rotef.game.world.generator.Generator;
import com.rotef.game.world.generator.GeneratorException;
import com.rotef.game.world.generator.OverworldGenerator;
import com.rotef.game.world.light.LightManager;
import com.rotef.game.world.loader.WorldData;
import com.rotef.game.world.loader.WorldLoader;
import com.rotef.game.world.physics.PhysicsManager;
import com.rotef.game.world.tile.Tile;
import com.rotef.game.world.tile.TileManager;

public class World {

	private final Array<WorldChunk> loadedChunks = new Array<WorldChunk>(4096);
	private final Array<WorldChunk> activeChunks = new Array<WorldChunk>(128);
	private final WorldChunk[] chunks;

	private final StatusListener listener;

	/**
	 * the width and height in tiles
	 */
	private final int width, height;
	private final int surface;
	private final WorldHeightmap heightmap;

	private WorldScreen screen;

	private final WorldDescriptor descriptor;

	// Managers
	final WorldLoader worldLoader;
	final TileManager tileManager;
	final EntityManager entityManager;
	final PhysicsManager physicsManager;
	final LightManager lightManager;
	final TimeManager timeManager;

	private Player player;

	public World(WorldDescriptor descriptor, StatusListener listener) throws WorldLoadingException {
		this.descriptor = descriptor;
		this.listener = listener;

		try {
			tileManager = new TileManager();
			entityManager = new EntityManager(this);
			physicsManager = new PhysicsManager(this);
			lightManager = new LightManager(this);
			timeManager = new TimeManager(this);

			worldLoader = new WorldLoader(this);
			if (worldLoader.isFirstInit()) {
				this.width = 2048;
				this.height = 256;
				this.surface = height / 2;
				this.chunks = new WorldChunk[(width / WorldChunk.CHUNK_SIZE) * (height / WorldChunk.CHUNK_SIZE)];

				generate();
			} else {
				WorldData worldData = worldLoader.readWorldData();

				this.width = worldData.width;
				this.height = worldData.height;
				this.surface = height / 2;
				this.chunks = new WorldChunk[(width / WorldChunk.CHUNK_SIZE) * (height / WorldChunk.CHUNK_SIZE)];
				this.timeManager.setTotalWorldSeconds(worldData.time);

				Array<WorldChunk> chunks = worldLoader.readChunkData();
				for (int i = 0; i < chunks.size; i++) {
					WorldChunk chunk = chunks.get(i);

					loadChunk(chunk);

					float progress = ((float) loadedChunks.size / (float) ((width / WorldChunk.CHUNK_SIZE) * (height / WorldChunk.CHUNK_SIZE)));
					listener.status(Game.language.get("loadingChunksMessage"), progress);
				}
			}

			heightmap = new WorldHeightmap(width);
			for (int x = 0; x < width; x++) {
				updateHeightmap(x);
			}

			int spawnX = (width / 4);
			int spawnY = (getHighestTileAt(width / 2) + 5) / 2;

			player = (Player) entityManager.spawnEntity("Player", spawnX, spawnY);
			player.addTask(new PlayerController(player));
		} catch (Exception e) {
			dispose(true);
			e.printStackTrace();
			throw new WorldLoadingException("An Exception occured while loading world: " + descriptor, e);
		}
	}

	private void generate() throws GeneratorException {
		Gdx.app.log("World", "generating...");

		Generator generator = Generator.createGeneratorInstance(OverworldGenerator.class, new StatusListener() {
			@Override
			public void status(String msg, float progress) {
				listener.status(msg, progress);
			}
		});
		int[] map = generator.generateMap(width, height, surface);

		{
			int loadedChunks = 0;
			for (int chunkX = 0; chunkX < width / WorldChunk.CHUNK_SIZE; chunkX++) {
				for (int chunkY = 0; chunkY < height / WorldChunk.CHUNK_SIZE; chunkY++) {
					int[] subData = new int[WorldChunk.CHUNK_SIZE * WorldChunk.CHUNK_SIZE];

					for (int x = 0; x < WorldChunk.CHUNK_SIZE; x++) {
						for (int y = 0; y < WorldChunk.CHUNK_SIZE; y++) {
							int totalX = chunkX * WorldChunk.CHUNK_SIZE + x;
							int totalY = chunkY * WorldChunk.CHUNK_SIZE + y;

							subData[x + y * WorldChunk.CHUNK_SIZE] = map[totalX + totalY * width];
						}
					}

					WorldChunk chunk = new WorldChunk(chunkX, chunkY, this, new RawChunkData(subData));
					loadChunk(chunk);

					float progress = ((float) loadedChunks / (float) ((width / WorldChunk.CHUNK_SIZE) * (height / WorldChunk.CHUNK_SIZE)));
					listener.status(Game.language.get("generatingChunksMessage"), progress);
					loadedChunks++;
				}
			}
		}
	}

	public void update(float delta) {
		timeManager.update(delta);

		updateChunks();

		for (WorldChunk chunk : activeChunks) {
			chunk.update(delta);
		}

		entityManager.update(delta);
	}

	public void updateAfterRender(float delta) {
		physicsManager.updatePhysics(delta);
	}

	private WorldChunk loadChunk(WorldChunk chunk) {
		chunks[chunk.getChunkX() + chunk.getChunkY() * (width / WorldChunk.CHUNK_SIZE)] = chunk;
		loadedChunks.add(chunk);

		return chunk;
	}

	public boolean isChunkLoaded(int chunkX, int chunkY) {
		return getChunk(chunkX, chunkY) != null;
	}

	public WorldChunk getChunk(int chunkX, int chunkY) {
		if (!(chunkX >= 0 && chunkY >= 0 && chunkX < width / WorldChunk.CHUNK_SIZE && chunkY < height / WorldChunk.CHUNK_SIZE)) {
			return null;
		}

		return chunks[chunkX + chunkY * (width / WorldChunk.CHUNK_SIZE)];
	}

	/**
	 * This unloads the given chunk.
	 * 
	 * @param chunk
	 *            the chunk
	 * @param save
	 *            if the chunk should be saved
	 * @param skipLogics
	 *            if logics should be skipped
	 */
	private void unloadChunk(WorldChunk chunk, boolean save) {
		if (save) {
			chunk.save();
		}

		chunks[chunk.getChunkX() + chunk.getChunkY() * (width / WorldChunk.CHUNK_SIZE)] = null;
		loadedChunks.removeValue(chunk, true);
	}

	/**
	 * Only use with care! It might block for a longer time.
	 * 
	 * @param save
	 * @param skipPhysics
	 */
	private void unloadAllLoadedChunks(boolean save) {
		for (int i = 0; i < loadedChunks.size; i++) {
			WorldChunk chunk = loadedChunks.get(i);

			unloadChunk(chunk, save);
		}

		loadedChunks.clear();
		for (int i = 0; i < chunks.length; i++) {
			chunks[i] = null;
		}
	}

	private void updateChunks() {
		WorldViewport viewport = screen.getViewport();
		float x0 = viewport.getX();
		float y0 = viewport.getY();
		float x1 = x0 + viewport.getWidth();
		float y1 = y0 + viewport.getHeight();
		int chunkX0 = (int) (((x0 / Tile.TILE_SIZE) - 1) / WorldChunk.CHUNK_SIZE) - 2;
		int chunkY0 = (int) (((y0 / Tile.TILE_SIZE) - 1) / WorldChunk.CHUNK_SIZE) - 2;
		int chunkX1 = (int) (((x1 / Tile.TILE_SIZE) + 1) / WorldChunk.CHUNK_SIZE) + 2;
		int chunkY1 = (int) (((y1 / Tile.TILE_SIZE) + 1) / WorldChunk.CHUNK_SIZE) + 2;

		Array<WorldChunk> oldActiveChunks = new Array<WorldChunk>();
		oldActiveChunks.addAll(activeChunks);
		activeChunks.clear();

		for (int xi = chunkX0; xi < chunkX1; xi++) {
			for (int yi = chunkY0; yi < chunkY1; yi++) {
				if (xi >= 0 && yi >= 0 && xi < width / WorldChunk.CHUNK_SIZE && yi < height / WorldChunk.CHUNK_SIZE) {
					if (isChunkLoaded(xi, yi)) {
						activeChunks.add(getChunk(xi, yi));
					}
				}
			}
		}

		for (WorldChunk chunk : oldActiveChunks) {
			if (activeChunks.contains(chunk, true)) {
				if (!chunk.isActive()) {
					chunk.setActive(true);
				}
			} else {
				if (chunk.isActive()) {
					chunk.setActive(false);
				}
			}
		}

	}

	/**
	 * Called when a Tile is changed!
	 * 
	 * @param chunk
	 * @param tile
	 * @param xTile
	 * @param yTile
	 */
	void onTileUpdate(WorldChunk chunk, Tile oldTile, Tile newTile, int xTile, int yTile) {
		updateHeightmap(xTile);
	}

	public Tile getTile(int xTile, int yTile) {
		if (xTile < 0 || xTile >= width || yTile < 0 || yTile >= height) {
			return null;
		}

		int chunkX = xTile / WorldChunk.CHUNK_SIZE;
		int chunkY = yTile / WorldChunk.CHUNK_SIZE;

		WorldChunk chunk = getChunk(chunkX, chunkY);

		if (chunk == null) {
			return null;
		}

		return chunk.getTile(xTile - chunkX * WorldChunk.CHUNK_SIZE, yTile - chunkY * WorldChunk.CHUNK_SIZE);
	}

	public void setTile(int xTile, int yTile, int id) {
		if (xTile < 0 || xTile >= width || yTile < 0 || yTile >= height) {
			return;
		}

		int chunkX = xTile / WorldChunk.CHUNK_SIZE;
		int chunkY = yTile / WorldChunk.CHUNK_SIZE;

		WorldChunk chunk = getChunk(chunkX, chunkY);

		if (chunk == null) {
			return;
		}

		Tile tile = tileManager.createTile(id, this, xTile, yTile);

		chunk.setTile(xTile - chunkX * WorldChunk.CHUNK_SIZE, yTile - chunkY * WorldChunk.CHUNK_SIZE, tile);
	}

	public int getHighestTileAt(int xTile) {
		return heightmap.getHeight(xTile);
	}

	public void requestSave() {
		Gdx.app.log("World", "save requested!");

		save(false);
	}

	public void dispose(boolean error) {
		if (!error) {
			save(true);

			// chunks are allready saved
			unloadAllLoadedChunks(false);
		}

		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				if (physicsManager != null)
					physicsManager.dispose();

				if (lightManager != null)
					lightManager.dispose();
			}
		});

		Runtime.getRuntime().gc();

		Gdx.app.log("World", "disposed!");
	}

	private void save(boolean wait) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				worldLoader.writeWorldData(createWorldData());

				worldLoader.writeChunkData(loadedChunks);

				Gdx.app.log("World", "saved!");
			}
		}, "World Save Thread");

		thread.setDaemon(true);
		thread.start();

		if (wait) {
			try {
				thread.join();
			} catch (InterruptedException e) {
			}
		}
	}

	private WorldData createWorldData() {
		WorldData data = new WorldData();

		data.version = Version.gameVersion;
		data.width = this.width;
		data.height = this.height;
		data.time = this.timeManager.getTotalWorldSeconds();

		return data;
	}

	public WorldDescriptor getDescriptor() {
		return descriptor;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public PhysicsManager getPhysicsManager() {
		return physicsManager;
	}

	public LightManager getLightManager() {
		return lightManager;
	}

	public WorldScreen getScreen() {
		return screen;
	}

	public void setScreen(WorldScreen screen) {
		this.screen = screen;
	}

	public WorldLoader getWorldLoader() {
		return worldLoader;
	}

	public Array<WorldChunk> getLoadedChunks() {
		return loadedChunks;
	}

	public Array<WorldChunk> getActiveChunks() {
		return activeChunks;
	}

	public TileManager getTileManager() {
		return tileManager;
	}

	public Player getPlayer() {
		return player;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getSurface() {
		return surface;
	}

	public WorldHeightmap getHeightmap() {
		return heightmap;
	}

	private void updateHeightmap(int x) {
		int h = 0;
		for (int y = height - 1; y > 0; y--) {
			if (getTile(x, y) != null) {
				h = y;
				break;
			}
		}

		heightmap.setHeight(x, h);
	}

	public TimeManager getTimeManager() {
		return timeManager;
	}

	public float getWorldTime() {
		return timeManager.getTotalWorldSeconds();
	}

}
