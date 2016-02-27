package com.rotef.game.world.loader;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class WorldDataSerializer extends Serializer<WorldData> {

	@Override
	public void write(Kryo kryo, Output output, WorldData o) {
		output.writeString(o.version);
		output.writeInt(o.width);
		output.writeInt(o.height);
		output.writeFloat(o.time);
	}

	@Override
	public WorldData read(Kryo kryo, Input input, Class<WorldData> type) {
		WorldData data = new WorldData();

		data.version = input.readString();
		data.width = input.readInt();
		data.height = input.readInt();
		data.time = input.readFloat();

		return data;
	}

}
