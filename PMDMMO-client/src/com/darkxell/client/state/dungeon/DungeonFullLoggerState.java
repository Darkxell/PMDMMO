package com.darkxell.client.state.dungeon;

import java.awt.Graphics2D;

import com.darkxell.client.state.dungeon.DungeonState.DungeonSubState;
import com.darkxell.client.ui.Keys.Key;

public class DungeonFullLoggerState extends DungeonSubState {

    public DungeonFullLoggerState(DungeonState parent) {
        super(parent);
    }

    @Override
    public void onKeyPressed(Key key) {
        if (key == Key.RUN) {
            this.parent.setSubstate(this.parent.actionSelectionState);
            this.parent.logger.setFullscreen(false);
        }
    }

    @Override
    public void onKeyReleased(Key key) {
    }

    @Override
    public void onStart() {
        super.onStart();
        this.parent.logger.setFullscreen(true);
    }

    @Override
    public void prerender(Graphics2D g, int width, int height) {
    }

    @Override
    public void render(Graphics2D g, int width, int height) {
    }

    @Override
    public void update() {
        if (Key.UP.isPressed() && !Key.DOWN.isPressed())
            this.parent.logger.scrollUp();
        if (!Key.UP.isPressed() && Key.DOWN.isPressed())
            this.parent.logger.scrollDown();
    }

}
