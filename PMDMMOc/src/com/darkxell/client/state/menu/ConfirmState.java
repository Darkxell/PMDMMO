package com.darkxell.client.state.menu;

import java.awt.Rectangle;

import com.darkxell.client.state.ConfirmDialogState;

public class ConfirmState extends OptionSelectionMenuState
{

	public ConfirmState(ConfirmDialogState backgroundState)
	{
		super(backgroundState);
		this.createOptions();
	}

	@Override
	protected void createOptions()
	{
		this.selection = 1;
		MenuTab t = new MenuTab();
		t.addOption(new MenuOption("ui.yes"));
		t.addOption(new MenuOption("ui.no"));
		this.tabs.add(t);
	}

	@Override
	protected Rectangle mainWindowDimensions()
	{
		ConfirmDialogState s = (ConfirmDialogState) this.backgroundState;
		Rectangle r = super.mainWindowDimensions();
		return new Rectangle((int) s.dialogBox().getMaxX() - r.width - 5, (int) s.dialogBox().getY() - r.height - 5, r.width, r.height);
	}

	@Override
	protected void onExit()
	{}

	@Override
	protected void onOptionSelected(MenuOption option)
	{
		((ConfirmDialogState) this.backgroundState).onConfirm(this.optionIndex() == 0);
	}

}
