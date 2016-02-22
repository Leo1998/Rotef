package com.rotef.game;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

public class Config implements Json.Serializable {

	private int width;
	private int height;
	private boolean fullscreen;
	private boolean vSync;
	private int lightMapDownScale;
	private float uiSize;
	private boolean debug;

	public Config() {
		setWidth(800);
		setHeight(600);
		setFullscreen(false);
		setVSync(true);
		setLightMapDownScale(2);
		setUiSize(1.5f);
		setDebug(false);
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

	public int getLightMapDownScale() {
		return lightMapDownScale;
	}

	public void setLightMapDownScale(int lightMapDownScale) {
		this.lightMapDownScale = lightMapDownScale;
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

	public static Config load(FileHandle file) {
		return load(file.read());
	}

	public static Config load(InputStream in) {
		Json json = new Json(OutputType.json);

		return json.fromJson(Config.class, in);
	}

	public static void save(FileHandle file, Config config) {
		save(file.write(false), config);
	}

	public static void save(OutputStream out, Config config) {
		Json json = new Json(OutputType.json);

		JsonWriter writer = new JsonWriter(new OutputStreamWriter(out));

		json.toJson(config, writer);
	}

	@Override
	public void write(Json json) {
		json.writeValue("width", width);
		json.writeValue("height", height);
		json.writeValue("fullscreen", fullscreen);
		json.writeValue("vSync", vSync);
		json.writeValue("lightMapDownScale", lightMapDownScale);
		json.writeValue("uiSize", uiSize);
		json.writeValue("debug", debug);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		this.width = jsonData.get("width").asInt();
		this.height = jsonData.get("height").asInt();
		this.fullscreen = jsonData.get("fullscreen").asBoolean();
		this.vSync = jsonData.get("vSync").asBoolean();
		this.lightMapDownScale = jsonData.get("lightMapDownScale").asInt();
		this.uiSize = jsonData.get("uiSize").asFloat();
		this.debug = jsonData.get("debug").asBoolean();
	}

}
