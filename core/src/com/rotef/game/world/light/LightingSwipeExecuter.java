package com.rotef.game.world.light;

import com.rotef.game.util.ThreadUtils;

public class LightingSwipeExecuter {

	private LightingSwipe[] swipes;
	private LightingSwipeProcessor[] processors;
	private volatile int workingProcessors = 0;

	public LightingSwipeExecuter(int stateWidth, int stateHeight, int lightingCores) {
		this.swipes = new LightingSwipe[lightingCores];
		int x = 0;
		int w = stateWidth;
		int s = stateWidth / lightingCores;
		for (int i = 0; i < swipes.length; i++) {
			w -= s;
			if (i == swipes.length - 1) {
				s += w;
			}

			this.swipes[i] = new LightingSwipe(x, 0, s, stateHeight);

			x += s;
		}

		this.processors = new LightingSwipeProcessor[this.swipes.length];
		for (int i = 0; i < swipes.length; i++) {
			this.processors[i] = new LightingSwipeProcessor(this.swipes[i], this);
		}
	}

	public void onStart(LightingSwipeProcessor processor) {
		workingProcessors++;
	}

	public void onEnd(LightingSwipeProcessor processor) {
		workingProcessors--;

		// if (workingProcessors == 0) {
		//
		// }
	}

	public void executeLightingPass(LightingSwipeProcessor.Task pass) {
		for (int i = 0; i < processors.length; i++) {
			LightingSwipeProcessor processor = processors[i];

			processor.execute(pass);
		}

		ThreadUtils.sleep(1);
		Thread.yield();

		while (workingProcessors > 0) {
			ThreadUtils.sleep(1);
		}
	}

	public void dispose() {
		for (int i = 0; i < swipes.length; i++) {
			this.processors[i].dispose();
		}
	}

	public LightingSwipe[] getSwipes() {
		return swipes;
	}

	public int getWorkingProcessors() {
		return workingProcessors;
	}

}
