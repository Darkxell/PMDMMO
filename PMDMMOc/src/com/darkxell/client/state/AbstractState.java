package com.darkxell.client.state;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.darkxell.client.graphics.AbstractGraphicsLayer;
import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.ui.Keys.Key;

public abstract class AbstractState implements AbstractGraphicsLayer {

    /**
     * @return True if this State is currently the Main State (i.e. {@link StateManager#getCurrentState()} returns this
     *         State).
     */
    public boolean isMain() {
        return this == Persistence.stateManager.getCurrentState();
    }

    /** Called when this State is ended. */
    public void onEnd() {
    }

    /**
     * Called when the user presses a key.
     *
     * @see Key#UP
     */
    public abstract void onKeyPressed(Key key);

    /**
     * Called when the user releases a key.
     *
     * @see Key#UP
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
     * @param x, y - The mouse position.
     */
    public void onMouseClick(int x, int y) {
    }

    /**
     * Called when the user moves their mouse.
     *
     * @param x, y - The mouse position.
     */
    public void onMouseMove(int x, int y) {
    }

    /**
     * Called when the user right clicks.
     *
     * @param x, y - The mouse position.
     */
    public void onMouseRightClick(int x, int y) {
    }

    /** Called when this State is started. */
    public void onStart() {
    }

    /**
     * Renders the state.
     *
     * @param   width, height - Dimensions of the canvas.
     * @param g - Graphics to draw with.
     */
    public abstract void render(Graphics2D g, int width, int height);

    /** Updates the state. Called 60 times per second. */
    public abstract void update();

}
