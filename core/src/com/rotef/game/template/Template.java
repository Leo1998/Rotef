package com.rotef.game.template;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.SerializationException;

public class Template implements Serializable {

	public static Template loadTemplate(FileHandle file) {
		Json json = new Json(OutputType.json);

		return json.fromJson(Template.class, file);
	}

	public static void saveTemplate(Template template, FileHandle file) {
		Json json = new Json(OutputType.json);

		String jsonText = json.prettyPrint(template);

		file.writeString(jsonText, false);
	}

	private ObjectMap<String, Object> map;

	public Template() {
		this.map = new ObjectMap<String, Object>();
	}

	public boolean has(String key) {
		return map.containsKey(key);
	}

	public Object get(String key) {
		return has(key) ? map.get(key) : null;
	}

	public String getString(String key) {
		Object object = get(key);
		if (object != null && object instanceof String) {
			return (String) object;
		} else {
			throw new IllegalArgumentException("The Object is not a String!");
		}
	}

	public boolean getBoolean(String key) {
		Object object = get(key);
		if (object != null && object instanceof Boolean) {
			return (boolean) object;
		} else {
			throw new IllegalArgumentException("The Object is not a Boolean!");
		}
	}

	public byte getByte(String key) {
		Object object = get(key);
		if (object != null && (object instanceof Long || object instanceof Double)) {
			return (byte) object;
		} else {
			throw new IllegalArgumentException("The Object is not a Byte!");
		}
	}

	public char getCharacter(String key) {
		Object object = get(key);
		if (object != null && (object instanceof Long || object instanceof Double)) {
			return (char) object;
		} else {
			throw new IllegalArgumentException("The Object is not a Character!");
		}
	}

	public short getShort(String key) {
		Object object = get(key);
		if (object != null && (object instanceof Long || object instanceof Double)) {
			return ((Long) object).shortValue();
		} else {
			throw new IllegalArgumentException("The Object is not a Short!");
		}
	}

	public int getInteger(String key) {
		Object object = get(key);
		if (object != null && (object instanceof Long || object instanceof Double)) {
			return ((Long) object).intValue();
		} else {
			throw new IllegalArgumentException("The Object is not a Integer!");
		}
	}

	public long getLong(String key) {
		Object object = get(key);
		if (object != null && (object instanceof Long || object instanceof Double)) {
			return (long) object;
		} else {
			throw new IllegalArgumentException("The Object is not a Long!");
		}
	}

	public float getFloat(String key) {
		Object object = get(key);
		if (object != null && (object instanceof Long || object instanceof Double)) {
			return ((Double) object).floatValue();
		} else {
			throw new IllegalArgumentException("The Object is not a Float!");
		}
	}

	public double getDouble(String key) {
		Object object = get(key);
		if (object != null && (object instanceof Long || object instanceof Double)) {
			return (double) object;
		} else {
			throw new IllegalArgumentException("The Object is not a Double!");
		}
	}

	@Override
	public String toString() {
		return map.toString();
	}

	@Override
	public void write(Json json) {
		for (Object object : map) {
			json.writeValue(object);
		}
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		read(jsonData);
	}

	private void read(JsonValue root) {
		for (JsonValue object : root) {
			String key = object.name;
			Object out = null;

			if (object.isObject()) {
				// nothing
			} else if (object.isArray()) {
				// read(object);
			} else if (object.isString()) {
				out = object.asString();
			} else if (object.isDouble()) {
				double doubleValue = object.asDouble();
				long longValue = object.asLong();
				out = doubleValue == longValue ? longValue : doubleValue;
			} else if (object.isLong()) {
				out = object.asLong();
			} else if (object.isBoolean()) {
				out = object.asBoolean();
			} else if (object.isNull()) {
				out = null;
			} else {
				throw new SerializationException("Unknown object type: " + object);
			}

			map.put(key, out);
		}
	}

}
