package com.rotef.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class AnimatedListener extends ClickListener {

	public AnimatedListener() {
	}

	@Override
	public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
		super.enter(event, x, y, pointer, fromActor);

		Actor target = event.getTarget();

		if (shouldAnimate(target)) {
			target.addAction(Actions.color(Color.GRAY, 0.4f));
		}
	}

	@Override
	public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
		super.exit(event, x, y, pointer, toActor);

		Actor target = event.getTarget();

		if (shouldAnimate(target)) {
			target.addAction(Actions.color(Color.WHITE, 0.2f));
		}
	}

	private boolean shouldAnimate(Actor target) {
		return false;
	}

}
