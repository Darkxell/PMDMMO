/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.gameserver.messagehandlers;

import com.darkxell.gameserver.GameServer;
import com.darkxell.gameserver.GameSessionHandler;
import com.darkxell.gameserver.MessageHandler;
import com.eclipsesource.json.Json;
import javax.json.JsonObject;
import javax.websocket.Session;

/**
 *
 * @author Darkxell
 */
public class ObjectrequestHandler extends MessageHandler {

    public ObjectrequestHandler(GameServer endpoint) {
        super(endpoint);
    }

    @Override
    public void handleMessage(JsonObject json, Session from, GameSessionHandler sessionshandler) {
        String type = json.getJsonString("type").getString().toLowerCase();
        int id = json.getJsonNumber("id").intValue();

        com.eclipsesource.json.JsonObject value = Json.object();
        value.add("action", "objectrequest");
        switch (type) {
            case "dbplayer":
            case "player":
                value.add("",endpoint.getPlayerDAO().find(id).toJson());
                value.add("type","dbplayer");
                break;
            case "dbinventory":
            case "inventory":
                break;
            case "dbitemstack":
            case "itemstack":
                break;
            case "dbpokemon":
            case "pokemon":
                break;
            case "dblearnedmove":
            case "learnedmove":
                break;
        }
    sessionshandler.sendToSession(from, value);
    }

}
