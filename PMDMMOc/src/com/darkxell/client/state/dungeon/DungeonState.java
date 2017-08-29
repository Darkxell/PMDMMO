package com.darkxell.client.state.dungeon;

import java.awt.Graphics2D;

import com.darkxell.client.renderers.FloorRenderer;
import com.darkxell.client.resources.images.AbstractDungeonTileset;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.ui.Keys;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;

public class DungeonState extends AbstractState
{

	public final Floor floor;
	private final FloorRenderer floorRenderer;
	private int xPos = 0, yPos = 0;

	public DungeonState(Floor floor)
	{
		this.floor = floor;
		this.floorRenderer = new FloorRenderer(this.floor);
	}

	@Override
	public void onKeyPressed(short key)
	{}

	@Override
	public void onKeyReleased(short key)
	{}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		this.floorRenderer.drawFloor(g, this.xPos, this.yPos, width, height);
	}

	@Override
	public void update()
	{
		if (Keys.isPressed(Keys.KEY_UP)) yPos -= 5;
		if (Keys.isPressed(Keys.KEY_DOWN)) yPos += 5;
		if (Keys.isPressed(Keys.KEY_LEFT)) xPos -= 5;
		if (Keys.isPressed(Keys.KEY_RIGHT)) xPos += 5;
	}

}
