package com.rotef.game.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.rotef.game.Game;

public class ErrorScreen extends BaseScreen {

	private Throwable e;

	public ErrorScreen(Throwable e) {
		super();

		this.e = e;
	}

	@Override
	public void show() {
		super.show();
	}

	public Throwable getThrowable() {
		return e;
	}

	@Override
	protected void makeContent() {
		final Table scrollTable = new Table();
		ScrollPane scrollPane = new ScrollPane(scrollTable, UI.skin);
		rootTable.add(scrollPane).width(width / 2 / uiScale).height(height / 2 / uiScale).center();

		Label label = new Label(e.toString(), UI.skin);
		scrollTable.add(label).center();

		scrollTable.row();

		// ok button

		TextButton okButton = new TextButton(Game.language.get("ok"), UI.skin);
		okButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Game.game.setScreen(new MainMenuScreen());
			}
		});
		rootTable.row();
		rootTable.add(okButton).center();
	}

}
