package com.darkxell.common.animation;

import java.awt.Graphics2D;

public class AbstractAnimation
{

	/** The total duration of this Animation. */
	public final int duration;
	public final AnimationEndListener listener;
	private int tick = 0;

	public AbstractAnimation(int duration, AnimationEndListener listener)
	{
		this.duration = duration;
		this.listener = listener;
	}

	/** @return True if this Animation has ended. */
	public boolean isOver()
	{
		return this.tick == this.duration - 1;
	}

	/** Called when this Animation finishes. */
	public void onFinish()
	{
		AnimationTicker.instance.unregister(this);
		this.listener.onAnimationEnd(this);
	}

	public void render(Graphics2D g, int width, int height)
	{}

	public void start()
	{
		AnimationTicker.instance.register(this);
	}

	/** @return The current tick of this Animation. */
	public int tick()
	{
		return this.tick;
	}

	public void update()
	{
		if (!this.isOver()) ++this.tick;
		// Can't use else: needs to increase and finish on same tick.
		if (this.isOver()) this.onFinish();
	}

}
