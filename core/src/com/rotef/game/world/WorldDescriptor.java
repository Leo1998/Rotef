package com.rotef.game.world;

import java.io.File;
import java.io.FileFilter;

import com.badlogic.gdx.files.FileHandle;
import com.rotef.game.Game;

public class WorldDescriptor {

	public static WorldDescriptor[] getExistingWorlds() {
		FileHandle[] files = Game.worldDir.list(new FileFilter() {
			@Override
			public boolean accept(File file) {
				if (file.isDirectory()) {
					return true;
				}

				return false;
			}
		});

		WorldDescriptor[] result = new WorldDescriptor[files.length];

		for (int i = 0; i < files.length; i++) {
			result[i] = new WorldDescriptor(files[i].name());
		}

		return result;
	}

	private final String name;

	public WorldDescriptor(String name) {
		this.name = name;
	}

	public FileHandle getDirectory() {
		return Game.worldDir.child(getName());
	}

	public void delete() {
		if (Game.config.isUsePastebin()) {
			getDirectory().copyTo(Game.pastebinDir.child(name));
		}
		getDirectory().deleteDirectory();
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "WorldDescriptor [name=" + name + "]";
	}

}
