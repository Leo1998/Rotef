package com.rotef.game;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.badlogic.gdx.files.FileHandle;

public class Config {

	private Map<String, Object> map = new HashMap<String, Object>();

	public Config(boolean loadDefaults) {
		if (loadDefaults) {

			setWidth(800);
			setHeight(600);
			setFullscreen(false);
			setVSync(true);
			setLightMapDownScale(Game.isMobile ? 2 : 1);
			setUiSize(Game.isMobile ? 2.0f : 1.5f);
			setDebug(false);
		}
	}

	public void copyFrom(Config other) {
		getMap().putAll(other.getMap());
	}

	public void load(FileHandle file) {
		try {
			load(file.read());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void load(InputStream in) throws IOException {
		Properties props = new Properties();
		props.load(in);
		for (Map.Entry<Object, Object> entry : props.entrySet()) {
			String key = (String) entry.getKey();
			String val = (String) entry.getValue();
			if (val != null) {
				val = val.trim();
			}
			if (key.endsWith("(int)")) {
				key = key.substring(0, key.length() - 5);
				int iVal = Integer.parseInt(val);
				putInteger(key, iVal);
			} else if (key.endsWith("(string)")) {
				putString(key.substring(0, key.length() - 8), val);
			} else if (key.endsWith("(bool)")) {
				boolean bVal = Boolean.parseBoolean(val);
				putBoolean(key.substring(0, key.length() - 6), bVal);
			} else if (key.endsWith("(float)")) {
				float fVal = Float.parseFloat(val);
				putFloat(key.substring(0, key.length() - 7), fVal);
			} else {
				throw new IOException("Cannot parse key: " + key);
			}
		}
		in.close();
	}

	public void save(FileHandle file) {
		try {
			save(file.write(false));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void save(OutputStream out) throws IOException {
		Properties props = new Properties();
		for (Map.Entry<String, Object> entry : getMap().entrySet()) {
			Object val = entry.getValue();
			String type;
			if (val instanceof Integer) {
				type = "(int)";
			} else if (val instanceof String) {
				type = "(string)";
			} else if (val instanceof Boolean) {
				type = "(bool)";
			} else if (val instanceof Float) {
				type = "(float)";
			} else {
				// See the note in the Config.save(String)
				// method regarding object type settings.
				continue;
			}
			props.setProperty(entry.getKey() + type, val.toString());
		}
		props.store(out, "Config");
		out.close();
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public int getInteger(String key) {
		Integer i = (Integer) getMap().get(key);
		if (i == null) {
			return 0;
		}

		return i.intValue();
	}

	public boolean getBoolean(String key) {
		Boolean b = (Boolean) getMap().get(key);
		if (b == null) {
			return false;
		}

		return b.booleanValue();
	}

	public String getString(String key) {
		String s = (String) getMap().get(key);
		if (s == null) {
			return null;
		}

		return s;
	}

	public float getFloat(String key) {
		Float f = (Float) getMap().get(key);
		if (f == null) {
			return 0f;
		}

		return f.floatValue();
	}

	public void putInteger(String key, int value) {
		getMap().put(key, Integer.valueOf(value));
	}

	public void putBoolean(String key, boolean value) {
		getMap().put(key, Boolean.valueOf(value));
	}

	public void putString(String key, String value) {
		getMap().put(key, value);
	}

	public void putFloat(String key, float value) {
		getMap().put(key, Float.valueOf(value));
	}

	public int getWidth() {
		return getInteger("Width");
	}

	public void setWidth(int value) {
		putInteger("Width", value);
	}

	public int getHeight() {
		return getInteger("Height");
	}

	public void setHeight(int value) {
		putInteger("Height", value);
	}

	public void setResolution(int width, int height) {
		setWidth(width);
		setHeight(height);
	}

	public boolean isVSync() {
		return getBoolean("VSync");
	}

	public void setVSync(boolean value) {
		putBoolean("VSync", value);
	}

	public boolean isFullscreen() {
		return getBoolean("Fullscreen");
	}

	public void setFullscreen(boolean value) {
		putBoolean("Fullscreen", value);
	}

	public int getLightMapDownScale() {
		return getInteger("LightMapDownScale");
	}

	public void setLightMapDownScale(int value) {
		putInteger("LightMapDownScale", value);
	}

	public float getUiSize() {
		return getFloat("uiScale");
	}

	public void setUiSize(float value) {
		putFloat("uiScale", value);
	}

	public boolean isDebug() {
		return getBoolean("Debug");
	}

	public void setDebug(boolean value) {
		putBoolean("Debug", value);
	}

}
