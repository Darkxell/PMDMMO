package com.darkxell.client.state.menu;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.dungeon.DungeonState;
import com.darkxell.client.state.menu.dungeon.InventoryMenuState;
import com.darkxell.common.util.Message;

public class DungeonMenuState extends AbstractMenuState
{

	@SuppressWarnings("unused")
	private MenuOption moves, items, team, others, ground;

	public DungeonMenuState(AbstractState background)
	{
		super(background);
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
		Launcher.stateManager.setState(this.backgroundState, 0);
	}

	@Override
	protected void onOptionSelected(MenuOption option)
	{
		DungeonState s = (DungeonState) this.backgroundState;
		if (option == this.items)
		{
			this.onExit();
			if (s.player.inventory.isEmpty() && s.player.getDungeonPokemon().tile.getItem() == null && s.player.getPokemon().getItem() == null)
			{
				s.logger.showMessage(new Message("inventory.empty"));
			} else Launcher.stateManager.setState(new InventoryMenuState(s), 0);
		} else if (option == this.ground)
		{
			this.onExit();
			if (s.player.getDungeonPokemon().tile.getItem() == null) s.logger.showMessage(new Message("ground.empty"));
			else s.logger.showMessage(new Message("ground.found").addReplacement("<item>", s.player.getDungeonPokemon().tile.getItem().item().name()));
		}
	}
}
