package com.darkxell.client.state;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class StateManager {

	public static final int defaultTransitionTime = 40;

	private AbstractState currentState;
	/** The state we are currently transitioning to. */
	private AbstractState nextState;
	/**
	 * Current transition state. 0 for no transition, negative for fading in,
	 * positive for fading out.
	 */
	private int transition;
	/**
	 * The time in ticks to fade states in and out (which makes it HALF the
	 * transition time).
	 */
	private int transitionTime;

	public StateManager() {
	}

	public void onKeyPressed(short key) {
		this.currentState.onKeyPressed(key);
	}

	public void onKeyReleased(short key) {
		this.currentState.onKeyReleased(key);
	}

	public void onMouseClick(int x, int y) {
		this.currentState.onMouseClick(x, y);
	}

	public void onMouseMove(int x, int y) {
		this.currentState.onMouseMove(x, y);
	}

	public void onMouseRightClick(int x, int y) {
		this.currentState.onMouseRightClick(x, y);
	}

	private BufferedImage internalBuffer;
	private int displayWidth = (int) (256*1.6);
	private int displayHeight = (int) (192*1.6);

	/**
	 * Sets the resolution of the internal display. Bigger means a zoomed out
	 * game. Defaults at [256*192]*2 (Official DS resolution * 2).
	 */
	public void setInternalDisplaySize(int w, int h) {
		displayHeight = h;
		displayWidth = w;
		internalBuffer = null;
	}

	public void render(Graphics2D g, int width, int height) {
		if(width==0)return;
		if (internalBuffer == null)
			internalBuffer = new BufferedImage(displayWidth, displayHeight, BufferedImage.TYPE_INT_ARGB);
		// Displays the game on the buffer
		Graphics2D g2 = internalBuffer.createGraphics();
		g2.clearRect(0, 0, displayWidth, displayHeight);
		if (this.transition >= 0)
			this.currentState.render(g2, displayWidth, displayHeight);
		else if (this.transition < 0)
			this.nextState.render(g2, displayWidth, displayHeight);
		double alpha = 1d * Math.abs(this.transition) / this.transitionTime * 255;
		g2.setColor(new Color(0, 0, 0, (int) alpha));
		g2.fillRect(0, 0, width, height);
		g2.dispose();

		int tempwidth = (int) (width / 1.5);
		int tempheight = displayHeight * tempwidth / displayWidth;
		g.drawImage(internalBuffer, (width - tempwidth) / 2, (height - tempheight) / 2, tempwidth, tempheight, null);
	}

	public void setState(AbstractState state) {
		this.setState(state, defaultTransitionTime);
	}

	public void setState(AbstractState state, int transitionTime) {
		if (transitionTime == 0)
			this.currentState = state;
		else {
			this.nextState = state;
			this.transitionTime = transitionTime;
			this.transition = 1;
		}
	}

	public synchronized void update() {
		if (this.transition == 0)
			this.currentState.update();
		else {
			++this.transition;
			if (this.transition == this.transitionTime)
				this.transition = -this.transitionTime;
			else if (this.transition == 0) {
				this.currentState = this.nextState;
				this.nextState = null;
			}
		}
	}
}
