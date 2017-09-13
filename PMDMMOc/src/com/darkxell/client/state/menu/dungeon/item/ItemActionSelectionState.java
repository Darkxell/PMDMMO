package com.darkxell.client.state.menu.dungeon.item;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.menu.AbstractMenuState;
import com.darkxell.common.item.Item.ItemAction;

public class ItemActionSelectionState extends AbstractMenuState {

	private ArrayList<ItemAction> actions;
	public final ItemActionSource actionSource;
	private ArrayList<MenuOption> options;

	public ItemActionSelectionState(AbstractState backgroundState, ItemActionSource actionSource,
			ArrayList<ItemAction> actions) {
		super(backgroundState);
		this.actionSource = actionSource;
		this.actions = actions;
		this.options = new ArrayList<MenuOption>();

		ItemAction.sort(this.actions);
		this.createOptions();
	}

	@Override
	protected void createOptions() {
		MenuTab tab = new MenuTab();
		for (ItemAction action : this.actions) {
			this.options.add(new MenuOption(action.getName(this.actionSource.selectedItem())));
			tab.addOption(this.options.get(this.actions.indexOf(action)));
		}
		this.tabs.add(tab);
	}

	@Override
	protected Rectangle mainWindowDimensions(Graphics2D g) {
		Rectangle r = super.mainWindowDimensions(g);
		if (this.backgroundState instanceof AbstractMenuState)
			r.x = (int) (((AbstractMenuState) this.backgroundState).getMainWindow().dimensions.getMaxX() + 5);
		return r;
	}

	@Override
	protected void onExit() {
		Launcher.stateManager.setState(this.backgroundState);
	}

	@Override
	protected void onOptionSelected(MenuOption option) {
		this.actionSource.performAction(this.actions.get(this.options.indexOf(option)));
	}

	@Override
	public void render(Graphics2D g, int width, int height) {
		// If background state hasn't been drawn yet.
		if (this.getMainWindow() == null && this.backgroundState instanceof AbstractMenuState
				&& ((AbstractMenuState) this.backgroundState).getMainWindow() == null) {
			Shape c = g.getClip();
			g.setClip(0, 0, 0, 0);
			this.backgroundState.render(g, width, height);
			g.setClip(c);
		}

		super.render(g, width, height);
	}

}
