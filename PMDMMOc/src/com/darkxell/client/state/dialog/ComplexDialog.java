package com.darkxell.client.state.dialog;

import java.util.ArrayList;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.dialog.DialogState.DialogEndListener;

/** Generic class for complex dialog states. */
public abstract class ComplexDialog implements DialogEndListener
{

	public final AbstractState background;
	protected final ArrayList<DialogState> previousStates = new ArrayList<>();

	public ComplexDialog(AbstractState background)
	{
		this.background = background;
	}

	/** @return The first Dialog state to display. Should set a DialogEndListener equal to <code>this</code>. */
	public abstract DialogState firstState();

	/** @param previous - The last Dialog state that was displayed.
	 * @return The next Dialog state to display. Should set a DialogEndListener equal to <code>this</code>. */
	public abstract DialogState nextState(DialogState previous);

	@Override
	public void onDialogEnd(DialogState dialog)
	{
		this.previousStates.add(dialog);
		if (this.shouldFinish(dialog)) this.onFinish(dialog);
		else
		{
			DialogState next = this.nextState(dialog);
			if (next != null) Persistance.stateManager.setState(next);
		}
	}

	/** Called when the last state has been displayed.
	 * 
	 * @return The next state to set at the end of this Dialog. */
	public abstract AbstractState onFinish(DialogState lastState);

	/** @param previous - The last Dialog state that was displayed.
	 * @return True if this Dialog is over. */
	public abstract boolean shouldFinish(DialogState previous);

	/** Starts this Dialog. */
	public void start()
	{
		DialogState first = this.firstState();
		if (first != null) Persistance.stateManager.setState(first);
	}

}
