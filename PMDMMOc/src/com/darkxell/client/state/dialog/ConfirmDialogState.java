package com.darkxell.client.state.dialog;

import java.util.List;

import com.darkxell.client.state.AbstractState;
import com.darkxell.common.util.language.Message;

public class ConfirmDialogState extends OptionDialogState
{

	public ConfirmDialogState(AbstractState backgroundState, DialogEndListener listener, boolean isOpaque, DialogScreen screen)
	{
		super(backgroundState, listener, isOpaque, screen, new Message("ui.yes"), new Message("ui.no"));
	}

	public ConfirmDialogState(AbstractState backgroundState, DialogEndListener listener, boolean isOpaque, List<DialogScreen> screens)
	{
		super(backgroundState, listener, isOpaque, screens, new Message("ui.yes"), new Message("ui.no"));
	}

	public ConfirmDialogState(AbstractState backgroundState, DialogEndListener listener, List<DialogScreen> elements)
	{
		super(backgroundState, listener, elements, new Message("ui.yes"), new Message("ui.no"));
	}

	public boolean hasConfirmed()
	{
		return this.chosenIndex() == 0;
	}

}
