package com.darkxell.client.state.dialog.dialogs;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.dialog.ComplexDialog;
import com.darkxell.client.state.dialog.DialogScreen;
import com.darkxell.client.state.dialog.DialogState;
import com.darkxell.client.state.dialog.OptionDialogScreen;
import com.darkxell.client.state.dialog.screens.BankAmountDialogState;
import com.darkxell.client.state.dialog.screens.BankDialogScreen;
import com.darkxell.common.pokemon.PokemonRegistry;
import com.darkxell.common.pokemon.PokemonSpecies;
import com.darkxell.common.util.language.Message;

public class BankDialog extends ComplexDialog
{
	@SuppressWarnings("unused")
	private static final int DEPOSIT = 0, WITHDRAW = 1, EXIT = 2;

	private PokemonSpecies shopkeeper;

	public BankDialog(AbstractState background)
	{
		super(background);
		this.shopkeeper = PokemonRegistry.find(53);
	}

	@Override
	public DialogState firstState()
	{
		long bag = Persistance.player.getData().moneyinbag, bank = Persistance.player.getData().moneyinbank;
		return this.newDialog(new BankDialogScreen(this.shopkeeper, bag, bank,
				new Message("dialog.bank.intro").addReplacement("<bag>", Long.toString(bag)).addReplacement("<bank>", Long.toString(bank)),
				new Message("dialog.bank.deposit"), new Message("dialog.bank.withdraw"), new Message("dialog.bank.exit"))).setOpaque(true);
	}

	@Override
	public ComplexDialogAction nextAction(DialogState previous)
	{
		DialogScreen screen = previous.currentScreen();
		if (screen instanceof OptionDialogScreen)
		{
			int option = ((OptionDialogScreen) screen).chosenIndex();
			if (option == EXIT) return ComplexDialogAction.TERMINATE;
		}
		return ComplexDialogAction.NEW_DIALOG;
	}

	@Override
	public DialogState nextState(DialogState previous)
	{
		DialogScreen screen = previous.currentScreen();
		if (screen instanceof OptionDialogScreen)
		{
			int option = ((OptionDialogScreen) screen).chosenIndex();
			long bag = Persistance.player.getData().moneyinbag, bank = Persistance.player.getData().moneyinbank;
			return this.newDialog(new BankAmountDialogState(bag, bank, option == DEPOSIT));
		}
		return null;
	}

	@Override
	protected void onDialogFinished(DialogState dialog)
	{}

	@Override
	public AbstractState onFinish(DialogState lastState)
	{
		return (AbstractState) this.background;
	}

}
