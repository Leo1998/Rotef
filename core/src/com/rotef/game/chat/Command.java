package com.rotef.game.chat;

public abstract class Command {

	private ChatManager manager;
	private String cmd;

	public Command(ChatManager manager, String cmd) {
		this.manager = manager;
		this.cmd = cmd;
	}

	public String getCmd() {
		return cmd;
	}

	public ChatManager getManager() {
		return manager;
	}

	public abstract void execute(String[] args);

}
