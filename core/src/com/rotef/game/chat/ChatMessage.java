package com.rotef.game.chat;

public class ChatMessage {
	
	private final String sender;
	private final String message;
	
	private final boolean command;
	private final String commandRoot;
	private final String[] commandArgs;

	public ChatMessage(String sender, String message) {
		this.sender = sender;
		this.message = message;
		
		String[] tokens = message.split(" ");
		if (message.startsWith("/") && tokens.length > 0 && !tokens[0].isEmpty()) {
			command = true;
			
			commandRoot = tokens[0];
			
			commandArgs = new String[tokens.length - 1];
			for (int i = 1; i < tokens.length; i++) {
				commandArgs[i - 1] = tokens[i];
			}
		} else {
			command = false;
			commandRoot = null;
			commandArgs = null;
		}
	}

	public String getSender() {
		return sender;
	}

	public String getMessage() {
		return message;
	}

	public boolean isCommand() {
		return command;
	}

	public String getCommandRoot() {
		return commandRoot;
	}

	public String[] getCommandArgs() {
		return commandArgs;
	}
	
	@Override
	public String toString() {
		return sender + ": " + message;
	}
	
	
	
}
