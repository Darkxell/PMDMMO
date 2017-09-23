package com.darkxell.client.state.dungeon;

import java.awt.Graphics2D;

import com.darkxell.client.mechanics.event.DungeonEventProcessor;
import com.darkxell.client.state.dungeon.DungeonState.DungeonSubState;

/** A State that delays a certain number of ticks, then refers to the {@link DungeonEventProcessor} for pending events. */
public class DelayState extends DungeonSubState
{

	public final int duration;
	private int tick = 0;

	public DelayState(DungeonState parent, int duration)
	{
		super(parent);
		this.duration = duration;
	}

	@Override
	public void onKeyPressed(short key)
	{}

	@Override
	public void onKeyReleased(short key)
	{}

	@Override
	public void render(Graphics2D g, int width, int height)
	{}

	@Override
	public void update()
	{
		++this.tick;
		if (this.tick >= this.duration)
		{
			this.parent.setSubstate(this.parent.actionSelectionState);
			DungeonEventProcessor.processPending();
		}
	}

}
