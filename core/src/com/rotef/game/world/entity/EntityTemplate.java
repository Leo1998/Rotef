package com.rotef.game.world.entity;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

public final class EntityTemplate implements Serializable {

	public static EntityTemplate loadTemplate(FileHandle file) {
		Json json = new Json(OutputType.json);

		return json.fromJson(EntityTemplate.class, file);
	}

	public static void saveTemplate(EntityTemplate template, FileHandle file) {
		Json json = new Json(OutputType.json);

		String jsonText = json.prettyPrint(template);

		file.writeString(jsonText, false);
	}

	private Entity.Type type;
	private String name;
	private String spritePath;

	public EntityTemplate() {
	}

	@Override
	public void write(Json json) {
		json.writeValue("type", type);
		json.writeValue("name", name);
		json.writeValue("spritePath", spritePath);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		this.type = json.readValue("type", Entity.Type.class, jsonData);
		this.name = jsonData.get("name").asString();
		this.spritePath = jsonData.get("spritePath").asString();
	}

	public Entity.Type getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public String getSpritePath() {
		return spritePath;
	}

}
