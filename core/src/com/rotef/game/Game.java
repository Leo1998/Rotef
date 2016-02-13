package com.rotef.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.rotef.game.assets.Assets;
import com.rotef.game.renderer.WorldScreen;
import com.rotef.game.ui.ErrorScreen;
import com.rotef.game.ui.LoadingScreen;
import com.rotef.game.ui.MainMenuScreen;
import com.rotef.game.ui.UI;
import com.rotef.game.util.StatusListener;
import com.rotef.game.world.World;
import com.rotef.game.world.WorldDescriptor;
import com.rotef.game.world.WorldLoadingException;

public class Game extends com.badlogic.gdx.Game {

	public static Game game;
	public static Assets assets;
	public static Config config;

	public static FileHandle rootDir;
	public static FileHandle worldRoot;

	public static void openWorld(String name) {
		final WorldDescriptor descriptor = new WorldDescriptor(name);

		final LoadingScreen loadingScreen = new LoadingScreen();
		game.setScreen(loadingScreen);

		Thread loadingThread = new Thread(new Runnable() {
			@Override
			public void run() {
				StatusListener listener = new StatusListener() {
					@Override
					public void status(String msg, float progress) {
						loadingScreen.sendStatus(msg, progress);
					}
				};

				World world;
				try {
					world = new World(descriptor, listener);
				} catch (WorldLoadingException e) {
					game.setScreen(new ErrorScreen(e));
					return;
				}

				final WorldScreen screen = new WorldScreen(world);
				world.setScreen(screen);

				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						game.setScreen(screen);
					}
				});
			}
		}, "Loading Thread");

		loadingThread.setDaemon(true);
		loadingThread.setPriority(Thread.MAX_PRIORITY);

		loadingThread.start();
	}

	@Override
	public void create() {
		Gdx.app.log("Game", "Bootstrapping...");
		Gdx.app.log("Game", new GPUInfo(Gdx.gl).toString());

		init(Gdx.files.local("save"));

		assets = new Assets();
		assets.loadFromAssetFile(Gdx.files.internal("assets.dat"));

		UI.load();

		game = this;
		ShaderProgram.pedantic = false;

		this.setScreen(new MainMenuScreen());
	}

	@Override
	public void resize(int width, int height) {
		config.setResolution(width, height);

		super.resize(width, height);
	}

	@Override
	public void dispose() {
		super.dispose();

		saveConfig();
	}

	@Override
	public void render() {
		super.render();
	}

	public void init(FileHandle rootDirParam) {
		rootDir = rootDirParam;

		worldRoot = rootDir.child("worlds");
		worldRoot.mkdirs();

		FileHandle configFile = rootDir.child("config.dat");

		config = new Config(true);
		if (configFile.exists()) {
			config.load(configFile);
		} else {
			// create the file at first
			config.save(configFile);
		}

		applyConfig();
	}

	private static void saveConfig() {
		FileHandle configFile = rootDir.child("config.dat");
		config.save(configFile);
	}

	public static void applyConfig() {
		Gdx.graphics.setVSync(config.isVSync());

		if (!config.isFullscreen()) {
			Gdx.graphics.setDisplayMode(config.getWidth(), config.getHeight(), false);
		} else {
			Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode().width, Gdx.graphics.getDesktopDisplayMode().height, true);
		}

		saveConfig();
	}

	public static void autoSave() {
		Gdx.app.log("Game", "Autosaving Configuration");

		saveConfig();
	}
}
