package com.rotef.game.chat;

import com.badlogic.gdx.utils.Array;
import com.rotef.game.world.World;

public class ChatManager {

	private Array<ChatMessage> chatMessages;
	private Array<ChatListener> chatListeners;

	private final World world;

	public ChatManager(World world) {
		this.world = world;

		this.chatMessages = new Array<>();
		this.chatListeners = new Array<>();
	}

	public void send(ChatMessage message) {
		chatMessages.add(message);

		for (ChatListener listener : chatListeners) {
			listener.onReceive(message);
		}
	}

	public Array<ChatMessage> getChatMessages() {
		return chatMessages;
	}

	public void addChatListener(ChatListener listener) {
		chatListeners.add(listener);
	}

	public void removeChatListener(ChatListener listener) {
		chatListeners.removeValue(listener, true);
	}

	public Array<ChatListener> getChatListeners() {
		return chatListeners;
	}

	public World getWorld() {
		return world;
	}

}
