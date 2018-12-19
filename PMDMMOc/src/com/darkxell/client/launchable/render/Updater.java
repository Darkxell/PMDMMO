package com.darkxell.client.launchable.render;

import static com.darkxell.client.launchable.render.RenderProfile.PROFILE_UNCAPPED;

import com.darkxell.client.launchable.Persistence;

public class Updater extends GameLoop {
    protected RenderProfile getProcessingProfile() {
        return PROFILE_UNCAPPED;
    }

    @Override
    protected void tick() {
	    Persistence.stateManager.update();
    }
}
