package com.rotef.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.rotef.game.ui.UI;
import com.rotef.game.world.World;

public class Debug {

	public static void renderDebug(SpriteBatch batch, World world) {
		String msg = "Debug" + "\n"//
				+ "FPS: " + Gdx.graphics.getFramesPerSecond() + "\n"//
				+ "Java Heap: " + Gdx.app.getJavaHeap() + "\n"//
				+ "Native Heap: " + Gdx.app.getNativeHeap() + "\n"//
				+ "Daytime: " + world.getTimeManager().getDaytime() + " (Time: " + world.getTimeManager().getTime() + ")" + "\n"//
				+ "Loaded Chunks: " + world.getLoadedChunks().size + "\n"//
				+ "Active Chunks: " + world.getActiveChunks().size + "\n"//
				+ "Lights: " + world.getLightManager().getLightCount() + ", Visible Lights: " + world.getLightManager().getVisibleLightCount() + "\n";//

		UI.renderString(batch, msg, 10, 7 * 15);
	}

}
