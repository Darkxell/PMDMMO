package com.darkxell.client.mechanics.animation;

import java.awt.Graphics2D;

import com.darkxell.client.resources.music.SoundManager;

public class AbstractAnimation
{

	/** The total duration of this Animation. */
	public final int duration;
	public final AnimationEndListener listener;
	/** True if displaying this Animation should stop the Pending Events. */
	public boolean needsPause = true;
	/** The number of times this animation plays. Usually 1, or -1 as until removed. */
	public int plays = 1;
	/** The ID of the sound to play when this Animation is played. */
	public String sound = null;
	/** The number of ticks to wait before playing the sound. */
	public int soundDelay = 0;
	/** Used to remove this animation when this Source is dropped. */
	public Object source;
	private int tick = 0;

	public AbstractAnimation(int duration, AnimationEndListener listener)
	{
		this.duration = duration;
		this.listener = listener;
	}

	/** @return From 0 to 1, how far this Animation is. */
	public float completion()
	{
		return this.tick * 1f / (this.duration * Math.abs(this.plays));
	}

	/** @return True if this Animation has ended. */
	public boolean isOver()
	{
		if (this.plays == -1) return false;
		return this.tick == (this.duration - 1) * Math.abs(this.plays);
	}

	/** Called when this Animation finishes. */
	public void onFinish()
	{
		AnimationTicker.instance.unregister(this);
		if (this.listener != null) this.listener.onAnimationEnd(this);
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
		if (this.sound != null && this.tick == this.soundDelay) SoundManager.playSound(this.sound);
		if (!this.isOver()) ++this.tick;
		// Can't use else: needs to increase and finish on same tick.
		if (this.isOver()) this.onFinish();
	}

}
