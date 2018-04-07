package com.darkxell.client.state.dialog;

import java.util.List;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.menu.OptionState;
import com.darkxell.common.util.language.Message;

public class OptionDialogState extends DialogState
{

	private int chosenIndex = -1;
	public final Message[] options;
	private boolean showingOptions = false;

	public OptionDialogState(AbstractState backgroundState, DialogEndListener listener, boolean isOpaque, DialogScreen screen, Message... options)
	{
		super(backgroundState, listener, isOpaque, screen);
		this.options = options;
	}

	public OptionDialogState(AbstractState backgroundState, DialogEndListener listener, boolean isOpaque, List<DialogScreen> screens, Message... options)
	{
		super(backgroundState, listener, isOpaque, screens);
		this.options = options;
	}

	public OptionDialogState(AbstractState backgroundState, DialogEndListener listener, List<DialogScreen> elements, Message... options)
	{
		super(backgroundState, listener, elements);
		this.options = options;
	}

	public int chosenIndex()
	{
		return this.chosenIndex;
	}

	@Override
	public void nextMessage()
	{
		if (this.currentScreen == this.screens.size() - 1 && !this.showingOptions)
		{
			this.showingOptions = true;
			Persistance.stateManager.setState(new OptionState(this, this.isOpaque));
		} else super.nextMessage();
	}

	public void onOptionSelected(int option)
	{
		this.chosenIndex = option;
		this.nextMessage();
	}

}
