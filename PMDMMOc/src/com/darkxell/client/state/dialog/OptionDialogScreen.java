package com.darkxell.client.state.dialog;

import java.util.ArrayList;
import java.util.Random;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.state.dialog.DialogState.DialogEndListener;
import com.darkxell.client.state.menu.OptionState;
import com.darkxell.common.pokemon.Pokemon;
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
		this((Pokemon) null, message, DialogPortraitLocation.BOTTOM_LEFT, options);
	}

	public OptionDialogScreen(Pokemon pokemon, Message message, DialogPortraitLocation dialogLocation, Message... options)
	{
		super(pokemon, message, dialogLocation);

		for (Message m : options)
		{
			this.options.add(m);
			this.results.add(null);
		}
	}

	public OptionDialogScreen(PokemonSpecies species, Message message, DialogPortraitLocation dialogLocation, Message... options)
	{
		this(species.generate(new Random(), 1), message, dialogLocation, options);
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

	protected OptionState getOptionSelectionState()
	{
		return new OptionState((DialogState) this.parentState, this, this.isOpaque);
	}

	public void onOptionSelected(int option)
	{
		this.chosenIndex = option;
		this.parentState.nextMessage();
	}

	public Message[] options()
	{
		return this.options.toArray(new Message[this.options.size()]);
	}

	@Override
	public boolean requestNextMessage()
	{
		if (!this.showingOptions)
		{
			this.showingOptions = true;
			Persistance.stateManager.setState(this.getOptionSelectionState());
			return false;
		} else return super.requestNextMessage();
	}

}
