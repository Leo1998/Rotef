package com.rotef.game.template;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

public class Template {

	public final JsonValue map;

	public Template(FileHandle file) {
		this.map = new JsonReader().parse(file);
	}

	public void save(FileHandle file) {
		String jsonText = map.prettyPrint(OutputType.json, 0);

		file.writeString(jsonText, false);
	}

	public static Color getColor(JsonValue val) {
		if (val == null || !val.isArray())
			return Color.BLACK;

		float[] array = val.asFloatArray();

		if (array.length == 3) {
			return new Color(array[0], array[1], array[2], 1.0f);
		} else if (array.length == 4) {
			return new Color(array[0], array[1], array[2], array[3]);
		} else {
			throw new IllegalStateException("JsonValue is not assignable as a Color!");
		}
	}

}
