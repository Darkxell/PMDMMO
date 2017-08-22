package com.darkxell.client.state;

import java.awt.Color;
import java.awt.Graphics2D;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.ui.Keys;

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
		g.drawString("UPS: " + Launcher.updater.currentUPS() + ", FPS: " + Launcher.renderer.currentFPS(), 1, g.getFont().getSize() * 2);

		String keys = "";
		for (short key = 0; key < Keys.KEY_COUNT; ++key)
			if (Keys.isPressed(key))
			{
				if (!keys.equals("")) keys += ", ";
				keys += Keys.getKeyName(key);
			}
		if (!keys.equals("")) g.drawString("Pressed keys: " + keys, 1, g.getFont().getSize() * 3);
	}

	@Override
	public void update()
	{
		++this.tick;
		if (this.tick == 400) Launcher.stateManager.setState(new TestState());
	}

}
