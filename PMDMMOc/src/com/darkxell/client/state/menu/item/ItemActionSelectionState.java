package com.darkxell.client.state.menu.item;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.launchable.messagehandlers.ItemActionHandler.ItemActionMessageHandler;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.menu.OptionSelectionMenuState;
import com.darkxell.common.item.Item.ItemAction;
import com.eclipsesource.json.JsonObject;

public class ItemActionSelectionState extends OptionSelectionMenuState implements ItemActionMessageHandler
{

	private ArrayList<ItemAction> actions;
	public final ItemActionSource actionSource;
	private ArrayList<MenuOption> options;

	public ItemActionSelectionState(AbstractState backgroundState, ItemActionSource actionSource, ArrayList<ItemAction> actions)
	{
		super(backgroundState);
		this.actionSource = actionSource;
		this.actions = actions;
		this.options = new ArrayList<MenuOption>();

		ItemAction.sort(this.actions);
		this.createOptions();
	}

	@Override
	protected void createOptions()
	{
		MenuTab tab = new MenuTab();
		for (ItemAction action : this.actions)
		{
			this.options.add(new MenuOption(action.getName(this.actionSource.selectedItem())));
			tab.addOption(this.options.get(this.actions.indexOf(action)));
		}
		this.tabs.add(tab);
	}

	@Override
	public void handleMessage(JsonObject message)
	{
		if (this.actionSource instanceof ItemActionMessageHandler) ((ItemActionMessageHandler) this.actionSource).handleMessage(message);
	}

	@Override
	protected Rectangle mainWindowDimensions()
	{
		Rectangle r = super.mainWindowDimensions();
		Point p = new Point(r.x, r.y);
		if (this.backgroundState instanceof ItemContainersMenuState) p = ((ItemContainersMenuState) this.backgroundState).actionSelectionWindowLocation();
		return new Rectangle(p.x, p.y, r.width, r.height);
	}

	@Override
	protected void onExit()
	{
		Persistance.stateManager.setState(this.backgroundState);
	}

	@Override
	protected void onOptionSelected(MenuOption option)
	{
		this.actionSource.performAction(this.actions.get(this.options.indexOf(option)));
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		// If background state hasn't been drawn yet.
		if (this.getMainWindow() == null && this.backgroundState instanceof ItemContainersMenuState
				&& ((ItemContainersMenuState) this.backgroundState).getMainWindow() == null)
		{
			Shape c = g.getClip();
			g.setClip(0, 0, 0, 0);
			this.backgroundState.render(g, width, height);
			g.setClip(c);
		}

		super.render(g, width, height);
	}

}
