package com.rotef.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.rotef.game.renderer.WorldScreen;
import com.rotef.game.renderer.WorldViewport;
import com.rotef.game.world.entity.Player;
import com.rotef.game.world.physics.PhysicsManager;

public class WorldInput implements InputProcessor {

	private WorldScreen screen;

	private Vector2 miningPosition = new Vector2();
	private boolean mining = false;

	public WorldInput(WorldScreen screen) {
		this(screen, false);
	}

	public WorldInput(WorldScreen screen, boolean desktopTouchControlls) {
		this.screen = screen;
	}

	public void handlePlayer(Player player, float delta) {
		float xMove = 0;
		if (Gdx.input.isKeyPressed(Keys.A)) {
			xMove -= 1;
		}
		if (Gdx.input.isKeyPressed(Keys.D)) {
			xMove += 1;
		}

		player.walk(xMove);

		boolean jump = false;
		if (Gdx.input.isKeyPressed(Keys.W)) {
			jump = true;
		}

		if (jump) {
			player.jump();
		}

		boolean interact = false;
		if (Gdx.input.isKeyJustPressed(Keys.E)) {
			interact = true;
		}

		if (interact) {
		}

		if (mining) {
			WorldViewport viewport = screen.getViewport();

			float xs = miningPosition.x;
			float ys = -miningPosition.y + viewport.getHeight();

			xs += viewport.getX();
			ys += viewport.getY();

			float xt = (xs / PhysicsManager.PPM);
			float yt = (ys / PhysicsManager.PPM);

			player.mine(xt, yt);
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (pointer == 0 && button == Input.Buttons.LEFT) {
			miningPosition.set(screenX, screenY);
			mining = true;

			return true;
		}

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (pointer == 0 && button == Input.Buttons.LEFT) {
			miningPosition.set(-1, -1);
			mining = false;

			return true;
		}

		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (pointer == 0 && mining) {
			miningPosition.set(screenX, screenY);

			return true;
		}

		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	public WorldScreen getScreen() {
		return screen;
	}
}
