package com.darkxell.client.test;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.state.mainstates.PrincipalMainState;

public class ClientSetup {
    private static boolean initialized = false;

    public static void setUp() {
        if (initialized)
            return;

        Persistence.isUnitTesting = true;
        Launcher.main(null);
        Persistence.stateManager = new PrincipalMainState();

        initialized = true;
    }
}
