package com.darkxell.client.state.dialog;

import java.util.ArrayList;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.state.dialog.AbstractDialogState.DialogEndListener;
import com.darkxell.client.state.menu.OptionState;
import com.darkxell.common.pokemon.PokemonSpecies;
import com.darkxell.common.util.language.Message;

public class OptionDialogScreen extends PokemonDialogScreen
{

	private int chosenIndex = -1;
	protected final ArrayList<Message> options = new ArrayList<>();
	protected final ArrayList<DialogEndListener> results = new ArrayList<>();
	protected boolean showingOptions = false;

	public OptionDialogScreen(Message message, Message... options)
	{
		this(null, message, options);
	}

	public OptionDialogScreen(PokemonSpecies species, Message message, Message... options)
	{
		super(species, message);

		for (Message m : options)
		{
			this.options.add(m);
			this.results.add(null);
		}
	}

	public OptionDialogScreen addOption(Message option)
	{
		return this.addOption(option, null);
	}

	public OptionDialogScreen addOption(Message option, DialogEndListener result)
	{
		this.options.add(option);
		this.results.add(result);
		return this;
	}

	public int chosenIndex()
	{
		return this.chosenIndex;
	}

	public DialogEndListener chosenOptionResult()
	{
		if (this.chosenIndex == -1) return null;
		return this.results.get(this.chosenIndex);
	}

	@Override
	public boolean requestNextMessage()
	{
		if (!this.showingOptions)
		{
			this.showingOptions = true;
			Persistance.stateManager.setState(new OptionState((DialogState) this.parentState, this, this.isOpaque));
			return false;
		} else return super.requestNextMessage();
	}

	public void onOptionSelected(int option)
	{
		this.chosenIndex = option;
		this.parentState.requestNextMessage();
	}

	public Message[] options()
	{
		return this.options.toArray(new Message[this.options.size()]);
	}

}
