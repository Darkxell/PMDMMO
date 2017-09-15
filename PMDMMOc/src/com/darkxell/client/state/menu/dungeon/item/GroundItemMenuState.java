package com.darkxell.client.state.menu.dungeon.item;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.mechanics.DungeonEventProcessor;
import com.darkxell.client.persistance.DungeonPersistance;
import com.darkxell.client.renderers.ItemRenderer;
import com.darkxell.client.state.dungeon.AnimationState;
import com.darkxell.client.state.dungeon.DungeonState;
import com.darkxell.client.state.menu.OptionSelectionMenuState;
import com.darkxell.client.state.menu.dungeon.DungeonMenuState;
import com.darkxell.common.event.ItemUseEvent;
import com.darkxell.common.item.Item.ItemAction;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Message;

/** This state is never truly active. It serves as a parent to ItemActionSelectionState for an Item on the ground. */
public class GroundItemMenuState extends OptionSelectionMenuState implements ItemActionSource, ItemSelectionListener
{

	public final ItemStack item;

	public GroundItemMenuState(DungeonState parent)
	{
		super(parent);
		this.item = DungeonPersistance.player.getDungeonPokemon().tile.getItem();

		this.createOptions();
	}

	@Override
	protected void createOptions()
	{
		this.tabs.add(new MenuTab("ground").addOption(new MenuOption(this.item.name())));
	}

	@Override
	public void itemSelected(ItemStack item, int index)
	{
		if (item == null) Launcher.stateManager.setState(this);
		else
		{
			ItemStack i = DungeonPersistance.player.inventory.remove(index);
			DungeonPersistance.player.getDungeonPokemon().tile.setItem(i);
			DungeonPersistance.player.inventory.add(this.item);

			DungeonState s = DungeonPersistance.dungeonState;
			s.logger.showMessage(new Message("ground.swap").addReplacement("<item-gotten>", this.item.name()).addReplacement("<item-placed>", i.name()));
			Launcher.stateManager.setState(s);
		}
	}

	@Override
	protected void onExit()
	{
		Launcher.stateManager.setState(new DungeonMenuState(this.backgroundState));
	}

	@Override
	protected void onOptionSelected(MenuOption option)
	{}

	@Override
	public void performAction(ItemAction action)
	{
		DungeonState s = DungeonPersistance.dungeonState;
		Launcher.stateManager.setState(s);

		if (action != ItemAction.INFO) DungeonPersistance.player.getDungeonPokemon().tile.setItem(null);

		if (action == ItemAction.GET) s.logger.showMessage(new Message("ground.inventory").addReplacement("<item>", this.item.name()));
		else if (action == ItemAction.SWITCH) Launcher.stateManager.setState(new InventoryMenuState(s, this));
		else if (action == ItemAction.USE)
		{
			DungeonPokemon p = DungeonPersistance.player.getDungeonPokemon();
			s.logger.showMessage(this.item.item().getUseMessage(p));
			ItemUseEvent event = new ItemUseEvent(this.item.item(), p, null, DungeonPersistance.floor);
			DungeonEventProcessor.addToPending(event);

			AnimationState a = new AnimationState(s);
			a.animation = ItemRenderer.createItemAnimation(event, a);
			s.setSubstate(a);
		}
	}

	@Override
	public ItemStack selectedItem()
	{
		return this.item;
	}

}
