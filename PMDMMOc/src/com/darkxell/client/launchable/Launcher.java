package com.darkxell.client.launchable;

import static com.darkxell.client.launchable.render.RenderProfile.PROFILE_SYNCHRONIZED;
import static com.darkxell.client.launchable.render.RenderProfile.PROFILE_UNDEFINED;

import java.io.IOException;

import javax.swing.JOptionPane;

import com.darkxell.client.discord.DiscordEventHandlerForPMDMMO;
import com.darkxell.client.launchable.render.GameLoop;
import com.darkxell.client.launchable.render.RenderProfile;
import com.darkxell.client.launchable.render.Renderer;
import com.darkxell.client.launchable.render.Updater;
import com.darkxell.client.launchable.render.UpdaterAndRenderer;
import com.darkxell.client.mechanics.animation.Animations;
import com.darkxell.client.resources.Res;
import com.darkxell.client.resources.image.Sprites;
import com.darkxell.client.resources.image.pokemon.body.PokemonSpritesets;
import com.darkxell.client.resources.image.pokemon.portrait.Portraits;
import com.darkxell.client.resources.image.spritefactory.PMDSpriteFactory;
import com.darkxell.client.resources.music.SoundsHolder;
import com.darkxell.client.state.mainstates.LoadingMainState;
import com.darkxell.client.ui.Frame;
import com.darkxell.common.registry.Registries;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.language.Localization;
import com.darkxell.common.util.language.Message;

/** Launching class of the client */
public class Launcher {
    /**
     * Is the game running? If set to false, the game should start cleaning up resources and exit automatically by the
     * next game tick.
     */
    public static boolean isRunning;
    private static RenderProfile processingProfile = PROFILE_UNDEFINED;
    private static GameLoop renderer;
    private static GameLoop updater;
    private static DiscordEventHandlerForPMDMMO deh;

    public static void main(String[] args) {
        isRunning = true;
        Logger.load("CLIENT");

        Persistence.frame = new Frame();
        Persistence.frame.setIconImage(Res.getBase("/hud/framebackgrounds/icon.png"));
        Persistence.frame.canvas.requestFocus();
        Persistence.stateManager = new LoadingMainState();

        Localization.load(false);
        ClientSettings.load();
        try {
            PMDSpriteFactory.initialize();
        } catch (AssertionError e) {
            isRunning = false;
            JOptionPane.showMessageDialog(null, new Message("error.loading.sprite_factory"),
                    new Message("error").toString(), JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        setProcessingProfile(PROFILE_SYNCHRONIZED);

        Registries.load();
        Sprites.load();
        PokemonSpritesets.loadData();
        Portraits.load();
        Animations.loadData();
        SoundsHolder.load("");
        Logger.instance().info("Lang & Data loaded.");

        deh = new DiscordEventHandlerForPMDMMO("In logging screen", "main_big");
        deh.start();

        Persistence.setDefaultValues();

        if (Persistence.stateManager instanceof LoadingMainState) {
            LoadingMainState stateManager = (LoadingMainState) Persistence.stateManager;
            stateManager.onLoadingFinished();
        } else {
            Logger.w("The state did not have a load callback.");
        }
    }

    private static int getTicksPerSecond(GameLoop l) {
        return l == null ? 0 : l.ticksPerSecond();
    }

    public static int getFps() {
        return getTicksPerSecond(renderer);
    }

    public static int getUps() {
        return getTicksPerSecond(updater);
    }

    public static void stopGame() {
        processingProfile = PROFILE_UNDEFINED;
        isRunning = false;
        Logger.instance().saveOnExit = !Res.RUNNING_IN_IDE;
        Logger.instance().saveClient();
        if (Persistence.isUnitTesting)
            return;
        ClientSettings.save();
        if (Persistence.saveDataOnExit)
            try {
                Registries.save();
            } catch (IOException e) {
                Logger.e("Could not save registries: " + e);
                e.printStackTrace();
            }
        if (deh != null)
            deh.stop();
        System.exit(0);
    }

    public static void setProcessingProfile(RenderProfile profile) {
        if (processingProfile == profile)
            return;

        Logger.i("Processing profile switched: " + profile.name());
        switch (processingProfile = profile) {
        case PROFILE_SYNCHRONIZED:
            new Thread(updater = renderer = new UpdaterAndRenderer()).start();
            break;
        case PROFILE_UNCAPPED:
            new Thread(updater = new Updater()).start();
            new Thread(renderer = new Renderer()).start();
            break;
        case PROFILE_UNDEFINED:
            break;
        }
    }

    public static RenderProfile getProcessingProfile() {
        return processingProfile;
    }
}
