package com.darkxell.client.launchable;

import com.darkxell.client.state.StateManager;
import com.darkxell.client.state.TestState;
import com.darkxell.client.ui.Frame;
import com.darkxell.common.move.MoveRegistry;
import com.darkxell.common.pokemon.PokemonRegistry;

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
		PokemonRegistry.loadClient();
		MoveRegistry.loadClient();
		System.out.println("Data loaded.");

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
