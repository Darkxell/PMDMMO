package com.darkxell.client.state.menu.dungeon;

import java.util.ArrayList;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.persistance.DungeonPersistance;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.dungeon.DungeonState;
import com.darkxell.client.state.menu.OptionSelectionMenuState;
import com.darkxell.client.state.menu.dungeon.item.ItemContainersMenuState;
import com.darkxell.common.player.ItemContainer;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.Message;

public class DungeonMenuState extends OptionSelectionMenuState
{

	@SuppressWarnings("unused")
	private MenuOption moves, items, team, others, ground;

	public DungeonMenuState(AbstractState background)
	{
		super(background);
		this.createOptions();
	}

	@Override
	protected void createOptions()
	{
		MenuTab tab = new MenuTab();
		tab.addOption((this.moves = new MenuOption("menu.moves")));
		tab.addOption((this.items = new MenuOption("menu.items")));
		tab.addOption((this.team = new MenuOption("menu.team")));
		tab.addOption((this.others = new MenuOption("menu.others")));
		tab.addOption((this.ground = new MenuOption("menu.ground")));
		this.tabs.add(tab);
	}

	@Override
	protected void onExit()
	{
		Launcher.stateManager.setState(this.backgroundState);
	}

	@Override
	protected void onOptionSelected(MenuOption option)
	{
		DungeonState s = DungeonPersistance.dungeonState;
		if (option == this.moves) Launcher.stateManager.setState(new MovesMenuState(s));
		else if (option == this.items)
		{
			ArrayList<ItemContainer> containers = new ArrayList<ItemContainer>();
			if (!DungeonPersistance.player.inventory.isEmpty()) containers.add(DungeonPersistance.player.inventory);
			if (DungeonPersistance.player.getDungeonPokemon().tile.getItem() != null) containers.add(DungeonPersistance.player.getDungeonPokemon().tile);
			for (Pokemon pokemon : DungeonPersistance.player.getTeam())
				if (pokemon.getItem() != null) containers.add(pokemon);
			if (containers.isEmpty())
			{
				this.onExit();
				s.logger.showMessage(new Message("inventory.empty"));
			} else Launcher.stateManager.setState(new ItemContainersMenuState(s, containers.toArray(new ItemContainer[containers.size()])));
		} else if (option == this.team) Launcher.stateManager.setState(new TeamMenuState(s));
		else if (option == this.ground)
		{
			this.onExit();
			if (DungeonPersistance.player.getDungeonPokemon().tile.getItem() == null) s.logger.showMessage(new Message("ground.empty"));
			else Launcher.stateManager.setState(new ItemContainersMenuState(s, DungeonPersistance.player.getDungeonPokemon().tile));
		}
	}
}
