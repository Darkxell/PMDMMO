package com.darkxell.client.mechanics.freezones.entities;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.freezones.FreezoneEntity;
import com.darkxell.client.state.dialog.dialogs.BankDialog;

public class BankEntity extends FreezoneEntity
{

	public BankEntity(double x, double y)
	{
		super(false, true, x, y);
	}

	@Override
	public void onInteract()
	{
		new BankDialog(Persistance.stateManager.getCurrentState()).start();
	}

	@Override
	public void update()
	{}

}
