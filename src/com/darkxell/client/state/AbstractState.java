package com.darkxell.client.state;

import java.awt.Graphics2D;

import com.darkxell.client.ui.Keys;

public abstract class AbstractState
{

	/** Called when the user presses a key.
	 * 
	 * @see Keys#KEY_UP */
	public abstract void onKeyPressed(short key);

	/** Called when the user releases a key.
	 * 
	 * @see Keys#KEY_UP */
	public abstract void onKeyReleased(short key);

	/** Renders the state.
	 * 
	 * @param width, height - Dimensions of the canvas.
	 * @param g - Graphics to draw with. */
	public abstract void render(Graphics2D g, int width, int height);

	/** Updates the state. Called 60 times per second. */
	public abstract void update();

}
