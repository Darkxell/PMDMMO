package com.darkxell.client.launchable.messagehandlers;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.state.menu.freezone.MissionBoardState;
import com.eclipsesource.json.JsonObject;

public class GetMissionsHandler extends MessageHandler {

	@Override
	public void handleMessage(JsonObject message) {
		if (Persistance.stateManager.getCurrentState() instanceof MissionBoardState)
			((MissionBoardState) Persistance.stateManager.getCurrentState()).recieveMissions(message);
	}

}
