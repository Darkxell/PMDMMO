package com.darkxell.client.mechanics.freezones.entities;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.freezones.FreezoneEntity;
import com.darkxell.client.state.dialog.bank.BankDialog;

public class BankEntity extends FreezoneEntity
{

	public BankEntity(double x, double y)
	{
		super(false, true, x, y);
	}

	@Override
	public void onInteract()
	{
		new BankDialog(Persistence.stateManager.getCurrentState()).start();
	}

	@Override
	public void update()
	{}

}
