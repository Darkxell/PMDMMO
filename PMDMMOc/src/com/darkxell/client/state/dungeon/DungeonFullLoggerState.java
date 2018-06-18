package com.darkxell.client.state.dungeon;

import java.awt.Graphics2D;

import com.darkxell.client.state.dungeon.DungeonState.DungeonSubState;
import com.darkxell.client.ui.Keys;

public class DungeonFullLoggerState extends DungeonSubState
{

	public DungeonFullLoggerState(DungeonState parent)
	{
		super(parent);
	}

	@Override
	public void onKeyPressed(short key)
	{
		if (key == Keys.KEY_RUN)
		{
			this.parent.setSubstate(this.parent.actionSelectionState);
			this.parent.logger.setFullscreen(false);
		}
	}

	@Override
	public void onKeyReleased(short key)
	{}

	@Override
	public void onStart()
	{
		super.onStart();
		this.parent.logger.setFullscreen(true);
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{}

	@Override
	public void update()
	{
		if (Keys.isPressed(Keys.KEY_UP) && !Keys.isPressed(Keys.KEY_DOWN)) this.parent.logger.scrollUp();
		if (!Keys.isPressed(Keys.KEY_UP) && Keys.isPressed(Keys.KEY_DOWN)) this.parent.logger.scrollDown();
	}

}
