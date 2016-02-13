package com.rotef.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.rotef.game.renderer.WorldScreen;
import com.rotef.game.renderer.WorldViewport;
import com.rotef.game.util.StatusListener;
import com.rotef.game.util.file.DFAException;
import com.rotef.game.world.entity.EntityManager;
import com.rotef.game.world.entity.Player;
import com.rotef.game.world.entity.PlayerController;
import com.rotef.game.world.generator.Generator;
import com.rotef.game.world.generator.GeneratorException;
import com.rotef.game.world.generator.OverworldGenerator;
import com.rotef.game.world.light.Light;
import com.rotef.game.world.light.LightManager;
import com.rotef.game.world.physics.PhysicsManager;
import com.rotef.game.world.tile.Tile;
import com.rotef.game.world.tile.TileRegister;

public class World {

	public static final int WORLD_NAME_MAX_LENGTH = 32;

	private Array<WorldChunk> loadedChunks = new Array<WorldChunk>();
	private WorldChunk[] chunks;

	private final StatusListener listener;

	private int width;
	private int height;
	private WorldHeightmap heightmap;

	private WorldScreen screen;

	private final WorldDescriptor descriptor;
	final ChunkLoader chunkLoader;
	final TileRegister tileRegister;
	final EntityManager entityManager;
	final PhysicsManager physicsManager;
	final LightManager lightManager;
	final TimeManager timeManager;

	private Player player;

	public World(WorldDescriptor descriptor, StatusListener listener) throws WorldLoadingException {
		this.descriptor = descriptor;
		this.listener = listener;

		try {
			tileRegister = new TileRegister();
			entityManager = new EntityManager(this);
			physicsManager = new PhysicsManager(this);
			lightManager = new LightManager(this);
			timeManager = new TimeManager(this);

			chunkLoader = new ChunkLoader(this);
			if (chunkLoader.isFirstInit()) {
				this.width = 1024;
				this.height = 256;
				this.chunks = new WorldChunk[(width / WorldChunk.CHUNK_SIZE) * (height / WorldChunk.CHUNK_SIZE)];
				this.heightmap = new WorldHeightmap(width);

				chunkLoader.initialize();

				generate();
			} else {
				chunkLoader.initialize();// nothing happens here, but don't care
											// about it...

				this.width = chunkLoader.readWorldWidth();
				this.height = chunkLoader.readWorldHeight();
				this.chunks = new WorldChunk[(width / WorldChunk.CHUNK_SIZE) * (height / WorldChunk.CHUNK_SIZE)];
				this.heightmap = chunkLoader.readWorldHeightmap();
			}

			physicsManager.initialize();

			int spawnX = (width / 4);
			int spawnY = (getHighestTileAt(width / 2) + 5) / 2;
			lightManager.addLight(new Light(spawnX, spawnY, 6, Color.WHITE));

			player = (Player) entityManager.spawnEntity(1, spawnX, spawnY);
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
		int[] map = generator.generateMap(width, height);

		Gdx.app.log("World", "generating chunks...");
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
					loadChunk(chunk, true);

					float progress = ((float) loadedChunks / (float) ((width / WorldChunk.CHUNK_SIZE) * (height / WorldChunk.CHUNK_SIZE)));
					listener.status("Generating Chunks...", progress);
					loadedChunks++;
				}
			}
		}

		// generate heightmap
		for (int x = 0; x < width; x++) {
			int h = 0;
			for (int y = height - 1; y > 0; y--) {
				if (getTile(x, y, false) != null) {
					h = y;
					break;
				}
			}

			heightmap.setHeight(x, h);
		}

		unloadAllLoadedChunks(true, true);
		saveHeightmap();
	}

	public void update(float delta) {
		timeManager.update(delta);

		updateChunks();

		for (int chunkX = 0; chunkX < width / WorldChunk.CHUNK_SIZE; chunkX++) {
			for (int chunkY = 0; chunkY < height / WorldChunk.CHUNK_SIZE; chunkY++) {
				WorldChunk chunk = getChunk(chunkX, chunkY);
				if (chunk != null) {
					chunk.update(delta);
				}
			}
		}

		entityManager.update(delta);
	}

	public void updateAfterRender(float delta) {
		physicsManager.updatePhysics(delta);
	}

	public WorldChunk loadChunk(int chunkX, int chunkY) {
		return loadChunk(chunkX, chunkY, false);
	}

	public WorldChunk loadChunk(int chunkX, int chunkY, boolean skipPhysics) {
		try {
			WorldChunkData chunkData = chunkLoader.readChunk(chunkX, chunkY);

			WorldChunk chunk = new WorldChunk(chunkX, chunkY, this, chunkData);

			return loadChunk(chunk, skipPhysics);
		} catch (DFAException e) {
			return null;
		} catch (InvalidChunkDataException e) {
			return null;
		}
	}

	private WorldChunk loadChunk(WorldChunk chunk, boolean skipLogics) {
		chunks[chunk.getChunkX() + chunk.getChunkY() * (width / WorldChunk.CHUNK_SIZE)] = chunk;
		loadedChunks.add(chunk);

		if (!skipLogics) {
			physicsManager.onChunkLoaded(chunk);
		}

		return chunk;
	}

	public boolean isChunkLoaded(int chunkX, int chunkY) {
		return getChunk(chunkX, chunkY) != null;
	}

	public WorldChunk getChunk(int chunkX, int chunkY) {
		if (!chunkLoader.hasChunk(chunkX, chunkY)) {
			return null;
		}

		return chunks[chunkX + chunkY * (width / WorldChunk.CHUNK_SIZE)];
	}

	/**
	 * This unloads a chunk.
	 * 
	 * @param chunkX
	 * @param chunkY
	 */
	public void unloadChunk(int chunkX, int chunkY) {
		WorldChunk chunk = getChunk(chunkX, chunkY);

		unloadChunk(chunk, true, false);
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
	private void unloadChunk(WorldChunk chunk, boolean save, boolean skipLogics) {
		if (save) {
			chunk.save();
		}

		chunks[chunk.getChunkX() + chunk.getChunkY() * (width / WorldChunk.CHUNK_SIZE)] = null;
		loadedChunks.removeValue(chunk, true);

		if (!skipLogics) {
			physicsManager.onChunkUnloaded(chunk);
		}
	}

	/**
	 * Only use with care! It might block for a longer time.
	 * 
	 * @param save
	 * @param skipPhysics
	 */
	private void unloadAllLoadedChunks(boolean save, boolean skipPhysics) {
		for (int i = 0; i < loadedChunks.size; i++) {
			WorldChunk chunk = loadedChunks.get(i);

			unloadChunk(chunk, save, skipPhysics);
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
		Array<WorldChunk> nessessaryChunks = new Array<WorldChunk>();

		for (int xi = chunkX0; xi < chunkX1; xi++) {
			for (int yi = chunkY0; yi < chunkY1; yi++) {
				if (chunkLoader.hasChunk(xi, yi)) {
					if (!isChunkLoaded(xi, yi)) {
						WorldChunk chunk = loadChunk(xi, yi);

						nessessaryChunks.add(chunk);
					} else {
						nessessaryChunks.add(getChunk(xi, yi));
					}
				}
			}
		}

		// close unnessessary Chunks
		for (int i = 0; i < loadedChunks.size; i++) {
			WorldChunk chunk = loadedChunks.get(i);

			if (!nessessaryChunks.contains(chunk, true)) {
				unloadChunk(chunk.getChunkX(), chunk.getChunkY());
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
		if (yTile > heightmap.getHeight(xTile)) {
			heightmap.setHeight(xTile, yTile);
		}

		physicsManager.onTileUpdate(chunk, oldTile, newTile, xTile, yTile);
	}

	public Tile getTile(int xTile, int yTile, boolean loadChunkIfNessessary) {
		if (xTile < 0 || xTile >= width || yTile < 0 || yTile >= height) {
			return null;
		}

		int chunkX = xTile / WorldChunk.CHUNK_SIZE;
		int chunkY = yTile / WorldChunk.CHUNK_SIZE;

		WorldChunk chunk = getChunk(chunkX, chunkY);

		if (chunk == null) {
			if (loadChunkIfNessessary) {
				chunk = loadChunk(chunkX, chunkY);
				if (chunk == null) {
					return null;
				}
			} else {
				return null;
			}
		}

		return chunk.getTile(xTile - chunkX * WorldChunk.CHUNK_SIZE, yTile - chunkY * WorldChunk.CHUNK_SIZE);
	}

	public int getHighestTileAt(int xTile) {
		return heightmap.getHeight(xTile);
	}

	public void requestSave() {
		Gdx.app.log("World", "save requested!");

		save();
	}

	public void dispose(boolean error) {
		if (!error) {
			save();

			// chunks are allready saved
			unloadAllLoadedChunks(false, false);
		}

		if (chunkLoader != null)
			chunkLoader.dispose();

		if (physicsManager != null)
			physicsManager.dispose();

		if (lightManager != null)
			lightManager.dispose();

		Runtime.getRuntime().gc();

		Gdx.app.log("World", "disposed!");
	}

	private void save() {
		for (int i = 0; i < loadedChunks.size; i++) {
			WorldChunk chunk = loadedChunks.get(i);

			chunk.save();
		}

		saveHeightmap();

		Gdx.app.log("World", "saved!");
	}

	private void saveHeightmap() {
		try {
			chunkLoader.writeWorldHeightmap(heightmap);
		} catch (DFAException e) {
			e.printStackTrace();
		}
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

	public ChunkLoader getChunkLoader() {
		return chunkLoader;
	}

	public Array<WorldChunk> getLoadedChunks() {
		return loadedChunks;
	}

	public TileRegister getTileRegister() {
		return tileRegister;
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

	public WorldHeightmap getHeightmap() {
		return heightmap;
	}

	public TimeManager getTimeManager() {
		return timeManager;
	}

	public float getWorldTime() {
		return timeManager.getTotalWorldSeconds();
	}

}
