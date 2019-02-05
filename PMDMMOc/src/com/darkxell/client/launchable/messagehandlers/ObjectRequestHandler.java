package com.darkxell.client.launchable.messagehandlers;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.PlayerLoadingState;
import com.darkxell.common.util.Logger;
import com.eclipsesource.json.JsonObject;

public class ObjectRequestHandler extends MessageHandler {

    @Override
    public void handleMessage(JsonObject message) {
        if (message.get("object") == null || !message.get("object").isObject()) {
            Logger.e("Received ObjectRequest message had no object!" + message.toString());
            return;
        }

        String type = message.getString("type", null);
        if (type == null) {
            Logger.e("Received ObjectRequest type is null! " + message.toString());
            return;
        }

        switch (type) {
        case "dbplayer":
            AbstractState state = Persistence.stateManager.getCurrentState();
            if (state != null && state instanceof PlayerLoadingState)
                ((PlayerLoadingState) state).onPlayerReceived(message.get("object").asObject());
            break;

        default:
            break;
        }
    }

}
