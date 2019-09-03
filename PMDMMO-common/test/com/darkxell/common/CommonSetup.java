package com.darkxell.common;

import com.darkxell.common.Registries;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.language.Localization;

public class CommonSetup {
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
