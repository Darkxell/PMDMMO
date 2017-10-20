package com.darkxell.client.state.menu.freezone;

import java.util.ArrayList;
import java.util.Comparator;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.event.ClientEventProcessor;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.dungeon.DungeonState;
import com.darkxell.client.state.map.DungeonFloorMap;
import com.darkxell.client.state.menu.OptionSelectionMenuState;
import com.darkxell.common.dungeon.Dungeon;
import com.darkxell.common.dungeon.DungeonRegistry;

public class DungeonSelectionState extends OptionSelectionMenuState
{
	private static class DungeonMenuOption extends MenuOption
	{
		public final Dungeon dungeon;

		public DungeonMenuOption(Dungeon dungeon)
		{
			super(dungeon.name());
			this.dungeon = dungeon;
		}
	}

	public DungeonSelectionState(AbstractState backgroundState)
	{
		super(backgroundState);
		this.isOpaque = true;
		this.createOptions();
	}

	@Override
	protected void createOptions()
	{
		ArrayList<Dungeon> dungeons = new ArrayList<Dungeon>();
		dungeons.addAll(DungeonRegistry.list());
		dungeons.sort(Comparator.naturalOrder());
		MenuTab t = null;

		for (int d = 0; d < dungeons.size(); ++d)
		{
			if (d % 10 == 0)
			{
				t = new MenuTab("dungeon.title");
				this.tabs.add(t);
			}
			t.addOption(new DungeonMenuOption(dungeons.get(d)));
		}
	}

	@Override
	protected void onExit()
	{
		Persistance.stateManager.setState(this.backgroundState);
	}

	@Override
	protected void onOptionSelected(MenuOption option)
	{
		Persistance.dungeon = ((DungeonMenuOption) option).dungeon.newInstance();
		Persistance.eventProcessor = new ClientEventProcessor(Persistance.dungeon);
		Persistance.floor = Persistance.dungeon.currentFloor();
		Persistance.floor.generate();
		Persistance.stateManager.setState(Persistance.dungeonState = new DungeonState());
		Persistance.displaymap = new DungeonFloorMap();
		Persistance.eventProcessor.processEvents(Persistance.dungeon.currentFloor().onFloorStart());
	}

}
