package com.rotef.game.chat;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.rotef.game.Game;
import com.rotef.game.ui.UI;
import com.rotef.game.ui.WorldView;

public class ChatView extends WorldView {

	private List<String> messageList;
	private ScrollPane scrollPane;
	private TextField messageTextField;
	private TextButton enterButton;

	private ChatManager chatManager;

	public ChatView(ChatManager manager) {
		super(250, 400);

		this.chatManager = manager;

		this.chatManager.addChatListener(new ChatListener() {
			@Override
			public void onReceive(ChatMessage message) {
				updateMessageList();
			}
		});
	}

	@Override
	public void makeContent(Stage stage) {
		rootTable.top();

		messageList = new List<>(UI.skin);

		scrollPane = new ScrollPane(messageList, UI.skin);
		scrollPane.setForceScroll(false, true);
		scrollPane.setFlickScroll(false);
		scrollPane.setFadeScrollBars(false);
		rootTable.add(scrollPane).expand().fill().colspan(2);

		rootTable.row();

		messageTextField = new TextField("", UI.skin);
		messageTextField.addListener(new InputListener() {
			@Override
			public boolean keyUp(InputEvent event, int keycode) {
				if (keycode == Input.Keys.ENTER) {
					send();
				}
				return false;
			}
		});
		rootTable.add(messageTextField).padTop(5).expandX().fill();

		enterButton = new TextButton(Game.language.get("ok"), UI.skin);
		enterButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				send();
			}
		});
		rootTable.add(enterButton).padTop(5).padLeft(5).right();

		stage.setKeyboardFocus(messageTextField);
	}

	private void send() {
		String msg = messageTextField.getText();
		messageTextField.setText(null);

		if (msg != null && !msg.isEmpty()) {
			chatManager.send(new ChatMessage("Player", msg));
		}
	}

	private void updateMessageList() {
		messageList.setItems(chatManager.getChatMessages());

		messageList.setSelectedIndex(messageList.getItems().size - 1);

		scrollPane.invalidate();
		scrollPane.validate();
		scrollPane.setScrollPercentY(1.0f);
	}

}
