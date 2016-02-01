package com.rotef.game.util;

public abstract class LoadingThread implements Runnable {

	/**
	 * the progress between 0 and 1
	 */
	protected volatile float progress;

	private volatile Object result;

	private volatile Thread thread = null;

	public LoadingThread() {
	}

	public synchronized void launch() {
		thread = new Thread(this);
		thread.setName(getClass().getName());
		thread.setPriority(Thread.MAX_PRIORITY);

		thread.start();
	}

	@Override
	public void run() {
		progress = 0.0f;
		result = null;

		result = runProcess();

		thread = null;
	}

	public boolean isRunning() {
		return thread != null;
	}

	protected abstract Object runProcess();

	public float getProgress() {
		return progress;
	}

	public void setProgress(float progress) {
		this.progress = progress;
	}

	public Object getResult() {
		return result;
	}

}
