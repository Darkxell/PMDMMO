package com.darkxell.client.launchable.render;

import com.darkxell.client.launchable.Persistance;

import static com.darkxell.client.launchable.render.RenderProfile.*;

public class Updater extends GameLoop {
    protected RenderProfile getProcessingProfile() {
        return PROFILE_UNCAPPED;
    }

    @Override
    protected void tick() {
	    Persistance.stateManager.update();
    }
}
