package com.darkxell.client.launchable.messagehandlers;

import com.darkxell.client.launchable.Persistence;
import com.eclipsesource.json.JsonObject;

public class ItemActionHandler extends MessageHandler
{

	public static interface ItemActionMessageHandler
	{
		public void handleMessage(JsonObject message);
	}

	@Override
	public void handleMessage(JsonObject message)
	{
		if (Persistence.stateManager.getCurrentState() instanceof ItemActionMessageHandler)
			((ItemActionMessageHandler) Persistence.stateManager.getCurrentState()).handleMessage(message);
	}

}
