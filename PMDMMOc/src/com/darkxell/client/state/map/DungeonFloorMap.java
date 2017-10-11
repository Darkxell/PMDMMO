package com.darkxell.client.state.map;

import java.awt.Color;
import java.awt.Graphics2D;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.ui.Keys;
import com.darkxell.common.dungeon.floor.Floor;

public class DungeonFloorMap extends AbstractDisplayMap
{

	/** True if the default location needs has been set. Reset to false for each new floor. */
	private boolean defaultLocationSet = false;
	private Floor floor;
	/** True if the map should follow the leader. Set to false whenever the player moves the map themselves. */
	private boolean followLeader = true;
	/** Map offsets. */
	private int x = 0, y = 0;

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		if ((this.followLeader || !this.defaultLocationSet) && this.floor != null && Persistance.player.getDungeonPokemon().tile != null)
		{
			this.x = Persistance.player.getDungeonPokemon().tile.x * 2 - width / 2;
			this.y = Persistance.player.getDungeonPokemon().tile.y * 2 - height / 2;
			this.defaultLocationSet = true;
		}

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);

		g.translate(-this.x, -this.y);

		if (this.floor != null)
		{
			g.setColor(Color.WHITE);
			g.drawRect(0, 0, this.floor.getWidth() * 4, this.floor.getHeight() * 4);
			g.drawLine(0, 0, this.floor.getWidth() * 4, this.floor.getHeight() * 4);
			g.drawLine(this.floor.getWidth() * 4, 0, 0, this.floor.getHeight() * 4);
		}

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
		if (Keys.isPressed(Keys.KEY_MAP_RESET))
		{
			this.followLeader = true;
			this.defaultLocationSet = false;
		}
		if (Keys.isPressed(Keys.KEY_MAP_UP) || Keys.isPressed(Keys.KEY_MAP_DOWN) || Keys.isPressed(Keys.KEY_MAP_LEFT) || Keys.isPressed(Keys.KEY_MAP_RIGHT)) this.followLeader = false;
	}

}
