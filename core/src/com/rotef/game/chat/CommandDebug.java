package com.rotef.game.chat;

import com.rotef.game.Game;

public class CommandDebug extends Command {

	public CommandDebug(ChatManager manager) {
		super(manager, "debug");
	}

	@Override
	public void execute(String[] args) {
		boolean debug = !Game.config.isDebug();

		if (args.length == 1 && args[0].equalsIgnoreCase("true") && args[0].equalsIgnoreCase("false")) {
			debug = Boolean.valueOf(args[0]);
		}

		Game.config.setDebug(debug);
	}

}
