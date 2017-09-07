package com.darkxell.client.state.menu.dungeon.item;

import java.util.ArrayList;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.state.dungeon.DungeonState;
import com.darkxell.client.state.menu.AbstractMenuState;
import com.darkxell.client.state.menu.DungeonMenuState;
import com.darkxell.common.item.Item.ItemAction;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.player.Inventory;
import com.darkxell.common.player.Player;
import com.darkxell.common.util.Message;

public class InventoryMenuState extends AbstractMenuState implements ItemActionSource
{

	private MenuOption held;
	public final Inventory inventory;
	private ArrayList<MenuOption> inventoryOptions;
	public final Player player;

	public InventoryMenuState(DungeonState state)
	{
		super(state);
		this.player = state.player;
		this.inventory = this.player.inventory;
		this.createOptions();
	}

	@Override
	protected void createOptions()
	{
		if (this.inventory == null) return;
		MenuTab tab = null;
		MenuOption o;
		this.inventoryOptions = new ArrayList<AbstractMenuState.MenuOption>();
		for (int i = 0; i < this.inventory.itemCount(); ++i)
		{
			if (i % 10 == 0)
			{
				tab = new MenuTab(new Message("inventory.toolbox").addReplacement("<index>", Integer.toString(i / 10 + 1)));
				this.tabs.add(tab);
			}
			o = new MenuOption(this.inventory.get(i).name());
			tab.addOption(o);
			this.inventoryOptions.add(o);
		}

		if (this.player.getPokemon().getItem() != null) this.tabs.add(new MenuTab(new Message("inventory.held").addReplacement("<pokemon>", this.player
				.getPokemon().getNickname())).addOption(this.held = new MenuOption(this.player.getPokemon().getItem().name())));
	}

	@Override
	protected void onExit()
	{
		Launcher.stateManager.setState(new DungeonMenuState(this.backgroundState), 0);
	}

	@Override
	protected void onOptionSelected(MenuOption option)
	{
		DungeonState s = (DungeonState) this.backgroundState;

		ItemStack i = this.selectedItem();
		ArrayList<ItemAction> actions = i.item().getLegalActions(true);
		actions.add(ItemAction.SET);
		if (s.player.getDungeonPokemon().tile.getItem() == null) actions.add(ItemAction.PLACE);
		else actions.add(ItemAction.SWAP);
		Launcher.stateManager.setState(new ItemActionSelectionState(this, this, actions), 0);

		/* int index = this.inventoryOptions.indexOf(option); if (index != -1) { i = this.inventory.get(index).copy(); this.inventory.remove(i.item(), i.getQuantity()); } else if (option == this.held) { i = this.player.getPokemon().getItem(); this.player.getPokemon().setItem(null); }
		 * 
		 * if (i != null) { this.player.getDungeonPokemon().tile.setItem(i); s.logger.showMessage(new Message("ground.place").addReplacement("<pokemon>", this.player.getPokemon().getNickname()).addReplacement("<item>", i.name())); } else
		 * Logger.instance().error("Item could not be found in the inventories."); */
	}

	@Override
	public void performAction(ItemAction action)
	{
		System.out.println(action.getName(this.selectedItem()));
	}

	public ItemStack selectedItem()
	{
		MenuOption option = this.currentOption();
		int index = this.inventoryOptions.indexOf(option);
		if (index != -1) return this.inventory.get(index).copy();
		else if (option == this.held) return this.player.getPokemon().getItem();
		return null;
	}

}
