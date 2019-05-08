package com.darkxell.client.launchable.messagehandlers;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.state.dialog.storage.StorageDialog;
import com.eclipsesource.json.JsonObject;

public class StorageConfirmHandler extends MessageHandler
{

	@Override
	public void handleMessage(JsonObject message)
	{
		if (Persistence.currentDialog != null && Persistence.currentDialog instanceof StorageDialog)
			((StorageDialog) Persistence.currentDialog).onConfirmReceived(message);
	}

}
