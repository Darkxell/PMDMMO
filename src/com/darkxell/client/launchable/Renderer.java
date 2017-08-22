package com.darkxell.client.launchable;

import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

public class Renderer implements Runnable
{

	public static final int UPS = 60;

	public Renderer()
	{}

	private void render()
	{
		BufferStrategy bf = Launcher.frame.canvas.getBufferStrategy();
		Graphics2D g = (Graphics2D) bf.getDrawGraphics();
		int width = Launcher.frame.canvas.getWidth(), height = Launcher.frame.canvas.getHeight();
		g.clearRect(0, 0, width, height);

		Launcher.stateManager.render(g, width, height);

		g.dispose();
		bf.show();
	}

	@Override
	public void run()
	{

		while (Launcher.isRunning)
		{
			this.render();
			try
			{
				Thread.sleep(5);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

}
