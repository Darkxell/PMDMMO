package com.darkxell.client.launchable;

public class Updater implements Runnable
{

	public static final int targetUPS = 60;

	private long startTime, currentTime;
	private double updateTime, timePerUpdate;

	public Updater()
	{}

	@Override
	public void run()
	{
		// Preparing FPS handling
		this.startTime = System.nanoTime();
		this.currentTime = this.startTime;
		this.updateTime = 0;
		this.timePerUpdate = 1000000000 / targetUPS;

		while (Launcher.isRunning)
		{
			this.update();

			try
			{
				Thread.sleep(5);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	private void update()
	{
		// Calculate elapsed time
		long elapsedTime = System.nanoTime() - this.currentTime;
		this.currentTime += elapsedTime;
		this.updateTime += elapsedTime / this.timePerUpdate;

		// If a tick has passed, update until there is no delayed update
		while (this.updateTime >= 1)
		{
			Launcher.stateManager.update();
			--this.updateTime;
		}
	}

}
