package com.darkxell.client.launchable;

import javax.swing.JOptionPane;

import com.darkxell.client.discord.DiscordEventHandlerForPMDMMO;
import com.darkxell.client.launchable.render.*;
import com.darkxell.client.mechanics.animation.Animations;
import com.darkxell.client.resources.images.SpriteFactory;
import com.darkxell.client.resources.images.SpriteLoader;
import com.darkxell.client.resources.images.Sprites.Res_Frame;
import com.darkxell.client.resources.images.pokemon.PokemonPortrait;
import com.darkxell.client.resources.images.pokemon.PokemonSpritesets;
import com.darkxell.client.resources.music.SoundManager;
import com.darkxell.client.resources.music.SoundsHolder;
import com.darkxell.client.state.mainstates.LoginMainState;
import com.darkxell.client.ui.Frame;
import com.darkxell.common.Registries;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.language.Localization;
import com.darkxell.common.util.language.Message;

import java.io.IOException;

import static com.darkxell.client.launchable.render.RenderProfile.*;

/**
 * Launching class of the client
 */
public class Launcher {
    /**
     * Is the game running? If set to false, the game should start cleaning up resources and exit automatically by
     * the next game tick.
     */
    public static boolean isRunning;
    private static RenderProfile processingProfile = PROFILE_UNDEFINED;
    private static GameLoop renderer;
    private static GameLoop updater;

	public static void main(String[] args) {
        isRunning = true;

        ClientSettings.load();
		Logger.load("CLIENT");
		Localization.load(false);
		try {
			SpriteFactory.load();
		} catch (AssertionError e) {
			Launcher.stopGame();
			isRunning = false;
			JOptionPane.showMessageDialog(null, new Message("error.loading.sprite_factory"), new Message("error").toString(), JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		Registries.load();
        SpriteLoader.loadCommon();
        PokemonSpritesets.loadData();
        PokemonPortrait.load();
        Animations.loadData();
        SoundsHolder.load(".");
        Persistence.soundmanager = new SoundManager();
        Logger.instance().info("Lang & Data loaded.");

        DiscordEventHandlerForPMDMMO deh = new DiscordEventHandlerForPMDMMO("In logging screen", "main_big");
        deh.start();

		Persistence.frame = new Frame();
		Persistence.frame.setIconImage(Res_Frame.ICON.image());
		Persistence.frame.canvas.requestFocus();
		Persistence.stateManager = new LoginMainState();

        setProcessingProfile(PROFILE_SYNCHRONIZED);
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
		Logger.instance().saveClient();
        if (Persistence.isUnitTesting) {
            return;
        }
		ClientSettings.save();
        if (Persistence.saveDataOnExit) {
			try {
				Registries.save();
			} catch (IOException e) {
				Logger.e("Could not save registries: " + e);
				e.printStackTrace();
			}
		}
		System.exit(0);
	}

    public static void setProcessingProfile(RenderProfile profile) {
        if (processingProfile == profile) {
            return;
        }

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
