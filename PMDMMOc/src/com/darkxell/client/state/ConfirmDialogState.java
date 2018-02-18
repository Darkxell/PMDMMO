package com.darkxell.client.state;

import java.util.List;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.state.mainstates.PrincipalMainState;
import com.darkxell.client.state.menu.ConfirmState;

public class ConfirmDialogState extends DialogState
{

	private boolean confirmed = false;
	private boolean showingOptions = false;

	public ConfirmDialogState(AbstractState backgroundState, DialogEndListener listener, boolean isOpaque, List<DialogScreen> screens)
	{
		super(backgroundState, listener, isOpaque, screens);
	}

	public ConfirmDialogState(AbstractState backgroundState, DialogEndListener listener, List<DialogScreen> elements)
	{
		super(backgroundState, listener, elements);
	}

	public ConfirmDialogState(AbstractState backgroundState, DialogEndListener listener, boolean isOpaque, DialogScreen screen)
	{
		super(backgroundState, listener, isOpaque, screen);
	}

	public boolean hasConfirmed()
	{
		return this.confirmed;
	}

	@Override
	public void nextMessage()
	{
		if (this.currentScreen == this.screens.size() - 1 && !this.showingOptions)
		{
			this.showingOptions = true;
			((PrincipalMainState) Persistance.stateManager).setState(new ConfirmState(this));
		} else super.nextMessage();
	}

	public void onConfirm(boolean confirmed)
	{
		this.confirmed = confirmed;
		this.nextMessage();
	}

}
