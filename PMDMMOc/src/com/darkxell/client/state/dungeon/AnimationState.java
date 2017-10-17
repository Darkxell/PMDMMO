package com.darkxell.client.state.dungeon;

import java.awt.Graphics2D;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.animation.AbstractAnimation;
import com.darkxell.client.mechanics.animation.AnimationEndListener;
import com.darkxell.client.state.dungeon.DungeonState.DungeonSubState;

/** A State that displays an Animation, then refers to the DungeonEventProcessor for pending events.<br />
 * The Animation should not be null of this State will never end ! It should be set after creation, to allow the Animation to have this State as listener. */
public class AnimationState extends DungeonSubState implements AnimationEndListener
{

	public AbstractAnimation animation;

	public AnimationState(DungeonState parent)
	{
		super(parent);
	}

	@Override
	public void onAnimationEnd(AbstractAnimation animation)
	{
		this.parent.setSubstate(this.parent.actionSelectionState);
		Persistance.eventProcessor.processPending();
	}

	@Override
	public void onKeyPressed(short key)
	{}

	@Override
	public void onKeyReleased(short key)
	{}

	@Override
	public void onStart()
	{
		super.onStart();
		this.animation.start();
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		this.animation.render(g, width, height);
	}

	@Override
	public void update()
	{}

}
