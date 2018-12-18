package com.darkxell.client.launchable.messagehandlers;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.state.dungeon.DungeonEndState;
import com.eclipsesource.json.JsonObject;

public class DungeonEndConfirmHandler extends MessageHandler
{

	@Override
	public void handleMessage(JsonObject message)
	{
		if (Persistence.stateManager.getCurrentState() instanceof DungeonEndState) ((DungeonEndState) Persistence.stateManager.getCurrentState()).onConfirmMessage(message);
	}

}
