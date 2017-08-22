package com.darkxell.client.launchable;

import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

public class Game implements Runnable
{

	/** Set to false to stop the game. */
	private boolean isRunning;

	public Game()
	{}

	/** Renders the game. */
	private void render()
	{
		BufferStrategy bf = Launcher.frame.canvas.getBufferStrategy();
		Graphics2D g = (Graphics2D) bf.getDrawGraphics();
		int width = Launcher.frame.getWidth(), height = Launcher.frame.getHeight();
		g.clearRect(0, 0, width, height);

		// Draw here
		g.drawString("System.out.println(\"Hello world\");", 1, g.getFont().getSize());

		g.dispose();
		bf.show();
	}

	@Override
	public void run()
	{
		this.isRunning = true;

		while (this.isRunning)
		{
			this.update();
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

	/** Stops the game. */
	public void stop()
	{
		this.isRunning = false;
	}

	/** Updates the game. */
	private void update()
	{
		// TODO Auto-generated method stub
	}

}
