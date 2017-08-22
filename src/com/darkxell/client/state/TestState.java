package com.darkxell.client.state;

import java.awt.Color;
import java.awt.Graphics2D;

import com.darkxell.client.launchable.Launcher;

/** State with a text and a tick counter for testing. */
public class TestState extends AbstractState
{

	public Color bg = Color.WHITE, fg = Color.BLACK;
	public String text = "Test state - switches at 400";
	public int tick = 0;

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		g.setColor(this.bg);
		g.fillRect(0, 0, width, height);

		g.setColor(this.fg);
		g.drawString(this.text + "   |   " + this.tick, 1, g.getFont().getSize());
	}

	@Override
	public void update()
	{
		++this.tick;
		if (this.tick == 400) Launcher.stateManager.setState(new TestState());
	}

}
