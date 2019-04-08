package com.darkxell.client.launchable.messagehandlers;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.PlayerLoadingState;
import com.eclipsesource.json.JsonObject;

public class GetFriendAreasHandler extends MessageHandler {

    @Override
    public void handleMessage(JsonObject message) {
        AbstractState state = Persistence.stateManager.getCurrentState();
        if (state instanceof PlayerLoadingState) ((PlayerLoadingState) state).onFriendAreasReceived(message);
    }

}
