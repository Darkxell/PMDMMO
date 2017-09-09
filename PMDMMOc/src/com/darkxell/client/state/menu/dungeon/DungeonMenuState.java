package com.darkxell.client.state.menu.dungeon;

import java.util.ArrayList;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.dungeon.DungeonState;
import com.darkxell.client.state.menu.AbstractMenuState;
import com.darkxell.client.state.menu.dungeon.item.InventoryMenuState;
import com.darkxell.client.state.menu.dungeon.item.ItemActionSelectionState;
import com.darkxell.common.item.Item.ItemAction;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.util.Message;

public class DungeonMenuState extends AbstractMenuState
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
		Launcher.stateManager.setState(this.backgroundState, 0);
	}

	@Override
	protected void onOptionSelected(MenuOption option)
	{
		DungeonState s = (DungeonState) this.backgroundState;
		if (option == this.items)
		{
			if (s.player.inventory.isEmpty() && s.player.getDungeonPokemon().tile.getItem() == null && s.player.getPokemon().getItem() == null)
			{
				this.onExit();
				s.logger.showMessage(new Message("inventory.empty"));
			} else Launcher.stateManager.setState(new InventoryMenuState(s), 0);
		} else if (option == this.ground)
		{
			this.onExit();
			ItemStack i = s.player.getDungeonPokemon().tile.getItem();
			if (i == null) s.logger.showMessage(new Message("ground.empty"));
			else
			{
				GroundItemMenuState ground = new GroundItemMenuState(s);

				ArrayList<ItemAction> actions = i.item().getLegalActions(true);
				if (!s.player.inventory.isFull()) actions.add(ItemAction.GET);
				actions.add(ItemAction.SWITCH);
				actions.remove(ItemAction.GIVE);

				Launcher.stateManager.setState(new ItemActionSelectionState(ground, ground, actions), 0);
			}
		}
	}
}
