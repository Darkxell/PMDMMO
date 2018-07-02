package com.darkxell.client.state.dialog.screens;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.state.dialog.DialogScreen;
import com.darkxell.client.state.menu.components.IntegerSelectionState;
import com.darkxell.client.state.menu.components.IntegerSelectionState.IntegerSelectionListener;
import com.darkxell.common.util.language.Message;

public class BankAmountDialogState extends DialogScreen implements IntegerSelectionListener
{

	public final long bag, bank;
	public final boolean depositing;
	private int selectedAmount;
	private boolean showingOptions = false;

	public BankAmountDialogState(long bag, long bank, boolean depositing)
	{
		super(new Message("dialog.bank.howmany." + (depositing ? "deposit" : "withdraw")));
		this.bag = bag;
		this.bank = bank;
		this.depositing = depositing;
	}

	@Override
	public void onIntegerSelected(int selection)
	{
		this.selectedAmount = selection;
		if (!this.depositing) this.selectedAmount *= -1;
	}

	@Override
	public boolean requestNextMessage()
	{
		if (!this.showingOptions)
		{
			this.showingOptions = true;
			long start = this.depositing ? this.bag : this.bank, min = 0;
			Persistance.stateManager.setState(new IntegerSelectionState(this.parentState, this, min, start, start));
			return false;
		} else return super.requestNextMessage();
	}

	public int selectedAmount()
	{
		return this.selectedAmount;
	}

}
