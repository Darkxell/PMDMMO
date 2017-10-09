package com.darkxell.client.state.menu.dungeon;

import java.util.ArrayList;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.dungeon.DungeonState;
import com.darkxell.client.state.menu.OptionSelectionMenuState;
import com.darkxell.client.state.menu.dungeon.item.ItemContainersMenuState;
import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.player.ItemContainer;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.language.Message;

public class DungeonMenuState extends OptionSelectionMenuState {

	@SuppressWarnings("unused")
	private MenuOption moves, items, team, others, ground;

	public DungeonMenuState(AbstractState background) {
		super(background);
		this.createOptions();
	}

	@Override
	protected void createOptions() {
		MenuTab tab = new MenuTab();
		tab.addOption((this.moves = new MenuOption("menu.moves")));
		tab.addOption((this.items = new MenuOption("menu.items")));
		tab.addOption((this.team = new MenuOption("menu.team")));
		tab.addOption((this.others = new MenuOption("menu.others")));
		tab.addOption((this.ground = new MenuOption("menu.ground")));
		this.tabs.add(tab);
	}

	@Override
	protected void onExit() {
		Persistance.stateManager.setState(this.backgroundState);
	}

	@Override
	protected void onOptionSelected(MenuOption option) {
		DungeonState s = Persistance.dungeonState;
		if (option == this.moves)
			Persistance.stateManager.setState(new MovesMenuState(s));
		else if (option == this.items) {
			ArrayList<ItemContainer> containers = new ArrayList<ItemContainer>();
			if (!Persistance.player.inventory.isEmpty())
				containers.add(Persistance.player.inventory);
			if (Persistance.player.getDungeonPokemon().tile.getItem() != null)
				containers.add(Persistance.player.getDungeonPokemon().tile);
			for (Pokemon pokemon : Persistance.player.getTeam())
				if (pokemon.getItem() != null)
					containers.add(pokemon);
			if (containers.isEmpty()) {
				this.onExit();
				s.logger.showMessage(new Message("inventory.empty"));
			} else
				Persistance.stateManager.setState(
						new ItemContainersMenuState(s, containers.toArray(new ItemContainer[containers.size()])));
		} else if (option == this.team)
			Persistance.stateManager.setState(new TeamMenuState(s));
		else if (option == this.ground) {
			this.onExit();
			if (Persistance.player.getDungeonPokemon().tile.type() == TileType.STAIR)
				Persistance.stateManager.setState(new StairMenuState());
			else if (Persistance.player.getDungeonPokemon().tile.getItem() == null)
				s.logger.showMessage(new Message("ground.empty"));
			else
				Persistance.stateManager
						.setState(new ItemContainersMenuState(s, Persistance.player.getDungeonPokemon().tile));
		}
	}
}
