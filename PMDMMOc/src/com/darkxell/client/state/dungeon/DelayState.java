package com.darkxell.client.state.dungeon;

import java.awt.Graphics2D;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.event.ClientEventProcessor;
import com.darkxell.client.state.dungeon.DungeonState.DungeonSubState;
import com.darkxell.client.ui.Keys.Key;

/** A State that delays a certain number of ticks, then refers to the {@link ClientEventProcessor} for pending events. */
public class DelayState extends DungeonSubState
{
	public static interface DelayElapsedListener
	{
		public void onDelayElapsed(DelayState state);
	}

	public static final DelayElapsedListener processEvents = new DelayElapsedListener() {

		@Override
		public void onDelayElapsed(DelayState state)
		{
			state.parent.setSubstate(state.parent.actionSelectionState);
			Persistence.eventProcessor().processPending();
		}
	};

	public final int duration;
	public final DelayElapsedListener listener;
	private int tick = 0;

	public DelayState(DungeonState parent, int duration)
	{
		this(parent, duration, processEvents);
	}

	public DelayState(DungeonState parent, int duration, DelayElapsedListener listener)
	{
		super(parent);
		this.duration = duration;
		this.listener = listener;
	}

	@Override
	public void onKeyPressed(Key key)
	{}

	@Override
	public void onKeyReleased(Key key)
	{}

	@Override
	public void prerender(Graphics2D g, int width, int height)
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
			this.parent.setSubstate(this.parent.defaultSubstate);
			this.listener.onDelayElapsed(this);
		}
	}

}
