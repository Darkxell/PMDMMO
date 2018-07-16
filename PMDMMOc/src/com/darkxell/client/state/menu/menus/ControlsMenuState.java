package com.darkxell.client.state.menu.menus;

import java.awt.Rectangle;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.mainstates.PrincipalMainState;
import com.darkxell.client.state.menu.AbstractMenuState;
import com.darkxell.client.state.menu.OptionSelectionMenuState;
import com.darkxell.client.ui.Keys.Key;

public class ControlsMenuState extends OptionSelectionMenuState
{

	public static class ControlMenuOption extends MenuOption
	{
		public final Key key;
		int newValue;
		public final int oldValue;

		public ControlMenuOption(Key key)
		{
			super(key.getName());
			this.key = key;
			this.newValue = this.oldValue = key.keyValue();
		}
	}

	private static final String[] tabNames = { "movement", "actions", "menus", "map" };
	private static final Key[][] tabStructure = { { Key.UP, Key.DOWN, Key.LEFT, Key.RIGHT, Key.RUN, Key.DIAGONAL, Key.ROTATE },
			{ Key.ATTACK, Key.MOVE_1, Key.MOVE_2, Key.MOVE_3, Key.MOVE_4, Key.ITEM_1, Key.ITEM_2 },
			{ Key.MENU, Key.INVENTORY, Key.PARTY, Key.PAGE_LEFT, Key.PAGE_RIGHT }, { Key.MAP_UP, Key.MAP_DOWN, Key.MAP_LEFT, Key.MAP_RIGHT, Key.MAP_RESET } };

	public final AbstractMenuState parent;

	public ControlsMenuState(AbstractMenuState parent, AbstractState backgroundState)
	{
		super(backgroundState);
		this.parent = parent;

		this.createOptions();
	}

	@Override
	protected void createOptions()
	{

		for (int tab = 0; tab < tabStructure.length; ++tab)
		{
			MenuTab t = new MenuTab("key.tab." + tabNames[tab]);
			t.name.addSuffix(" (" + (tab + 1) + "/" + tabStructure.length + ")");

			for (int menu = 0; menu < tabStructure[tab].length; ++menu)
				t.addOption(new ControlMenuOption(tabStructure[tab][menu]));

			this.tabs.add(t);
		}
	}

	@Override
	protected Rectangle mainWindowDimensions()
	{
		Rectangle r = super.mainWindowDimensions();
		return new Rectangle(r.x, r.y, PrincipalMainState.displayWidth / 2, r.height);
	}

	@Override
	protected void onExit()
	{
		Persistance.stateManager.setState(this.parent);
	}

	@Override
	protected void onOptionChanged(MenuOption option)
	{
		super.onOptionChanged(option);
	}

	@Override
	protected void onOptionSelected(MenuOption option)
	{}

}
