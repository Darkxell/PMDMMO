package com.darkxell.client.state.menu.dungeon;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.mechanics.event.ClientEventProcessor;
import com.darkxell.client.persistance.DungeonPersistance;
import com.darkxell.client.state.menu.OptionSelectionMenuState;
import com.darkxell.common.event.dungeon.DungeonExitEvent;
import com.darkxell.common.event.dungeon.NextFloorEvent;

public class StairMenuState extends OptionSelectionMenuState
{
	private MenuOption proceed;

	public StairMenuState()
	{
		super(DungeonPersistance.dungeonState);
		this.createOptions();
	}

	@Override
	protected void createOptions()
	{
		this.tabs.add(new MenuTab("stairs.title").addOption(this.proceed = new MenuOption("stairs.proceed")).addOption(new MenuOption("stairs.cancel")));
	}

	@Override
	protected void onExit()
	{
		Launcher.stateManager.setState(this.backgroundState);
	}

	@Override
	protected void onOptionSelected(MenuOption option)
	{
		this.onExit();
		if (option == this.proceed)
		{
			if (DungeonPersistance.floor.id == DungeonPersistance.dungeon.dungeon().floorCount) ClientEventProcessor.processEvent(new DungeonExitEvent(
					DungeonPersistance.floor, DungeonPersistance.player.getDungeonPokemon()));
			else ClientEventProcessor.processEvent(new NextFloorEvent(DungeonPersistance.floor, DungeonPersistance.player));
		}
	}

}
