package com.rotef.game.world.entity.skin;

import java.io.BufferedReader;

import com.badlogic.gdx.files.FileHandle;
import com.rotef.game.Game;
import com.rotef.game.assets.Sprite;
import com.rotef.game.world.entity.Mob;
import com.rotef.game.world.entity.MobDirection;

public class MobSkin implements EntitySkin {

	private FileHandle skinFile;
	private Mob mob;

	private Sprite base;

	private Sprite currentSprite = null;

	public MobSkin(FileHandle skinFile, Mob mob) {
		this.skinFile = skinFile;
		this.mob = mob;

		load();
		update();
	}

	private void load() {
		try {
			BufferedReader reader = new BufferedReader(skinFile.reader());

			String line = null;
			while ((line = reader.readLine()) != null) {
				if (line.isEmpty()) {

				} else {
					String[] s = line.trim().split(":");
					assert s.length == 2;

					String param = s[0];
					String value = s[1];

					handleParam(param, value);
				}
			}

			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handleParam(String param, String value) {
		if (param.equals("base")) {
			this.base = Game.assets.getSprite(value);
		}
	}

	private void update() {
		this.currentSprite = base;
	}

	@Override
	public Sprite getSprite() {
		update();

		return currentSprite;
	}

	@Override
	public int getWidth() {
		return currentSprite.getWidth();
	}

	@Override
	public int getHeight() {
		return currentSprite.getHeight();
	}

	public Mob getMob() {
		return mob;
	}

	@Override
	public boolean mirrorX() {
		return mob.getDirection() == MobDirection.Left;
	}

	@Override
	public boolean mirrorY() {
		return false;
	}

}
