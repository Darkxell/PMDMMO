package com.darkxell.client.launchable;

public class Updater implements Runnable {

	public static final int targetUPS = 60;

	private long startTime, currentTime, timer;
	private int updatesCurrentSecond;
	private double updateTime, timePerUpdate;
	private int ups = 0;

	public Updater() {
	}

	public int currentUPS() {
		return this.ups;
	}

	@Override
	public void run() {
		// Preparing FPS handling
		this.startTime = System.nanoTime();
		this.currentTime = this.startTime;
		this.updateTime = 0;
		this.timer = 0;
		this.timePerUpdate = 1000000000 / targetUPS;
		this.updatesCurrentSecond = 0;
		this.ups = 0;

		while (Launcher.isRunning && Launcher.getProcessingProfile() == Launcher.PROFILE_UNCAPPED) {
			this.update();

			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void update() {
		// Calculate elapsed time
		long elapsedTime = System.nanoTime() - this.currentTime;
		this.timer += elapsedTime;
		this.currentTime += elapsedTime;
		this.updateTime += elapsedTime / this.timePerUpdate;

		// If a tick has passed, update until there is no delayed update
		while (this.updateTime >= 1) {
			Persistance.stateManager.update();
			++this.updatesCurrentSecond;
			--this.updateTime;
		}

		if (this.timer >= 1000000000) {
			this.ups = this.updatesCurrentSecond;
			this.timer = 0;
			this.updatesCurrentSecond = 0;
		}
	}

}
