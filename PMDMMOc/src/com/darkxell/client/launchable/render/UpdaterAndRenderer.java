package com.darkxell.client.launchable.render;

import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.common.util.Util;

import static com.darkxell.client.launchable.render.RenderProfile.*;

/**
 * (EXPERIMENTAL) A combined updater and renderer to avoid synchronization issues between game ticks and render events.
 *
 * @see Updater
 * @see Renderer
 */
public class UpdaterAndRenderer extends GameLoop {
    protected RenderProfile getProcessingProfile() {
        return PROFILE_SYNCHRONIZED;
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
