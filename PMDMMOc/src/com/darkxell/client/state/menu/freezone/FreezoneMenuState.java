package com.darkxell.client.state.menu.freezone;

import java.util.ArrayList;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.dialog.DialogScreen;
import com.darkxell.client.state.dialog.DialogState;
import com.darkxell.client.state.dialog.DialogState.DialogEndListener;
import com.darkxell.client.state.menu.OptionSelectionMenuState;
import com.darkxell.client.state.menu.item.ItemContainersMenuState;
import com.darkxell.client.state.menu.menus.SettingsMenuState;
import com.darkxell.client.state.menu.menus.TeamMenuState;
import com.darkxell.common.player.ItemContainer;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.language.Message;

public class FreezoneMenuState extends OptionSelectionMenuState
{

	private MenuOption items, team, settings;

	public FreezoneMenuState(AbstractState background)
	{
		super(background);
		this.isOpaque = true;
		this.createOptions();
	}

	@Override
	protected void createOptions()
	{
		MenuTab tab = new MenuTab();
		tab.addOption((this.items = new MenuOption("menu.items")));
		tab.addOption((this.team = new MenuOption("menu.team")));
		tab.addOption((this.settings = new MenuOption("menu.settings")));
		this.tabs.add(tab);
	}

	@Override
	protected void onExit()
	{
		Persistence.stateManager.setState((AbstractState) this.background);
	}

	@Override
	protected void onOptionSelected(MenuOption option)
	{
		if (option == this.items)
		{
			ArrayList<ItemContainer> containers = new ArrayList<ItemContainer>();
			containers.add(Persistence.player.inventory());
			for (Pokemon pokemon : Persistence.player.getTeam())
				containers.add(pokemon);

			boolean found = false;
			for (ItemContainer container : containers)
				if (container.size() != 0)
				{
					found = true;
					break;
				}

			if (!found)
			{
				this.onExit();
				FreezoneMenuState thismenu = this;
				DialogEndListener listener = new DialogEndListener() {
					@Override
					public void onDialogEnd(DialogState dialog)
					{
						Persistence.stateManager.setState(thismenu);
					}
				};
				Persistence.stateManager.setState(new DialogState(this.background, listener, new DialogScreen(new Message("inventory.empty"))));
			} else
			{
				ItemContainersMenuState s = new ItemContainersMenuState(this, this.background, false, containers.toArray(new ItemContainer[containers.size()]));
				s.isOpaque = this.isOpaque;
				Persistence.stateManager.setState(s);
			}
		} else if (option == this.team) Persistence.stateManager.setState(new TeamMenuState(this, this.background).setOpaque(this.isOpaque));
		else if (option == this.settings) Persistence.stateManager.setState(new SettingsMenuState(this, this.background).setOpaque(this.isOpaque));
	}
}
