package com.darkxell.client.launchable;

public class Updater extends GameLoop {
    protected byte getProcessingProfile() {
        return Launcher.PROFILE_UNCAPPED;
    }

    @Override
    protected void tick() {
			Persistance.stateManager.update();
    }
}
