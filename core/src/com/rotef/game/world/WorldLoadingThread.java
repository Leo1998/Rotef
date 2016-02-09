package com.rotef.game.world;

import com.rotef.game.renderer.WorldScreen;
import com.rotef.game.ui.ErrorScreen;
import com.rotef.game.util.LoadingThread;

public class WorldLoadingThread extends LoadingThread {

	private WorldDescriptor descriptor;

	public WorldLoadingThread(WorldDescriptor descriptor) {
		this.descriptor = descriptor;
	}

	@Override
	protected Object runProcess() {
		try {
			final World world = new World(descriptor, this);

			WorldScreen screen = new WorldScreen(world);
			world.setScreen(screen);

			return screen;
		} catch (WorldLoadingException e) {
			return new ErrorScreen(e);
		}
	}

}
