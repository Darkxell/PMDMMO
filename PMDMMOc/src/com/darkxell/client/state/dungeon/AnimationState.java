package com.darkxell.client.state.dungeon;

import java.awt.Graphics2D;

import com.darkxell.client.mechanics.animation.AbstractAnimation;
import com.darkxell.client.state.dungeon.DungeonState.DungeonSubState;
import com.darkxell.client.ui.Keys.Key;

/** A State waits for an Animation's completion, then refers to the DungeonEventProcessor for pending events.<br />
 * The Animation should not be null or this State will never end! It should be set after creation, to allow the Animation to have this State as listener. */
public class AnimationState extends DungeonSubState
{

	public AbstractAnimation animation;

	public AnimationState(DungeonState parent)
	{
		super(parent);
	}

	@Override
	public void onKeyPressed(Key key)
	{}

	@Override
	public void onKeyReleased(Key key)
	{}

	@Override
	public void onStart()
	{
		super.onStart();
		this.animation.start();
	}

	@Override
	public void prerender(Graphics2D g, int width, int height)
	{}

	@Override
	public void render(Graphics2D g, int width, int height)
	{}

	@Override
	public void update()
	{}

}
