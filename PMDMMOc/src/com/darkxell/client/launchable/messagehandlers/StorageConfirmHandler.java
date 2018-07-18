package com.darkxell.client.launchable.messagehandlers;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.state.dialog.storage.StorageDialog;
import com.eclipsesource.json.JsonObject;

public class StorageConfirmHandler extends MessageHandler
{

	@Override
	public void handleMessage(JsonObject message)
	{
		if (Persistance.currentDialog instanceof StorageDialog) ((StorageDialog) Persistance.currentDialog).onConfirmReceived(message);
	}

}
