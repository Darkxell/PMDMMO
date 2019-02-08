package com.darkxell.client.launchable.messagehandlers;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.state.mainstates.LoginMainState;
import com.eclipsesource.json.JsonObject;

public class SaltResetHandler extends MessageHandler {

    @Override
    public void handleMessage(JsonObject message) {
        if (Persistence.stateManager instanceof LoginMainState)
            ((LoginMainState) Persistence.stateManager).setSalt(message.asObject().getString("value", ""));
    }

}
