/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.gameserver.messagehandlers;

import com.darkxell.gameserver.GameServer;
import com.darkxell.gameserver.GameServerSafe;
import com.darkxell.gameserver.GameSessionHandler;
import com.darkxell.gameserver.GameSessionInfo;
import com.darkxell.gameserver.MessageHandler;
import com.darkxell.gameserver.SessionsInfoHolder;
import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.Session;

/**
 *
 * @author Darkxell
 */
public class PublicKeyRequestHandler extends MessageHandler{

    public PublicKeyRequestHandler(GameServer endpoint) {
        super(endpoint);
    }

    @Override
    public void handleMessage(JsonObject json, Session from, GameSessionHandler sessionshandler) {
        JsonObject value = Json.createObjectBuilder()
                .add("action", "publickeyrequest")
                .add("keybytes", GameServerSafe.getPublicKeyHexString())
                .build();
        sessionshandler.sendToSession(from, value);
    }
    
}
