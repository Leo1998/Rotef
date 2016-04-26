package com.rotef.game.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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
		ScrollPane scrollPane = new ScrollPane(scrollTable, UI.scrollPaneStyle);
		rootTable.add(scrollPane).width(width / 2 / uiScale).height(height / 2 / uiScale).center();

		Label label = new Label(msg, UI.labelStyle);
		scrollTable.add(label).center();

		scrollTable.row();

		// ok button

		TextButton okButton = new TextButton(Game.language.get("ok"), UI.textButtonStyle);
		okButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Game.game.setScreen(new MainMenuScreen());
			}
		});
		rootTable.row();
		rootTable.add(okButton).center();
	}

}
