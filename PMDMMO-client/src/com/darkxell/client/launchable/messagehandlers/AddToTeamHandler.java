package com.darkxell.client.launchable.messagehandlers;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.state.menu.freezone.FriendActionSelectionState;
import com.eclipsesource.json.JsonObject;

public class AddToTeamHandler extends MessageHandler {

	@Override
	public void handleMessage(JsonObject message) {
		if (Persistence.stateManager.getCurrentState() instanceof FriendActionSelectionState)
			((FriendActionSelectionState) Persistence.stateManager.getCurrentState()).handleMessage(message);
	}

}
