package com.darkxell.client.state.map;

import java.awt.Color;
import java.awt.Graphics2D;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.ui.Keys;
import com.darkxell.common.dungeon.floor.Floor;

public class DungeonFloorMap extends AbstractDisplayMap
{

	/** True if the default location needs has been set. Reset to false for each new floor. */
	boolean defaultLocationSet = false;
	private Floor floor;
	/** Map offsets. */
	private int x = 0, y = 0;

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		if (!this.defaultLocationSet && this.floor != null)
		{
			this.x = this.y = 0;
			this.defaultLocationSet = true;
		}

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);

		g.translate(-this.x, -this.y);

		g.setColor(Color.BLUE);
		g.fillRect(width / 3, height / 3, width / 3, height / 3);

		g.translate(this.x, this.y);
	}

	@Override
	public void update()
	{
		if (Persistance.floor != this.floor)
		{
			this.defaultLocationSet = false;
			this.floor = Persistance.floor;
		}

		final int mapSpeed = 3;

		if (Keys.isPressed(Keys.KEY_MAP_UP)) this.y -= mapSpeed;
		if (Keys.isPressed(Keys.KEY_MAP_DOWN)) this.y += mapSpeed;
		if (Keys.isPressed(Keys.KEY_MAP_LEFT)) this.x -= mapSpeed;
		if (Keys.isPressed(Keys.KEY_MAP_RIGHT)) this.x += mapSpeed;
		if (Keys.isPressed(Keys.KEY_MAP_RESET)) this.defaultLocationSet = false;
	}

}
