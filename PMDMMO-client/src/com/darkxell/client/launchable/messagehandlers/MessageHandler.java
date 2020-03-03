package com.darkxell.client.launchable.messagehandlers;

import com.eclipsesource.json.JsonObject;

/**
 * Abstract message handler class. Each message handler child is expected to process one kind a message type, defined by
 * it's action value.
 */
public abstract class MessageHandler {

    /**
     * Handles a message from the server. For a complete list of all messages handled by overrides of this method, check
     * the project documentation at communication.md<br/>
     * It is generally a good practice to redirect the actions done a a state to avoid desyncs. Doing so can be done
     * easely using instanceof operators on the persistance.currentstate .
     */
    public abstract void handleMessage(JsonObject message);

}
