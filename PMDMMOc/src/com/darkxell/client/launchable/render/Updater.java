package com.darkxell.client.launchable.render;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.launchable.Persistence;

public class Updater extends GameLoop {
    protected byte getProcessingProfile() {
        return Launcher.PROFILE_UNCAPPED;
    }

    @Override
    protected void tick() {
			Persistance.stateManager.update();
    }
}
