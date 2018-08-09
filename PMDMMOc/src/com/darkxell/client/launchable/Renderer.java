package com.darkxell.client.launchable;

import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import com.darkxell.client.resources.images.SpriteFactory;

public class Renderer implements Runnable {

	public static final int UPS = 60;

	private int fps;
	private int framesCurrentSecond;
	private long startTime, currentTime, timer;

	public Renderer() {
	}

	public int currentFPS() {
		return this.fps;
	}

	private void render() {
		BufferStrategy bf = Persistance.frame.canvas.getBufferStrategy();
		Graphics2D g = (Graphics2D) bf.getDrawGraphics();
		int width = Persistance.frame.canvas.getWidth(), height = Persistance.frame.canvas.getHeight();
		g.clearRect(0, 0, width, height);

		Persistance.stateManager.render(g, width, height);

		g.dispose();
		bf.show();
	}

	@Override
	public void run() {
		this.startTime = System.nanoTime();
		this.currentTime = this.startTime;
		this.timer = 0;
		this.framesCurrentSecond = 0;
		this.fps = 0;

		try
		{
			while (SpriteFactory.instance().hasLoadingSprites())
				Thread.sleep(5);
		} catch (InterruptedException e1)
		{
			e1.printStackTrace();
		}

		while (Launcher.isRunning && Launcher.getProcessingProfile() == Launcher.PROFILE_UNCAPPED) {
			this.render();
			++this.framesCurrentSecond;

			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			long elapsedTime = System.nanoTime() - this.currentTime;
			this.timer += elapsedTime;
			this.currentTime += elapsedTime;

			if (this.timer >= 1000000000) {
				this.fps = this.framesCurrentSecond;
				this.timer = 0;
				this.framesCurrentSecond = 0;
			}
		}
	}

}
