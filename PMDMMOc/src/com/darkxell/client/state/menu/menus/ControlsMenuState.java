package com.darkxell.client.state.menu.menus;

import java.awt.Rectangle;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.mainstates.PrincipalMainState;
import com.darkxell.client.state.menu.AbstractMenuState;
import com.darkxell.client.ui.Keys;

public class ControlsMenuState extends AbstractMenuState
{

	public static class ControlMenuOption extends MenuOption
	{
		public final short key;
		public final int oldValue;
		int newValue;

		public ControlMenuOption(String nameID, short key)
		{
			super(nameID);
			this.key = key;
			this.newValue = this.oldValue = Keys.getKeyFromID(key);
		}
	}

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
		MenuTab tab = new MenuTab("menu.controls");
		for (short key = 0; key < Keys.KEY_COUNT; ++key)
			tab.addOption(new ControlMenuOption("key." + Keys.NAMES[key], key));
		this.tabs.add(tab);
	}

	@Override
	protected Rectangle mainWindowDimensions()
	{
		return new Rectangle(16, 32, PrincipalMainState.displayWidth - 16 * 2, PrincipalMainState.displayHeight - 32 * 2);
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
