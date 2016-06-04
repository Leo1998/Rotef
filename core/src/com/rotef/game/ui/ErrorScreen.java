package com.rotef.game.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.rotef.game.Game;
import com.rotef.game.util.ExceptionUtils;

public class ErrorScreen extends BaseScreen {

	private Exception e;
	private String msg;

	public ErrorScreen(Exception e) {
		super();

		this.e = e;
		this.msg = ExceptionUtils.getStackTrace(e);
	}

	@Override
	public void show() {
		super.show();
	}

	public Exception getException() {
		return e;
	}

	public String getMsg() {
		return msg;
	}

	@Override
	protected void makeContent() {
		final Table scrollTable = new Table();
		ScrollPane scrollPane = new ScrollPane(scrollTable, UI.skin);
		rootTable.add(scrollPane).width(width / 2 / uiScale).height(height / 2 / uiScale).center();

		Label label = new Label(msg, UI.skin);
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
