package com.darkxell.client.state.menu;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.dungeon.DungeonState;
import com.darkxell.common.util.Logger;
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
		tab.options.add(this.moves = new MenuOption("menu.moves"));
		tab.options.add(this.items = new MenuOption("menu.items"));
		tab.options.add(this.team = new MenuOption("menu.team"));
		tab.options.add(this.others = new MenuOption("menu.others"));
		tab.options.add(this.ground = new MenuOption("menu.ground"));
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
		// TODO Auto-generated method stub
		if (option == this.ground)
		{
			DungeonState s = (DungeonState) this.backgroundState;
			this.onExit();
			if (s.player.getDungeonPokemon().tile.getItem() == null) s.showMessage(new Message("ground.empty"));
			else s.showMessage(new Message("ground.found").addReplacement("<item>", s.player.getDungeonPokemon().tile.getItem().item().name()));
		}
		Logger.instance().info(option.name);
	}
}
