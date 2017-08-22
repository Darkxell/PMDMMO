package com.darkxell.client.state;

import java.awt.Color;
import java.awt.Graphics2D;

public class StateManager
{

	public static final int defaultTransitionTime = 40;

	private AbstractState currentState;
	/** The state we are currently transitioning to. */
	private AbstractState nextState;
	/** Current transition state. 0 for no transition, negative for fading in, positive for fading out. */
	private int transition;
	/** The time in ticks to fade states in and out (which makes it HALF the transition time). */
	private int transitionTime;

	public StateManager()
	{}

	public void onKeyPressed(short key)
	{
		this.currentState.onKeyPressed(key);
	}

	public void onKeyReleased(short key)
	{
		this.currentState.onKeyReleased(key);
	}

	public void render(Graphics2D g, int width, int height)
	{
		if (this.transition == 0) this.currentState.render(g, width, height);
		else if (this.transition > 0)
		{
			this.currentState.render(g, width, height);
		} else if (this.transition < 0)
		{
			this.nextState.render(g, width, height);
		}

		double alpha = 1d * Math.abs(this.transition) / this.transitionTime * 255;
		g.setColor(new Color(0, 0, 0, (int) alpha));
		g.fillRect(0, 0, width, height);
	}

	public void setState(AbstractState state)
	{
		this.setState(state, defaultTransitionTime);
	}

	public void setState(AbstractState state, int transitionTime)
	{
		if (transitionTime == 0) this.currentState = state;
		else
		{
			this.nextState = state;
			this.transitionTime = transitionTime;
			this.transition = 1;
		}
	}

	public void update()
	{
		if (this.transition == 0) this.currentState.update();
		else
		{
			++this.transition;
			if (this.transition == this.transitionTime) this.transition = -this.transitionTime;
			else if (this.transition == 0)
			{
				this.currentState = this.nextState;
				this.nextState = null;
			}
		}
	}
}
