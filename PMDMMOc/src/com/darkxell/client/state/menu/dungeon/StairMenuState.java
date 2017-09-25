package com.darkxell.client.state.menu.dungeon;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.event.ClientEventProcessor;
import com.darkxell.client.state.menu.OptionSelectionMenuState;
import com.darkxell.common.event.dungeon.DungeonExitEvent;
import com.darkxell.common.event.dungeon.NextFloorEvent;

public class StairMenuState extends OptionSelectionMenuState {
	private MenuOption proceed;

	public StairMenuState() {
		super(Persistance.dungeonState);
		this.createOptions();
	}

	@Override
	protected void createOptions() {
		this.tabs.add(new MenuTab("stairs.title").addOption(this.proceed = new MenuOption("stairs.proceed"))
				.addOption(new MenuOption("stairs.cancel")));
	}

	@Override
	protected void onExit() {
		Persistance.stateManager.setState(this.backgroundState);
	}

	@Override
	protected void onOptionSelected(MenuOption option) {
		this.onExit();
		if (option == this.proceed) {
			if (Persistance.floor.id == Persistance.dungeon.dungeon().floorCount)
				ClientEventProcessor
						.processEvent(new DungeonExitEvent(Persistance.floor, Persistance.player.getDungeonPokemon()));
			else
				ClientEventProcessor.processEvent(new NextFloorEvent(Persistance.floor, Persistance.player));
		}
	}

}
