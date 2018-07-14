package com.darkxell.client.state.menu.menus;

import java.awt.Rectangle;

import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.mainstates.PrincipalMainState;
import com.darkxell.client.state.menu.AbstractMenuState;
import com.darkxell.client.ui.Keys;
import com.darkxell.common.util.language.Message;

public class ControlsMenuState extends AbstractMenuState
{

	public static class ControlMenuOption extends MenuOption
	{
		public final short key;
		public final int oldValue;
		int newValue;

		public ControlMenuOption(Message name, short key)
		{
			super(name);
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
		// TODO Auto-generated method stub

	}

	@Override
	protected Rectangle mainWindowDimensions()
	{
		return new Rectangle(16, 32, PrincipalMainState.displayWidth - 16 * 2, PrincipalMainState.displayHeight - 32 * 2);
	}

	@Override
	protected void onExit()
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void onOptionSelected(MenuOption option)
	{
		// TODO Auto-generated method stub

	}

}
