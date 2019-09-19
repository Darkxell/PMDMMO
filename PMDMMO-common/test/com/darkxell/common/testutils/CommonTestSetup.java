package com.darkxell.common.testutils;

import com.darkxell.common.registry.Registries;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.language.Localization;

public class CommonTestSetup {
    private static boolean initialized = false;

    public static void setUp() {
        if (initialized)
            return;

        Logger.load("SERVER-TEST");
        Localization.load(false);
        Registries.load();

        initialized = true;
    }
}
