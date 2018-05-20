/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.gameserver.messagehandlers;

import com.darkxell.gameserver.GameServer;
import com.darkxell.gameserver.GameSessionHandler;
import com.darkxell.gameserver.GameSessionInfo;
import com.darkxell.gameserver.MessageHandler;
import com.darkxell.gameserver.SessionsInfoHolder;
import com.darkxell.utility.RandomString;
import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.Session;

/**
 *
 * @author Darkxell
 */
public class SaltResetHandler extends MessageHandler {

    public SaltResetHandler(GameServer endpoint) {
        super(endpoint);
    }

    @Override
    public void handleMessage(JsonObject json, Session from, GameSessionHandler sessionshandler) {
        // Generates a new random salt unique for the user
        GameSessionInfo si = SessionsInfoHolder.getInfo(from.getId());
        String salt = new RandomString().nextString() + from.getId();
        si.salt = salt;
        // Sends the salt back to the user
        JsonObject value = Json.createObjectBuilder()
                .add("action", "saltreset")
                .add("value", salt)
                .build();
        sessionshandler.sendToSession(from, value);
    }

}
