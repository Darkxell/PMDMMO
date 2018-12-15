package com.darkxell.client.state.dialog.bank;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.dialog.ComplexDialog;
import com.darkxell.client.state.dialog.DialogScreen;
import com.darkxell.client.state.dialog.DialogState;
import com.darkxell.client.state.dialog.OptionDialogScreen;
import com.darkxell.client.state.dialog.PokemonDialogScreen;
import com.darkxell.common.Registries;
import com.darkxell.common.pokemon.PokemonRegistry;
import com.darkxell.common.pokemon.PokemonSpecies;
import com.darkxell.common.util.language.Message;

public class BankDialog extends ComplexDialog
{
	private static final byte CONFIRM_DIALOG = 1;
	@SuppressWarnings("unused")
	private static final int DEPOSIT = 0, WITHDRAW = 1, EXIT = 2;

	private Message[] options;
	private PokemonSpecies shopkeeper;

	public BankDialog(AbstractState background)
	{
		super(background);
		this.shopkeeper = Registries.species().find(53);
		this.options = new Message[] { new Message("dialog.bank.deposit"), new Message("dialog.bank.withdraw"), new Message("dialog.bank.exit") };
	}

	private DialogState actionSelection(boolean isFirst)
	{
		long bag = Persistance.player.getData().moneyinbag, bank = Persistance.player.getData().moneyinbank;
		return this.newDialog(new BankDialogScreen(this.shopkeeper, bag, bank, new Message(isFirst ? "dialog.bank.intro" : "dialog.bank.intro2")
				.addReplacement("<bag>", Long.toString(bag)).addReplacement("<bank>", Long.toString(bank)), this.options)).setOpaque(true);
	}

	@Override
	public DialogState firstState()
	{
		return this.actionSelection(true);
	}

	@Override
	public ComplexDialogAction nextAction(DialogState previous)
	{
		DialogScreen screen = previous.currentScreen();
		if (screen instanceof OptionDialogScreen)
		{
			int option = ((OptionDialogScreen) screen).chosenIndex();
			if (option == EXIT || option == -1) return ComplexDialogAction.TERMINATE;
		}
		return ComplexDialogAction.NEW_DIALOG;
	}

	@Override
	public DialogState nextState(DialogState previous)
	{
		DialogScreen screen = previous.currentScreen();
		if (screen.id == CONFIRM_DIALOG) return this.actionSelection(false);
		else if (screen instanceof OptionDialogScreen)
		{
			int option = ((OptionDialogScreen) screen).chosenIndex();
			long bag = Persistance.player.getData().moneyinbag, bank = Persistance.player.getData().moneyinbank;
			return this.newDialog(new BankAmountDialogScreen(shopkeeper, bag, bank, option == DEPOSIT)).setOpaque(true);
		}
		return null;
	}

	public void onConfirmReceived(long bag, long bank)
	{
		if (!Persistance.isCommunicating) return;
		Persistance.isCommunicating = false;
		Persistance.player.setMoneyInBag(bag);
		Persistance.player.setMoneyInBank(bank);
		Persistance.stateManager
				.setState(this.newDialog(new PokemonDialogScreen(this.shopkeeper, new Message("dialog.bank.confirm")).setID(CONFIRM_DIALOG)).setOpaque(true));
	}

	@Override
	protected void onDialogFinished(DialogState dialog)
	{}

	@Override
	public AbstractState onFinish(DialogState lastState)
	{
		return (AbstractState) this.background;
	}

	public void onSelectionCancel()
	{
		Persistance.stateManager.setState(this.actionSelection(false));
	}

}
