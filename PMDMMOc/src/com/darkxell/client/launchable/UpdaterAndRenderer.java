package com.darkxell.client.launchable;

import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.resources.images.SpriteFactory;
import com.darkxell.common.util.Util;

/**
 * (EXPERIMENTAL) A combined updater and renderer to avoid synchronization issues between game ticks and render events.
 *
 * @see com.darkxell.client.launchable.Updater
 * @see com.darkxell.client.launchable.Renderer
 */
public class UpdaterAndRenderer extends GameLoop {
    protected byte getProcessingProfile() {
        return Launcher.PROFILE_SYNCHRONIZED;
    }

    @Override
    protected void tick() {
        Persistance.stateManager.update();

        if (Persistance.frame == null || !Persistance.frame.isVisible()) {
            return;
        }

        BufferStrategy bf = Persistance.frame.canvas.getBufferStrategy();
        Graphics2D g = (Graphics2D) bf.getDrawGraphics();
        int width = Persistance.frame.canvas.getWidth(), height = Persistance.frame.canvas.getHeight();
        g.clearRect(0, 0, width, height);

        Persistance.stateManager.render(g, width, height);
        TextRenderer.render(g, "V " + Util.GAME_VERSION, 5, 5);

        g.dispose();
        bf.show();
    }
}
