package com.darkxell.client.launchable;

import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import com.darkxell.client.state.StateManager;
import com.darkxell.client.state.TestState;

public class Game implements Runnable
{

	/** Set to false to stop the game. */
	private boolean isRunning;
	public StateManager stateManager;

	public Game()
	{}

	/** Renders the game. */
	private void render()
	{
		BufferStrategy bf = Launcher.frame.canvas.getBufferStrategy();
		Graphics2D g = (Graphics2D) bf.getDrawGraphics();
		int width = Launcher.frame.canvas.getWidth(), height = Launcher.frame.canvas.getHeight();
		g.clearRect(0, 0, width, height);

		this.stateManager.render(g, width, height);

		g.dispose();
		bf.show();
	}

	@Override
	public void run()
	{
		this.isRunning = true;
		this.stateManager = new StateManager();
		this.stateManager.setState(new TestState(), 0);

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
		this.stateManager.update();
	}

}
