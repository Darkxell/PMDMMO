package com.darkxell.client.launchable.render;

import com.darkxell.client.launchable.Persistence;

import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import static com.darkxell.client.launchable.render.RenderProfile.*;

public class Renderer extends GameLoop {
    protected RenderProfile getProcessingProfile() {
        return PROFILE_UNCAPPED;
    }

	@Override
	protected void tick() {
		BufferStrategy bf = Persistence.frame.canvas.getBufferStrategy();
		Graphics2D g = (Graphics2D) bf.getDrawGraphics();
		int width = Persistence.frame.canvas.getWidth(), height = Persistence.frame.canvas.getHeight();
		g.clearRect(0, 0, width, height);

		Persistence.stateManager.render(g, width, height);

        g.dispose();
        bf.show();
    }
}
