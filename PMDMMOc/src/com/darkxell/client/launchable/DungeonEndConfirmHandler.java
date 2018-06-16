package com.darkxell.client.launchable;

import com.darkxell.client.launchable.messagehandlers.MessageHandler;
import com.darkxell.client.state.dungeon.DungeonEndState;
import com.eclipsesource.json.JsonObject;

public class DungeonEndConfirmHandler extends MessageHandler
{

	@Override
	public void handleMessage(JsonObject message)
	{
		if (Persistance.stateManager.getCurrentState() instanceof DungeonEndState) ((DungeonEndState) Persistance.stateManager.getCurrentState()).onConfirmMessage(message);
	}

}
