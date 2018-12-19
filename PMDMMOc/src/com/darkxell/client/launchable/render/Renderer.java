package com.darkxell.client.launchable.render;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.renderers.TextRenderer;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import static com.darkxell.client.launchable.render.RenderProfile.*;

public class Renderer extends GameLoop {
    protected RenderProfile getProcessingProfile() {
        return PROFILE_UNCAPPED;
    }

	@Override
	protected void tick() {
		BufferStrategy bf = Persistance.frame.canvas.getBufferStrategy();
		Graphics2D g = (Graphics2D) bf.getDrawGraphics();
		int width = Persistance.frame.canvas.getWidth(), height = Persistance.frame.canvas.getHeight();
		g.clearRect(0, 0, width, height);

		Persistence.stateManager.render(g, width, height);
		TextRenderer.render(g, "V " + Persistence.VERSION, 5, 5);
		
        g.dispose();
        bf.show();
    }
}
