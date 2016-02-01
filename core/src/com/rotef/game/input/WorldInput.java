package com.rotef.game.input;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.rotef.game.ui.WorldScreen;
import com.rotef.game.world.entity.Mob;
import com.rotef.game.world.light.Light;

public class WorldInput implements InputProcessor, GestureListener {

	private WorldScreen screen;

	private TouchControlls touchControlls;

	public WorldInput(WorldScreen screen) {
		this(screen, false);
	}

	public WorldInput(WorldScreen screen, boolean desktopTouchControlls) {
		this.screen = screen;

		boolean touch = desktopTouchControlls || Gdx.app.getType().equals(ApplicationType.Android) || Gdx.app.getType().equals(ApplicationType.iOS);

		if (touch) {
			touchControlls = new TouchControlls();
		}
	}

	public void render(float delta, WorldScreen screen) {
		if (isTouchControlls()) {
			touchControlls.updateAndRender(screen);
		}
	}

	public void movePlayer(Mob mob, float delta) {
		float xMove = 0;

		if (isTouchControlls()) {
			if (touchControlls.isLeftClicked()) {
				xMove -= 1;
			}
			if (touchControlls.isRightClicked()) {
				xMove += 1;
			}
		} else {
			if (Gdx.input.isKeyPressed(Keys.A)) {
				xMove -= 1;
			}
			if (Gdx.input.isKeyPressed(Keys.D)) {
				xMove += 1;
			}
		}
		mob.walk(xMove);

		boolean jump = false;
		if (isTouchControlls()) {
			if (touchControlls.isAClicked()) {
				jump = true;
			}
		} else {
			if (Gdx.input.isKeyPressed(Keys.W)) {
				jump = true;
			}
		}
		if (jump) {
			mob.jump();
		}
		
		boolean interact = false;
		if (isTouchControlls()) {
			if (touchControlls.isBClicked()) {
				interact = true;
			}
		} else {
			if (Gdx.input.isKeyJustPressed(Keys.E)) {
				interact = true;
			}
		}
		if (interact) {
			Light light = new Light(mob.getX(), mob.getY(), 5, Color.WHITE); 
			mob.getWorld().getLightManager().addLight(light);
		}
	}

	public boolean isTouchControlls() {
		return touchControlls != null;
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
		if (isTouchControlls() && touchControlls.touchDown(screenX, screenY, pointer, button)) {
			return true;
		}

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (isTouchControlls() && touchControlls.touchUp(screenX, screenY, pointer, button)) {
			return true;
		}

		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (isTouchControlls() && touchControlls.touchDragged(screenX, screenY, pointer)) {
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

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		return false;
	}

	public WorldScreen getScreen() {
		return screen;
	}
}
