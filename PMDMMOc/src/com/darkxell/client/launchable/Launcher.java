package com.darkxell.client.launchable;

import com.darkxell.client.state.StateManager;
import com.darkxell.client.state.TestState;
import com.darkxell.client.ui.Frame;
import com.darkxell.common.dungeon.DungeonRegistry;
import com.darkxell.common.item.Item;
import com.darkxell.common.item.ItemRegistry;
import com.darkxell.common.move.MoveRegistry;
import com.darkxell.common.pokemon.PokemonRegistry;
import com.darkxell.common.util.Lang;

/** Launching class of the client */
public class Launcher
{

	public static Frame frame;
	/** Set to false to stop the game. */
	static boolean isRunning;
	public static Renderer renderer;
	public static StateManager stateManager;
	public static Updater updater;

	public static void main(String[] args)
	{
		Lang.loadClient();
		PokemonRegistry.loadClient();
		MoveRegistry.loadClient();
		ItemRegistry.loadClient();
		DungeonRegistry.loadClient();
		System.out.println("Lang & Data loaded.");

		for (Item item : ItemRegistry.list())
			System.out.println(item.name() + ": " + item.getClass().getName().substring(Item.class.getName().length()));

		frame = new Frame();
		stateManager = new StateManager();
		stateManager.setState(new TestState(), 0);

		isRunning = true;
		new Thread(updater = new Updater()).start();
		new Thread(renderer = new Renderer()).start();

	}

	public static void stopGame()
	{
		isRunning = false;
	}

}
