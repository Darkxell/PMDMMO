package com.darkxell.client.test;

import com.darkxell.client.launchable.ClientSettings;
import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.animation.Animations;
import com.darkxell.client.resources.images.SpriteFactory;
import com.darkxell.client.resources.images.pokemon.PokemonSpritesets;
import com.darkxell.client.resources.music.SoundManager;
import com.darkxell.client.state.mainstates.PrincipalMainState;
import com.darkxell.client.ui.Frame;
import com.darkxell.common.Registries;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.language.Localization;

import static com.darkxell.client.launchable.render.RenderProfile.PROFILE_SYNCHRONIZED;

public class ClientSetup {
    private static boolean initialized = false;

    public static void setUp() {
        if (initialized)
            return;

        Launcher.isRunning = true;

        Logger.load("CLIENT-TEST");
        Localization.load(false);
        Registries.load();
        SpriteFactory.load();
        ClientSettings.load();
        PokemonSpritesets.loadData();
        Animations.loadData();
        Persistence.soundmanager = new SoundManager();
        Persistence.isUnitTesting = true;

        Persistence.frame = new Frame();
        Persistence.frame.canvas.requestFocus();
        Persistence.stateManager = new PrincipalMainState();

        Launcher.setProcessingProfile(PROFILE_SYNCHRONIZED);

        initialized = true;
    }
}
