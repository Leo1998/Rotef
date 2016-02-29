package com.rotef.game.util;

import com.badlogic.gdx.files.FileHandle;

public class FileUtils {

	private FileUtils() {
	}

	public static void createBackupFile(FileHandle file) {
		FileHandle backupFile = file.sibling(file.name() + ".bak");

		file.copyTo(backupFile);
	}

}
