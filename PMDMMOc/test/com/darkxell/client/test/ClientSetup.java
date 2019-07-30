package com.darkxell.client.test;

import static com.darkxell.client.launchable.render.RenderProfile.PROFILE_SYNCHRONIZED;

import com.darkxell.client.launchable.ClientSettings;
import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.animation.Animations;
import com.darkxell.client.resources.image.pokemon.body.PokemonSpritesets;
import com.darkxell.client.resources.image.spritefactory.PMDSpriteFactory;
import com.darkxell.client.resources.music.SoundManager;
import com.darkxell.client.state.mainstates.PrincipalMainState;
import com.darkxell.client.ui.Frame;
import com.darkxell.common.Registries;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.language.Localization;

public class ClientSetup {
    private static boolean initialized = false;

    public static void setUp() {
        if (initialized)
            return;

        Launcher.isRunning = true;

        Logger.load("CLIENT-TEST");
        Localization.load(false);
        Registries.load();
        PMDSpriteFactory.initialize();
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
