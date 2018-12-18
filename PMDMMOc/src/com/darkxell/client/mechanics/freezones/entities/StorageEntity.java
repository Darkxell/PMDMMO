package com.darkxell.client.mechanics.freezones.entities;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.freezones.FreezoneEntity;
import com.darkxell.client.state.dialog.storage.StorageDialog;

public class StorageEntity extends FreezoneEntity
{

	public StorageEntity(double x, double y)
	{
		super(false, true, x, y);
	}

	@Override
	public void onInteract()
	{
		new StorageDialog(Persistence.stateManager.getCurrentState()).start();
	}

	@Override
	public void update()
	{}

}
