package com.darkxell.client.launchable.render;

import com.darkxell.client.launchable.Persistence;

import static com.darkxell.client.launchable.render.RenderProfile.*;

public class Updater extends GameLoop {
    protected RenderProfile getProcessingProfile() {
        return PROFILE_UNCAPPED;
    }

    @Override
    protected void tick() {
        Persistence.stateManager.update();
    }
}
