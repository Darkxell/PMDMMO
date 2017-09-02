package com.darkxell.client.launchable;

import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.resources.images.PokemonSpritesets;
import com.darkxell.client.resources.music.SoundManager;
import com.darkxell.client.state.FreezoneExploreState;
import com.darkxell.client.state.StateManager;
import com.darkxell.client.ui.Frame;
import com.darkxell.common.dungeon.DungeonRegistry;
import com.darkxell.common.item.ItemRegistry;
import com.darkxell.common.move.MoveRegistry;
import com.darkxell.common.pokemon.PokemonRegistry;
import com.darkxell.common.util.Lang;

/** Launching class of the client */
public class Launcher
{

	/** If true, data is saved on exit. */
	public static boolean SAVE_ON_EXIT = false;

	/**The sound manager of the client.*/
	public static SoundManager soundmanager;
	public static Frame frame;
	/** Set to false to stop the game. */
	static boolean isRunning;
	private static Renderer renderer;
	private static UpdaterAndRenderer updaterandrenderer;
	private static Updater updater;

	public static StateManager stateManager;

	public static void main(String[] args)
	{
		Lang.loadClient();
		PokemonRegistry.loadClient();
		MoveRegistry.loadClient();
		ItemRegistry.loadClient();
		DungeonRegistry.loadClient();
		PokemonSpritesets.loadData();
		TextRenderer.load();
		soundmanager = new SoundManager();
		System.out.println("Lang & Data loaded.");

		frame = new Frame();
		stateManager = new StateManager();
		stateManager.setState(new FreezoneExploreState(), 0);
		//Floor f = new Floor(4, Layout.STATIC, DungeonRegistry.find(1));
		//f.generate();
		//stateManager.setState(new DungeonState(f), 0);

		isRunning = true;
		setProcessingProfile(PROFILE_SYNCHRONIZED);

	}

	public static int getFps()
	{
		return (processingprofile == PROFILE_SYNCHRONIZED) ? updaterandrenderer.currentUPS() : renderer.currentFPS();
	}

	public static int getUps()
	{
		return (processingprofile == PROFILE_SYNCHRONIZED) ? updaterandrenderer.currentUPS() : updater.currentUPS();
	}

	public static void stopGame()
	{
		isRunning = false;
		if (SAVE_ON_EXIT)
		{
			PokemonRegistry.saveClient();
			MoveRegistry.saveClient();
			ItemRegistry.saveClient();
			DungeonRegistry.saveClient();
		}
	}

	public static void setProcessingProfile(byte profile)
	{
		if (processingprofile == profile) return;
		processingprofile = profile;
		switch (profile)
		{
			case PROFILE_SYNCHRONIZED:
				new Thread(updaterandrenderer = new UpdaterAndRenderer()).start();
				System.out.println("Processing profile switched: PROFILE_SYNCHRONIZED");
				break;
			case PROFILE_UNCAPPED:
				new Thread(updater = new Updater()).start();
				new Thread(renderer = new Renderer()).start();
				System.out.println("Processing profile switched: PROFILE_UNCAPPED");
				break;
		}
	}

	public static byte getProcessingProfile()
	{
		return processingprofile;
	}

	public static final byte PROFILE_SYNCHRONIZED = 0;
	public static final byte PROFILE_UNCAPPED = 1;
	public static final byte PROFILE_UNDEFINED = 99;
	private static byte processingprofile = PROFILE_UNDEFINED;

}
