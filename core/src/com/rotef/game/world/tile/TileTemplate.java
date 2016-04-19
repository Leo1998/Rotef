package com.rotef.game.world.tile;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

public final class TileTemplate implements Serializable {

	public static TileTemplate loadTemplate(FileHandle file) {
		Json json = new Json(OutputType.json);

		return json.fromJson(TileTemplate.class, file);
	}

	public static void saveTemplate(TileTemplate template, FileHandle file) {
		Json json = new Json(OutputType.json);

		String jsonText = json.prettyPrint(template);

		file.writeString(jsonText, false);
	}

	private int id;
	private Tile.Type type;
	private String name;
	private String spritePath;

	public TileTemplate() {
	}

	@Override
	public void write(Json json) {
		json.writeValue("id", id);
		json.writeValue("type", type);
		json.writeValue("name", name);
		json.writeValue("spritePath", spritePath);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		this.id = jsonData.get("id").asInt();
		this.type = json.readValue("type", Tile.Type.class, jsonData);
		this.name = jsonData.get("name").asString();
		this.spritePath = jsonData.get("spritePath").asString();
	}

	public int getId() {
		return id;
	}

	public Tile.Type getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public String getSpritePath() {
		return spritePath;
	}

}
