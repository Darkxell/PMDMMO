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
                value.add("object",endpoint.getPlayerDAO().find(id).toJson());
                value.add("type","dbplayer");
                break;
            case "dbinventory":
            case "inventory":
                value.add("object",endpoint.getInventoryDAO().find(id).toJson());
                value.add("type","dbinventory");
                break;
            case "dbitemstack":
            case "itemstack":
                value.add("object",endpoint.getItemstackDAO().find(id).toJson());
                value.add("type","dbitemstack");
                break;
            case "dbpokemon":
            case "pokemon":
                value.add("object",endpoint.getPokemonDAO().find(id).toJson());
                value.add("type","dbpokemon");
                break;
            case "dblearnedmove":
            case "learnedmove":
                value.add("object",endpoint.getLearnedmoveDAO().find(id).toJson());
                value.add("type","dblearnedmove");
                break;
        }
    sessionshandler.sendToSession(from, value);
    }

}
