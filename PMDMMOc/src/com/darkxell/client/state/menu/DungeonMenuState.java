package com.darkxell.client.state.menu;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.state.AbstractState;
import com.darkxell.common.util.Logger;

public class DungeonMenuState extends AbstractMenuState
{

	public DungeonMenuState(AbstractState background)
	{
		super(background);
	}

	@Override
	protected void createOptions()
	{
		MenuTab tab = new MenuTab();
		tab.options.add(new MenuOption("menu.moves"));
		tab.options.add(new MenuOption("menu.items"));
		tab.options.add(new MenuOption("menu.team"));
		tab.options.add(new MenuOption("menu.others"));
		tab.options.add(new MenuOption("menu.ground"));
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
		Logger.instance().info(option.name);
	}

}
