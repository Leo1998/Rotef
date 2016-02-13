package com.rotef.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.rotef.game.Game;

public class DesktopLauncher {

	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = 800;
		config.height = 480;
		config.title = "Rise Of The Endless Fire";

		config.foregroundFPS = 0;
		config.backgroundFPS = 0;

		new LwjglApplication(new Game(), config);
	}

}
