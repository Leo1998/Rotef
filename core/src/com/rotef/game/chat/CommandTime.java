package com.rotef.game.chat;

import com.rotef.game.world.TimeManager;

public class CommandTime extends Command {

	public CommandTime(ChatManager manager) {
		super(manager, "time");
	}

	@Override
	public void execute(String[] args) {
		TimeManager timeManager = this.getManager().getWorld().getTimeManager();
		if (args.length == 0) {
			this.getManager().send(new ChatMessage(null, "Daytime: " + timeManager.getDaytime() + ", Time: " + timeManager.getTime() + ", Day: " + timeManager.getDay()));
		} else if (args.length == 2 && args[0].equals("set")) {
			int daytime = -1;
			try {
				daytime = Integer.valueOf(args[1]);
			} catch (Exception e) {
			}

			if (daytime >= 0 && daytime < 24000) {
				timeManager.setDaytime(daytime);
			}
		}
	}

}
