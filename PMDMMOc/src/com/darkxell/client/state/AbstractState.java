package com.darkxell.client.state;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.renderers.layers.AbstractGraphiclayer;
import com.darkxell.client.ui.Keys;
import com.darkxell.client.ui.Keys.Key;

public abstract class AbstractState extends AbstractGraphiclayer {

	/**
	 * @return True if this State is currently the Main State (i.e.
	 *         {@link StateManager#getCurrentState()} returns this State).
	 */
	public boolean isMain() {
		return this == Persistance.stateManager.getCurrentState();
	}

	/** Called when this State is ended. */
	public void onEnd() {
	}

	/**
	 * Called when the user presses a key.
	 * 
	 * @see Keys#KEY_UP
	 */
	public abstract void onKeyPressed(Key key);

	/**
	 * Called when the user releases a key.
	 * 
	 * @see Keys#KEY_UP
	 */
	public abstract void onKeyReleased(Key key);

	/**
	 * Called when the user types a char by the principalmainstate.
	 */
	public void onKeyTyped(KeyEvent e) {
	}

	/**
	 * Called when the user clicks.
	 * 
	 * @param x,
	 *            y - The mouse position.
	 */
	public void onMouseClick(int x, int y) {
	}

	/**
	 * Called when the user moves their mouse.
	 * 
	 * @param x,
	 *            y - The mouse position.
	 */
	public void onMouseMove(int x, int y) {
	}

	/**
	 * Called when the user right clicks.
	 * 
	 * @param x,
	 *            y - The mouse position.
	 */
	public void onMouseRightClick(int x, int y) {
	}

	/** Called when this State is started. */
	public void onStart() {
	}

	/**
	 * Renders the state.
	 * 
	 * @param width,
	 *            height - Dimensions of the canvas.
	 * @param g
	 *            - Graphics to draw with.
	 */
	public abstract void render(Graphics2D g, int width, int height);

	/** Updates the state. Called 60 times per second. */
	public abstract void update();

}
