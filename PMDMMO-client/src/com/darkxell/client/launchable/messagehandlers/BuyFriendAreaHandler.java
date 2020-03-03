package com.darkxell.client.launchable.messagehandlers;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.state.dialog.friendarea.BuyFriendAreaActionSelectionState;
import com.eclipsesource.json.JsonObject;

public class BuyFriendAreaHandler extends MessageHandler {

    @Override
    public void handleMessage(JsonObject message) {
        if (Persistence.stateManager.getCurrentState() instanceof BuyFriendAreaActionSelectionState)
            ((BuyFriendAreaActionSelectionState) Persistence.stateManager.getCurrentState()).parent
                    .handleMessage(message);
    }

}
