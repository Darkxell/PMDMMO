package com.darkxell.client.launchable.messagehandlers;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.state.menu.freezone.MissionDetailsState;
import com.darkxell.common.util.Logger;
import com.eclipsesource.json.JsonObject;

public class AcceptMissionHandler extends MessageHandler {

	@Override
	public void handleMessage(JsonObject message) {
		String missioncode = message.getString("mission", "");
		int accepted = message.getInt("accepted", 0);
		if (accepted == 1) {
			Persistence.player.getMissions().add(missioncode);
			if (Persistence.stateManager.getCurrentState() instanceof MissionDetailsState)
				((MissionDetailsState) Persistence.stateManager.getCurrentState()).notifyMissionAcceptResponse(true);
			Logger.i("Accepted mission : " + missioncode);
		} else if (Persistence.stateManager.getCurrentState() instanceof MissionDetailsState)
			((MissionDetailsState) Persistence.stateManager.getCurrentState()).notifyMissionAcceptResponse(false);
	}

}
