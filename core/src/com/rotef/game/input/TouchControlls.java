package com.rotef.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.rotef.game.ui.UI;
import com.rotef.game.ui.WorldScreen;

public class TouchControlls {

	private Rectangle leftButton = new Rectangle();
	private Rectangle rightButton = new Rectangle();
	private Rectangle bButton = new Rectangle();
	private Rectangle aButton = new Rectangle();

	private int leftPointer = -1;
	private int rightPointer = -1;
	private int bPointer = -1;
	private int aPointer = -1;

	public TouchControlls() {
	}

	public void updateAndRender(WorldScreen screen) {
		update();

		render(screen.getBatch(), screen.getUiScale());
	}

	private void update() {
		updateButtons();
	}

	private void updateButtons() {
		int w = Gdx.graphics.getWidth();
		int h = Gdx.graphics.getHeight();

		int size = h / 5;

		leftButton.set(0, 0, w / 4, size);
		rightButton.set(w / 4, 0, w / 4, size);
		bButton.set((w / 4) * 2, 0, w / 4, size);
		aButton.set((w / 4) * 3, 0, w / 4, size);
	}

	private void render(SpriteBatch batch, float uiScale) {
		int w = (int) (Gdx.graphics.getWidth() / uiScale);
		int h = (int) (Gdx.graphics.getHeight() / uiScale);

		int size = h / 5;
		int padding = ((w / 4) - size) / 2;

		batch.draw(UI.getTexture("button-left"), padding, 0, size, size);
		batch.draw(UI.getTexture("button-right"), w / 4 + padding, 0, size, size);
		batch.draw(UI.getTexture("button-b"), (w / 4) * 2 + padding, 0, size, size);
		batch.draw(UI.getTexture("button-a"), (w / 4) * 3 + padding, 0, size, size);

		if (isLeftClicked()) {
			batch.draw(UI.getTexture("hover"), leftButton.x / uiScale, leftButton.y / uiScale, leftButton.width / uiScale, leftButton.height / uiScale);
		}
		if (isRightClicked()) {
			batch.draw(UI.getTexture("hover"), rightButton.x / uiScale, rightButton.y / uiScale, rightButton.width / uiScale, rightButton.height / uiScale);
		}
		if (isBClicked()) {
			batch.draw(UI.getTexture("hover"), bButton.x / uiScale, bButton.y / uiScale, bButton.width / uiScale, bButton.height / uiScale);
		}
		if (isAClicked()) {
			batch.draw(UI.getTexture("hover"), aButton.x / uiScale, aButton.y / uiScale, aButton.width / uiScale, aButton.height / uiScale);
		}
	}

	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		checkPressed(screenX, screenY, pointer);

		return false;
	}

	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		checkReleased(pointer);

		return false;
	}

	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	private void checkPressed(int screenX, int screenY, int pointer) {
		int h = Gdx.graphics.getHeight();
		int x = screenX;
		int y = h - screenY;

		if (leftButton.contains(x, y)) {
			leftPointer = pointer;
		}
		if (rightButton.contains(x, y)) {
			rightPointer = pointer;
		}
		if (bButton.contains(x, y)) {
			bPointer = pointer;
		}
		if (aButton.contains(x, y)) {
			aPointer = pointer;
		}
	}

	private void checkReleased(int pointer) {
		if (leftPointer == pointer) {
			leftPointer = -1;
		}
		if (rightPointer == pointer) {
			rightPointer = -1;
		}
		if (bPointer == pointer) {
			bPointer = -1;
		}
		if (aPointer == pointer) {
			aPointer = -1;
		}
	}

	public boolean isLeftClicked() {
		return leftPointer != -1;
	}

	public boolean isRightClicked() {
		return rightPointer != -1;
	}

	public boolean isBClicked() {
		return bPointer != -1;
	}

	public boolean isAClicked() {
		return aPointer != -1;
	}

}
