package com.darkxell.client.state.map;

import java.awt.Color;
import java.awt.Graphics2D;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.common.dungeon.floor.Floor;

public class DungeonFloorMap extends AbstractDisplayMap
{

	private Floor floor;

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);
	}

	@Override
	public void update()
	{
		if (Persistance.floor != this.floor) this.floor = Persistance.floor;
	}

}
