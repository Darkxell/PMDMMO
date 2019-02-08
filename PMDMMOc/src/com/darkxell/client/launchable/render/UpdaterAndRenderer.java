package com.darkxell.client.launchable.render;

import static com.darkxell.client.launchable.render.RenderProfile.PROFILE_SYNCHRONIZED;

import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.ui.Keys;

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
        Persistence.stateManager.update();
        Keys.update();

        if (Persistence.frame == null || !Persistence.frame.isVisible())
            return;

        BufferStrategy bf = Persistence.frame.canvas.getBufferStrategy();
        Graphics2D g = (Graphics2D) bf.getDrawGraphics();
        int width = Persistence.frame.canvas.getWidth(), height = Persistence.frame.canvas.getHeight();
        g.clearRect(0, 0, width, height);

        Persistence.stateManager.render(g, width, height);
        Renderer.displayVersionWatermark(g);

        g.dispose();
        bf.show();
    }
}
