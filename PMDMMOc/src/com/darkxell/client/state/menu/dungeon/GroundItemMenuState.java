package com.darkxell.client.state.menu.dungeon;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.state.dungeon.DungeonState;
import com.darkxell.client.state.dungeon.ItemUseState;
import com.darkxell.client.state.menu.AbstractMenuState;
import com.darkxell.client.state.menu.dungeon.item.ItemActionSource;
import com.darkxell.common.event.ItemUseEvent;
import com.darkxell.common.item.Item.ItemAction;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.player.Player;
import com.darkxell.common.util.Message;

/** This state is never truly active. It serves as a parent to ItemActionSelectionState for an Item on the ground. */
public class GroundItemMenuState extends AbstractMenuState implements ItemActionSource
{

	public final ItemStack item;
	public final Player player;

	public GroundItemMenuState(DungeonState parent)
	{
		super(parent);
		this.player = parent.player;
		this.item = this.player.getDungeonPokemon().tile.getItem();

		this.createOptions();
	}

	@Override
	protected void createOptions()
	{
		this.tabs.add(new MenuTab("ground").addOption(new MenuOption(this.item.name())));
	}

	@Override
	protected void onExit()
	{
		Launcher.stateManager.setState(new DungeonMenuState(this.backgroundState), 0);
	}

	@Override
	protected void onOptionSelected(MenuOption option)
	{}

	@Override
	public void performAction(ItemAction action)
	{
		DungeonState s = (DungeonState) this.backgroundState;
		if (action == ItemAction.SWITCH) s.logger.showMessage(new Message("Switching Items is not supported yet!", false));
		else if (action != ItemAction.INFO) this.player.getDungeonPokemon().tile.setItem(null);

		if (action == ItemAction.GET || action == ItemAction.SWITCH)
		{
			if (action == ItemAction.GET) s.logger.showMessage(new Message("ground.inventory").addReplacement("<item>", this.item.name()));
			this.player.inventory.add(this.item);
		} else if (action == ItemAction.USE)
		{
			s.logger.showMessage(this.item.item().getUseMessage(this.player.getDungeonPokemon()));
			ItemUseEvent event = this.item.item().use(s.floor, this.player.getDungeonPokemon());
			s.setSubstate(new ItemUseState(s, event));
		}

		Launcher.stateManager.setState(s, 0);
	}

	@Override
	public ItemStack selectedItem()
	{
		return this.item;
	}

}
