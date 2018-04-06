package com.darkxell.client.state.menu;

import java.awt.Rectangle;

import com.darkxell.client.state.OptionDialogState;
import com.darkxell.common.util.language.Message;

public class OptionState extends OptionSelectionMenuState
{

	public OptionState(OptionDialogState backgroundState, boolean isOpaque)
	{
		super(backgroundState, isOpaque);
		this.createOptions();
	}

	@Override
	protected void createOptions()
	{
		OptionDialogState s = (OptionDialogState) this.backgroundState;
		MenuTab t = new MenuTab();
		for (Message option : s.options)
			t.addOption(new MenuOption(option));
		this.tabs.add(t);
	}

	@Override
	protected Rectangle mainWindowDimensions()
	{
		OptionDialogState s = (OptionDialogState) this.backgroundState;
		Rectangle r = super.mainWindowDimensions();
		return new Rectangle((int) s.dialogBox().getMaxX() - r.width - 5, (int) s.dialogBox().getY() - r.height - 5, r.width, r.height);
	}

	@Override
	protected void onExit()
	{}

	@Override
	protected void onOptionSelected(MenuOption option)
	{
		((OptionDialogState) this.backgroundState).onOptionSelected(this.selection);
	}

}
