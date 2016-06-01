package com.rotef.game;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.rotef.game.world.light.LightingMode;

public class Config {

	private int width;
	private int height;
	private boolean fullscreen;
	private boolean vSync;
	private float uiSize;
	private boolean debug;
	private boolean usePastebin;
	private String languageFile;
	private LightingMode lightingMode;
	private int lightingCores;

	public Config() {
		setWidth(800);
		setHeight(600);
		setFullscreen(false);
		setVSync(true);
		setUiSize(1.2f);
		setDebug(false);
		setUsePastebin(true);
		setLanguageFile("en_US.lang");
		setLightingMode(LightingMode.High);
		setLightingCores(Game.enviroment.availableProcessors);
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public boolean isFullscreen() {
		return fullscreen;
	}

	public void setFullscreen(boolean fullscreen) {
		this.fullscreen = fullscreen;
	}

	public boolean isVSync() {
		return vSync;
	}

	public void setVSync(boolean vSync) {
		this.vSync = vSync;
	}

	public float getUiSize() {
		return uiSize;
	}

	public void setUiSize(float uiSize) {
		this.uiSize = uiSize;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public boolean isUsePastebin() {
		return usePastebin;
	}

	public void setUsePastebin(boolean usePastebin) {
		this.usePastebin = usePastebin;
	}

	public String getLanguageFile() {
		return languageFile;
	}

	public void setLanguageFile(String languageFile) {
		this.languageFile = languageFile;
	}

	public LightingMode getLightingMode() {
		return lightingMode;
	}

	public void setLightingMode(LightingMode lightingMode) {
		this.lightingMode = lightingMode;
	}

	public int getLightingCores() {
		return lightingCores;
	}

	public void setLightingCores(int lightingCores) {
		this.lightingCores = lightingCores;
	}

	public static Config load(FileHandle file) {
		Json json = new Json(OutputType.json);

		return json.fromJson(Config.class, file);
	}

	public static void save(FileHandle file, Config config) {
		Json json = new Json(OutputType.json);
		json.setUsePrototypes(false);

		String jsonText = json.prettyPrint(config);

		file.writeString(jsonText, false);
	}
}
