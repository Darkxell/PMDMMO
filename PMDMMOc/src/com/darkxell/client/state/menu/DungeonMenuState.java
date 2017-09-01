package com.darkxell.client.state.menu;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.state.AbstractState;
import com.darkxell.common.util.Message;

public class DungeonMenuState extends AbstractMenuState
{

	public DungeonMenuState(AbstractState background)
	{
		super(background);
	}

	@Override
	protected void createOptions()
	{
		MenuTab tab = new MenuTab(null);
		tab.options.add(new MenuOption(new Message("menu.moves")));
		tab.options.add(new MenuOption(new Message("menu.items")));
		tab.options.add(new MenuOption(new Message("menu.team")));
		tab.options.add(new MenuOption(new Message("menu.others")));
		tab.options.add(new MenuOption(new Message("menu.ground")));
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
		System.out.println(option.message);
	}

}
