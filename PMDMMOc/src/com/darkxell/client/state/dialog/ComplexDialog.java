package com.darkxell.client.state.dialog;

import java.awt.Graphics2D;
import java.util.ArrayList;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.renderers.layers.AbstractGraphiclayer;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.dialog.DialogState.DialogEndListener;

/** Generic class for complex dialog states. */
public abstract class ComplexDialog implements DialogEndListener
{

	public static enum ComplexDialogAction
	{
		NEW_DIALOG,
		PAUSE,
		TERMINATE;
	}

	public static class DialogLoadingState extends AbstractState
	{

		public final AbstractGraphiclayer background;
		public final ComplexDialog dialog;

		public DialogLoadingState(ComplexDialog dialog)
		{
			this(dialog, dialog.background);
		}

		public DialogLoadingState(ComplexDialog dialog, AbstractGraphiclayer background)
		{
			this.dialog = dialog;
			this.background = background;
		}

		@Override
		public void onKeyPressed(short key)
		{}

		@Override
		public void onKeyReleased(short key)
		{}

		@Override
		public void onStart()
		{
			super.onStart();
			this.dialog.start();
		}

		@Override
		public void render(Graphics2D g, int width, int height)
		{
			if (this.background != null) this.background.render(g, width, height);
		}

		@Override
		public void update()
		{
			if (this.background != null) this.background.update();
		}

	}

	public final AbstractGraphiclayer background;
	private boolean isPaused = false;
	protected final ArrayList<DialogState> previousStates = new ArrayList<>();

	public ComplexDialog(AbstractGraphiclayer background)
	{
		this.background = background;
	}

	/** @return The first Dialog state to display. Should set a DialogEndListener equal to <code>this</code>. */
	public abstract DialogState firstState();

	/** @return A Loading state for this Dialog. Use for transition states. That state calls this Complex Dialog's start() method when it loads. */
	public DialogLoadingState getLoadingState()
	{
		return new DialogLoadingState(this);
	}

	public boolean isPaused()
	{
		return this.isPaused;
	}

	private void moveToNextDialog(DialogState previous)
	{
		DialogState next = this.nextState(previous);
		if (next != null) Persistance.stateManager.setState(next);
	}

	/** Utility methods for creating DialogStates.
	 * 
	 * @param screens - The Screens of the State.
	 * @return A new Dialog State. */
	protected DialogState newDialog(DialogScreen... screens)
	{
		return new DialogState(this.background, this, screens);
	}

	/** @param previous - The last Dialog state that was displayed.
	 * @return The next action to be done. */
	public abstract ComplexDialogAction nextAction(DialogState previous);

	/** @param previous - The last Dialog state that was displayed.
	 * @return The next Dialog state to display. Should set a DialogEndListener equal to <code>this</code>. */
	public abstract DialogState nextState(DialogState previous);

	@Override
	public final void onDialogEnd(DialogState dialog)
	{
		if (!this.previousStates.contains(dialog)) this.previousStates.add(dialog);
		this.onDialogFinished(dialog);
		ComplexDialogAction action = this.nextAction(dialog);
		switch (action)
		{
			case NEW_DIALOG:
				this.moveToNextDialog(dialog);
				break;
			case TERMINATE:
				Persistance.currentDialog = null;
				Persistance.stateManager.setState(this.onFinish(dialog));
				break;
			case PAUSE:
				this.isPaused = true;
				break;
		}
	}

	/** Called when a Dialog State finishes.
	 * 
	 * @param dialog - The Dialog state that just finished. */
	protected abstract void onDialogFinished(DialogState dialog);

	/** Called when the last state has been displayed.
	 * 
	 * @return The next state to set at the end of this Dialog. */
	public abstract AbstractState onFinish(DialogState lastState);

	/** Starts this Dialog. */
	public void start()
	{
		Persistance.currentDialog = this;
		DialogState first = this.firstState();
		if (first != null) Persistance.stateManager.setState(first);
	}

	public void unpause()
	{
		this.moveToNextDialog(this.previousStates.get(this.previousStates.size() - 1));
		this.isPaused = false;
	}

}
