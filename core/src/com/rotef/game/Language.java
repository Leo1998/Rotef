package com.rotef.game;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;

public class Language implements Serializable {

	public static Language load(FileHandle languageFile) {
		Json json = new Json();

		return json.fromJson(Language.class, languageFile);
	}

	private final ObjectMap<String, String> languageMap;

	public Language() {
		this.languageMap = new ObjectMap<String, String>();
	}

	public boolean has(String key) {
		return languageMap.containsKey(key);
	}

	public String get(String key) {
		if (has(key)) {
			return languageMap.get(key);
		}

		return "Unknown String";
	}

	@Override
	public String toString() {
		return languageMap.toString();
	}

	@Override
	public void write(Json json) {
		ObjectMap.Entries<String, String> entries = languageMap.entries();

		while (entries.hasNext()) {
			ObjectMap.Entry<String, String> entry = entries.next();

			String key = entry.key;
			String value = entry.value;

			json.writeValue(key, value);
		}
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		for (JsonValue object : jsonData) {
			String key = object.name;
			String value = null;

			if (object.isString()) {
				value = object.asString();
			} else {
				value = "Unknown String";
			}

			languageMap.put(key, value);
		}
	}

}
