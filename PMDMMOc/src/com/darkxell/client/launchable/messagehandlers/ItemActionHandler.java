package com.darkxell.client.launchable.messagehandlers;

import com.darkxell.client.launchable.Persistance;
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
		System.out.println(message.getString("value", null) + " : " + Persistance.stateManager.getCurrentState().getClass().getName());
		if (Persistance.stateManager.getCurrentState() instanceof ItemActionMessageHandler)
			((ItemActionMessageHandler) Persistance.stateManager.getCurrentState()).handleMessage(message);
	}

}
