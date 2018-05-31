package com.darkxell.client.state.menu.freezone;

import java.util.ArrayList;
import java.util.Arrays;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.dialog.AbstractDialogState;
import com.darkxell.client.state.dialog.AbstractDialogState.DialogEndListener;
import com.darkxell.client.state.dialog.DialogScreen;
import com.darkxell.client.state.dialog.DialogState;
import com.darkxell.client.state.menu.OptionSelectionMenuState;
import com.darkxell.client.state.menu.dungeon.TeamMenuState;
import com.darkxell.client.state.menu.item.ItemContainersMenuState;
import com.darkxell.common.player.ItemContainer;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.language.Message;

public class FreezoneMenuState extends OptionSelectionMenuState
{

	@SuppressWarnings("unused")
	private MenuOption items, team, others;

	public FreezoneMenuState(AbstractState background)
	{
		super(background);
		this.createOptions();
	}

	@Override
	protected void createOptions()
	{
		MenuTab tab = new MenuTab();
		tab.addOption((this.items = new MenuOption("menu.items")));
		tab.addOption((this.team = new MenuOption("menu.team")));
		tab.addOption((this.others = new MenuOption("menu.others")));
		this.tabs.add(tab);
	}

	@Override
	protected void onExit()
	{
		Persistance.stateManager.setState(this.backgroundState);
	}

	@Override
	protected void onOptionSelected(MenuOption option)
	{
		if (option == this.items)
		{
			ArrayList<ItemContainer> containers = new ArrayList<ItemContainer>();
			if (!Persistance.player.inventory().isEmpty()) containers.add(Persistance.player.inventory());
			for (Pokemon pokemon : Persistance.player.getTeam())
				if (pokemon.getItem() != null) containers.add(pokemon);
			if (containers.isEmpty())
			{
				this.onExit();
				FreezoneMenuState thismenu = this;
				DialogEndListener listener = new DialogEndListener() {
					@Override
					public void onDialogEnd(AbstractDialogState dialog)
					{
						Persistance.stateManager.setState(thismenu);
					}
				};
				Persistance.stateManager
						.setState(new DialogState(this.backgroundState, listener, true, Arrays.asList(new DialogScreen(new Message("inventory.empty")))));
			} else Persistance.stateManager
					.setState(new ItemContainersMenuState(this, this.backgroundState, false, containers.toArray(new ItemContainer[containers.size()])));
		} else if (option == this.team) Persistance.stateManager.setState(new TeamMenuState(this, this.backgroundState));
	}
}
