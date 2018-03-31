package com.darkxell.client.state;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.darkxell.common.util.Logger;

/**
 * Describes how a statemanager is supposed to work. A statemanager is expected
 * to display A very big portion of the application, like for example the game /
 * the login facilities...<br/>
 * Note that changing the statemanager removes the previous one completely, and
 * should only be done when the user does significants acts that changes the way
 * he is going to interact with the application after, like for exemple logging
 * in.
 */
public abstract class StateManager {

	public abstract void onKeyPressed(KeyEvent e, short key);

	public abstract void onKeyReleased(KeyEvent e, short key);

	public abstract void onKeyTyped(KeyEvent e);

	public abstract void onMouseClick(int x, int y);

	public abstract void onMouseMove(int x, int y);

	public abstract void onMouseRightClick(int x, int y);

	public abstract void render(Graphics2D g, int width, int height);

	public abstract void update();
	
	public void setState(AbstractState state) {
		Logger.e("Tried to call setState() on the wrong state manager!");
	}

	public AbstractState getCurrentState() {
		Logger.e("Tried to call getCurrentState() on the wrong state manager!");
		return null;
	}
	
}
