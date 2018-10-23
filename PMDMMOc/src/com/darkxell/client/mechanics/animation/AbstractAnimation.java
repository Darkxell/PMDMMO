package com.darkxell.client.mechanics.animation;

import java.awt.Graphics2D;

import com.darkxell.client.resources.music.SoundManager;

public class AbstractAnimation
{

	protected final AnimationData data;
	int delayTime = 0;
	/** The total duration of this Animation. */
	int duration = 0;
	private AnimationEndListener listener;
	/** The number of times this animation plays. Usually 1, or -1 as until removed. */
	public int plays = 1;
	/** Used to remove this animation when this Source is dropped. */
	public Object source;
	private int tick = 0;
	private AnimationData usedData;

	public AbstractAnimation(AnimationData data, int duration, AnimationEndListener listener)
	{
		this.data = this.usedData = data;
		this.duration = duration;
		this.listener = listener;
	}

	protected AnimationData chooseData()
	{
		return this.data;
	}

	/** @return From 0 to 1, how far this Animation is. */
	public float completion()
	{
		return this.tick * 1f / (this.duration * Math.abs(this.plays));
	}

	public AnimationData data()
	{
		return this.usedData;
	}

	public int duration()
	{
		return this.duration;
	}

	/** @return True if this Animation's delay has ended and thus other game logic and animations may begin. */
	public boolean isDelayOver()
	{
		if (this.plays == -1) return false;
		return this.tick == (this.delayTime - 1) * Math.abs(this.plays);
	}

	/** @return True if this Animation has ended. */
	public boolean isOver()
	{
		if (this.plays == -1) return false;
		return this.tick == (this.duration - 1) * Math.abs(this.plays);
	}

	/** @return True if this Animation should pause the game logic. */
	public boolean needsPause()
	{
		return this.delayTime > 0;
	}

	private void onDelayFinished()
	{
		if (this.listener != null)
		{
			this.listener.onAnimationEnd(this);
			this.listener = null;
		}
	}

	/** Called when this Animation finishes. */
	public void onFinish()
	{
		AnimationTicker.instance.unregister(this);
	}

	public void render(Graphics2D g, int width, int height)
	{}

	public void start()
	{
		AnimationTicker.instance.register(this);
		this.usedData = this.chooseData();
	}

	public void stop()
	{
		this.onDelayFinished();
		this.onFinish();
	}

	/** @return The current tick of this Animation. */
	public int tick()
	{
		return this.tick;
	}

	public void update()
	{
		if (this.data().sound != null && this.tick == this.data().soundDelay) SoundManager.playSound(this.data().sound);
		if (!this.isOver()) ++this.tick;
		// Can't use else: needs to increase and finish on same tick.
		if (this.isDelayOver()) this.onDelayFinished();
		if (this.isOver()) this.onFinish();
	}

}
