/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.gameserver.messagehandlers;

import com.darkxell.common.dbobject.DBInventory;
import com.darkxell.common.dbobject.DBPokemon;
import com.darkxell.gameserver.GameServer;
import com.darkxell.gameserver.GameSessionHandler;
import com.darkxell.gameserver.MessageHandler;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import javax.json.JsonObject;
import javax.websocket.Session;

/**
 *
 * @author Darkxell
 */
public class InventoryRequestHandler extends MessageHandler{

    public InventoryRequestHandler(GameServer endpoint) {
        super(endpoint);
    }

    @Override
    public void handleMessage(JsonObject json, Session from, GameSessionHandler sessionshandler) {
        int id = json.getJsonNumber("id").intValue();
        DBInventory inventory = endpoint.getInventoryDAO().find(id);
        
        com.eclipsesource.json.JsonObject value = Json.object();
        value.add("action", "requestinventory");
        value.add("object",inventory.toJson());
        JsonArray items = new JsonArray();
        
        for (int i = 0; i < inventory.content.size(); ++i) {
            items.add(endpoint.getItemstackDAO().find(inventory.content.get(i).id).toJson());
        }
        
        value.add("items",items);
        sessionshandler.sendToSession(from, value);
    }
    
}
