package com.rotef.game.chat;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class ChatMessageSerializer extends Serializer<ChatMessage> {

	@Override
	public void write(Kryo kryo, Output output, ChatMessage object) {
		output.writeString(object.getSender());
		output.writeString(object.getMessage());
	}

	@Override
	public ChatMessage read(Kryo kryo, Input input, Class<ChatMessage> type) {
		String sender = input.readString();
		String message = input.readString();

		return new ChatMessage(sender, message);
	}

}
