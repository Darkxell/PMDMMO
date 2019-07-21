package com.darkxell.client.launchable.render;

import static com.darkxell.client.launchable.render.RenderProfile.PROFILE_UNCAPPED;

import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.renderers.TextRenderer;

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
		TextRenderer.render(g, "V " + Persistence.VERSION, 5, 5);
		
        g.dispose();
        bf.show();
    }
}
