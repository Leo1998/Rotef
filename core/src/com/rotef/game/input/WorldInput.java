package com.rotef.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.rotef.game.renderer.WorldScreen;
import com.rotef.game.world.entity.Mob;
import com.rotef.game.world.light.Light;

public class WorldInput implements InputProcessor {

	private WorldScreen screen;

	public WorldInput(WorldScreen screen) {
		this(screen, false);
	}

	public WorldInput(WorldScreen screen, boolean desktopTouchControlls) {
		this.screen = screen;
	}

	public void movePlayer(Mob mob, float delta) {
		float xMove = 0;
		if (Gdx.input.isKeyPressed(Keys.A)) {
			xMove -= 1;
		}
		if (Gdx.input.isKeyPressed(Keys.D)) {
			xMove += 1;
		}

		mob.walk(xMove);

		boolean jump = false;
		if (Gdx.input.isKeyPressed(Keys.W)) {
			jump = true;
		}

		if (jump) {
			mob.jump();
		}

		boolean interact = false;
		if (Gdx.input.isKeyJustPressed(Keys.E)) {
			interact = true;
		}

		if (interact) {
			Light light = new Light(mob.getX(), mob.getY(), 5, Color.WHITE);
			mob.getWorld().getLightManager().addLight(light);
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
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
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
