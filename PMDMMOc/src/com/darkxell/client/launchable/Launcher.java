package com.darkxell.client.launchable;

import com.darkxell.client.discord.DiscordEventHandlerForPMDMMO;
import com.darkxell.client.mechanics.animation.Animations;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.resources.images.pokemon.PokemonSpritesets;
import com.darkxell.client.resources.music.SoundManager;
import com.darkxell.client.resources.music.SoundsHolder;
import com.darkxell.client.state.mainstates.LoginMainState;
import com.darkxell.client.ui.Frame;
import com.darkxell.common.dungeon.DungeonRegistry;
import com.darkxell.common.item.ItemRegistry;
import com.darkxell.common.move.MoveRegistry;
import com.darkxell.common.pokemon.PokemonRegistry;
import com.darkxell.common.trap.TrapRegistry;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.language.Lang;

import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;

/** Launching class of the client */
public class Launcher {

	/** Set to false to stop the game. */
	public static boolean isRunning;
	private static Renderer renderer;
	private static UpdaterAndRenderer updaterandrenderer;
	private static Updater updater;

	public static void main(String[] args) {
		isRunning = true;

		ClientSettings.load();
		Logger.loadClient();
		Lang.load();
		PokemonRegistry.load();
		MoveRegistry.load();
		ItemRegistry.load();
		TrapRegistry.load();
		DungeonRegistry.load();
		PokemonSpritesets.loadData();
		Animations.loadData();
		TextRenderer.load();
		SoundsHolder.load(".");
		Persistance.soundmanager = new SoundManager();
		Logger.instance().info("Lang & Data loaded.");

			DiscordRPC.discordInitialize("463408543572426762", DiscordEventHandlerForPMDMMO.createHandler(), true);
			DiscordRichPresence rich = new DiscordRichPresence.Builder("In logging screen").setBigImage("main_big", "")
					.build();
			DiscordRPC.discordUpdatePresence(rich);
			// DiscordRPC.discordRunCallbacks();
		} catch (Exception e) {
			Logger.e("Could not connect to discord...");
			e.printStackTrace();
		Persistance.frame = new Frame();
		Persistance.frame.canvas.requestFocus();
		Persistance.stateManager = new LoginMainState();

		setProcessingProfile(PROFILE_SYNCHRONIZED);

	}

	public static int getFps() {
		return (processingprofile == PROFILE_SYNCHRONIZED) ? updaterandrenderer.currentUPS() : renderer.currentFPS();
	}

	public static int getUps() {
		return (processingprofile == PROFILE_SYNCHRONIZED) ? updaterandrenderer.currentUPS() : updater.currentUPS();
	}

	public static void stopGame() {
		DiscordRPC.discordShutdown();
		isRunning = false;
		Logger.instance().saveClient();
		if (Persistance.isUnitTesting)
			return;
		ClientSettings.save();
		if (Persistance.saveDataOnExit) {
			PokemonRegistry.saveClient();
			MoveRegistry.saveClient();
			ItemRegistry.saveClient();
			DungeonRegistry.saveClient();
		}
	}

	public static void setProcessingProfile(byte profile) {
		if (processingprofile == profile)
			return;
		processingprofile = profile;
		switch (profile) {
		case PROFILE_SYNCHRONIZED:
			new Thread(updaterandrenderer = new UpdaterAndRenderer()).start();
			Logger.i("Processing profile switched: PROFILE_SYNCHRONIZED");
			break;
		case PROFILE_UNCAPPED:
			new Thread(updater = new Updater()).start();
			new Thread(renderer = new Renderer()).start();
			Logger.instance().debug("Processing profile switched: PROFILE_UNCAPPED");
			break;
		}
	}

	public static byte getProcessingProfile() {
		return processingprofile;
	}

	public static final byte PROFILE_SYNCHRONIZED = 0;
	public static final byte PROFILE_UNCAPPED = 1;
	public static final byte PROFILE_UNDEFINED = 99;
	private static byte processingprofile = PROFILE_UNDEFINED;

}
