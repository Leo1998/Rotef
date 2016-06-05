package com.rotef.game.world.loader;

import com.badlogic.gdx.utils.Array;
import com.rotef.game.chat.ChatMessage;

public final class WorldData {

	public String version;
	public int width;
	public int height;
	public float time;
	public Array<ChatMessage> chatMessages = new Array<>();

	@Override
	public String toString() {
		return "WorldData [version=" + version + ", width=" + width + ", height=" + height + ", time=" + time + ", chatMessages(" + chatMessages.size + ")" + "]";
	}

}
