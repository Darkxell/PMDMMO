package com.darkxell.client.launchable.messagehandlers;

import com.darkxell.client.launchable.Persistence;
import com.eclipsesource.json.JsonObject;

public class FreezonePositionHandler extends MessageHandler {

    @Override
    public void handleMessage(JsonObject message) {
        if (Persistence.currentmap != null)
            Persistence.currentmap.updateOtherPlayers(message);
    }

}
