package com.rotef.game.world.light;

public class LightingSwipeProcessor {

	public interface Task {

		void process(LightingSwipe swipe);

	}

	private Thread thread;
	private LightingSwipe swipe;
	private volatile Task task;
	private volatile boolean running = false;
	private Object lock = new Object();
	private LightingSwipeExecuter executer;

	public LightingSwipeProcessor(LightingSwipe swipe, LightingSwipeExecuter executer) {
		this.swipe = swipe;
		this.executer = executer;

		this.thread = new Thread(new Runnable() {
			@Override
			public void run() {
				running = true;

				while (running) {
					synchronized (lock) {
						try {
							lock.wait();
						} catch (InterruptedException e) {
						}
					}

					if (task != null) {
						executer.onStart(LightingSwipeProcessor.this);

						task.process(swipe);

						task = null;
						executer.onEnd(LightingSwipeProcessor.this);
					}

				}
			}
		}, "LightingSwipeProcessor-Thread");

		thread.start();
	}

	public void execute(Task task) {
		this.task = task;

		synchronized (lock) {
			lock.notifyAll();
		}
	}

	public boolean isWorking() {
		return task != null;
	}

	public void dispose() {
		running = false;

		synchronized (lock) {
			lock.notifyAll();
		}
	}

	public LightingSwipe getSwipe() {
		return swipe;
	}

	public LightingSwipeExecuter getManager() {
		return executer;
	}

}
