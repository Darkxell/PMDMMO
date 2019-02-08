package com.darkxell.client.launchable.messagehandlers;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.state.menu.freezone.MissionBoardState;
import com.eclipsesource.json.JsonObject;

public class GetMissionsHandler extends MessageHandler {

    @Override
    public void handleMessage(JsonObject message) {
        if (Persistence.stateManager.getCurrentState() instanceof MissionBoardState)
            ((MissionBoardState) Persistence.stateManager.getCurrentState()).recieveMissions(message);
    }

}
