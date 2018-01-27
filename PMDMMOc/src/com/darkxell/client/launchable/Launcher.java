package com.darkxell.client.launchable;

import com.darkxell.client.mechanics.animation.SpritesetAnimation;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.resources.images.pokemon.PokemonSpritesets;
import com.darkxell.client.resources.music.SoundManager;
import com.darkxell.client.state.mainstates.LoginMainState;
import com.darkxell.client.ui.Frame;
import com.darkxell.common.dungeon.DungeonRegistry;
import com.darkxell.common.item.ItemRegistry;
import com.darkxell.common.move.MoveRegistry;
import com.darkxell.common.pokemon.PokemonRegistry;
import com.darkxell.common.trap.TrapRegistry;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.language.Lang;

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
		Lang.loadClient();
		PokemonRegistry.loadClient();
		MoveRegistry.loadClient();
		ItemRegistry.loadClient();
		TrapRegistry.load();
		DungeonRegistry.loadClient();
		PokemonSpritesets.loadData();
		SpritesetAnimation.loadData();
		TextRenderer.load();
		Persistance.soundmanager = new SoundManager();
		Logger.instance().info("Lang & Data loaded.");

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
		isRunning = false;
		if (Persistance.saveDataOnExit) {
			PokemonRegistry.saveClient();
			MoveRegistry.saveClient();
			ItemRegistry.saveClient();
			DungeonRegistry.saveClient();
		}
		Logger.instance().saveClient();
		ClientSettings.save();
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
