package com.darkxell.common.test;

import com.darkxell.client.launchable.ClientSettings;
import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.animation.Animations;
import com.darkxell.client.resources.images.pokemon.PokemonSpritesets;
import com.darkxell.client.resources.music.SoundManager;
import com.darkxell.client.state.mainstates.PrincipalMainState;
import com.darkxell.client.ui.Frame;
import com.darkxell.common.test.tests.DungeonReplayTest;

public class ClientUnitTests
{

	private static void executeTests()
	{
		CommonUnitTests.executeTests();

		Launcher.isRunning = true;
		ClientSettings.load();
		PokemonSpritesets.loadData();
		Animations.loadData();
		Persistance.soundmanager = new SoundManager();
		Persistance.isUnitTesting = true;

		Persistance.frame = new Frame();
		Persistance.frame.canvas.requestFocus();
		Persistance.stateManager = new PrincipalMainState();

		Launcher.setProcessingProfile(Launcher.PROFILE_SYNCHRONIZED);

		new DungeonReplayTest().execute();
	}

	public static void main(String[] args)
	{
		executeTests();
	}

}
