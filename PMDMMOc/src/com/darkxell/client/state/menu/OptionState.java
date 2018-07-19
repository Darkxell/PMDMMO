package com.darkxell.client.state.menu;

import java.awt.Rectangle;

import com.darkxell.client.state.dialog.DialogState;
import com.darkxell.client.state.dialog.OptionDialogScreen;
import com.darkxell.common.util.language.Message;

public class OptionState extends OptionSelectionMenuState
{

	public final OptionDialogScreen screen;

	public OptionState(DialogState backgroundState, OptionDialogScreen screen, boolean isOpaque)
	{
		super(backgroundState, isOpaque);
		this.screen = screen;
		this.createOptions();
	}

	@Override
	protected void createOptions()
	{
		MenuTab t = new MenuTab();
		for (Message option : this.screen.options())
			t.addOption(new MenuOption(option));
		this.tabs.add(t);
	}

	@Override
	protected Rectangle mainWindowDimensions()
	{
		DialogState s = (DialogState) this.background;
		Rectangle r = super.mainWindowDimensions();
		return new Rectangle((int) s.dialogBox().getMaxX() - r.width - 5, (int) s.dialogBox().getY() - r.height - 5, r.width, r.height);
	}

	@Override
	protected void onExit()
	{}

	@Override
	protected void onOptionSelected(MenuOption option)
	{
		this.screen.onOptionSelected(this.selection);
	}

}
