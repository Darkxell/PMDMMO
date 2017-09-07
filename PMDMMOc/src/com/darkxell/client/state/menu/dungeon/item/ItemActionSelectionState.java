package com.darkxell.client.state.menu.dungeon.item;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.menu.AbstractMenuState;
import com.darkxell.common.item.Item.ItemAction;
import com.darkxell.common.item.ItemStack;

public class ItemActionSelectionState extends AbstractMenuState
{

	private HashMap<MenuOption, ItemAction> actions;
	public final ItemActionSource actionSource;

	public ItemActionSelectionState(AbstractState backgroundState, ItemActionSource actionSource, ArrayList<ItemAction> actions)
	{
		super(backgroundState);
		this.actionSource = actionSource;
		this.actions = new HashMap<MenuOption, ItemAction>();
		ItemAction.sort(actions);

		ItemStack i = this.actionSource.selectedItem();
		for (ItemAction action : actions)
			this.actions.put(new MenuOption(action.getName(i)), action);

		this.createOptions();
	}

	@Override
	protected void createOptions()
	{
		if (this.actions == null) return;

		MenuTab tab = new MenuTab();
		for (MenuOption option : this.actions.keySet())
			tab.addOption(option);
		this.tabs.add(tab);
	}

	@Override
	protected Rectangle mainWindowDimensions(Graphics2D g)
	{
		Rectangle r = super.mainWindowDimensions(g);
		if (this.backgroundState instanceof AbstractMenuState) r.x = (int) (((AbstractMenuState) this.backgroundState).getMainWindow().dimensions.getMaxX() + 5);
		return r;
	}

	@Override
	protected void onExit()
	{
		Launcher.stateManager.setState(this.backgroundState, 0);
	}

	@Override
	protected void onOptionSelected(MenuOption option)
	{
		this.actionSource.performAction(this.actions.get(option));
	}

}
