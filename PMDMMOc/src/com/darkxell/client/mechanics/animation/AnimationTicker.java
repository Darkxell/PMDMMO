package com.darkxell.client.mechanics.animation;

import java.util.HashSet;

public final class AnimationTicker
{

	public static final AnimationTicker instance = new AnimationTicker();

	private HashSet<AbstractAnimation> animations = new HashSet<AbstractAnimation>();

	private AnimationTicker()
	{}

	public void register(AbstractAnimation animation)
	{
		this.animations.add(animation);
	}

	public void unregister(AbstractAnimation animation)
	{
		this.animations.remove(animation);
	}

	public void update()
	{
		for (AbstractAnimation a : animations)
			a.update();
	}

}
