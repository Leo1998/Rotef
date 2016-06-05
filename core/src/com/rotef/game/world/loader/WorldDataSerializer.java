package com.rotef.game.world.loader;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.rotef.game.chat.ChatMessage;

public class WorldDataSerializer extends Serializer<WorldData> {

	@Override
	public void write(Kryo kryo, Output output, WorldData o) {
		output.writeString(o.version);
		output.writeInt(o.width);
		output.writeInt(o.height);
		output.writeFloat(o.time);

		output.writeInt(o.chatMessages.size);
		for (ChatMessage chatMessage : o.chatMessages) {
			kryo.writeObject(output, chatMessage);
		}
	}

	@Override
	public WorldData read(Kryo kryo, Input input, Class<WorldData> type) {
		WorldData data = new WorldData();

		data.version = input.readString();
		data.width = input.readInt();
		data.height = input.readInt();
		data.time = input.readFloat();

		int l = input.readInt();
		for (int i = 0; i < l; i++) {
			data.chatMessages.add(kryo.readObject(input, ChatMessage.class));
		}

		return data;
	}

}
