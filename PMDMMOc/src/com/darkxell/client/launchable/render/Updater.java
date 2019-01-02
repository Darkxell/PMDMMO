package com.darkxell.client.launchable.render;

import static com.darkxell.client.launchable.render.RenderProfile.PROFILE_UNCAPPED;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.ui.Keys;

public class Updater extends GameLoop {
    protected RenderProfile getProcessingProfile() {
        return PROFILE_UNCAPPED;
    }

    @Override
    protected void tick() {
        Persistence.stateManager.update();
		Keys.update();
    }
}
