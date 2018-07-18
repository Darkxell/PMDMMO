/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.gameserver.messagehandlers;

import com.darkxell.common.dbobject.DBInventory;
import com.darkxell.common.dbobject.DBPlayer;
import com.darkxell.gameserver.GameServer;
import com.darkxell.gameserver.GameSessionHandler;
import com.darkxell.gameserver.GameSessionInfo;
import com.darkxell.gameserver.MessageHandler;
import com.darkxell.gameserver.SessionsInfoHolder;
import com.eclipsesource.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.websocket.Session;

/**
 *
 * @author Darkxell
 */
public class WithdrawmanyHandler extends MessageHandler {

    public WithdrawmanyHandler(GameServer endpoint) {
        super(endpoint);
    }

    @Override
    public void handleMessage(JsonObject json, Session from, GameSessionHandler sessionshandler) {
        GameSessionInfo si = SessionsInfoHolder.getInfo(from.getId());
        JsonArray jsonids = json.getJsonArray("items");
        DBPlayer dbplayer = endpoint.getPlayerDAO().find(si.serverid);
        DBInventory p_storage = endpoint.getInventoryDAO().find(dbplayer.storageinventory.id);
        DBInventory p_toolbox = endpoint.getInventoryDAO().find(dbplayer.toolboxinventory.id);
        long[] ids = new long[jsonids.size()];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = jsonids.getJsonNumber(i).longValueExact();
        }
        //Checks if there is enough space in the inventory for the transaction
        if (ids.length + p_toolbox.content.size() > p_toolbox.maxsize) {
            com.eclipsesource.json.JsonObject value = Json.object();
            value.add("action", "storageconfirm");
            value.add("result", "inventoryfull");
            sessionshandler.sendToSession(from, value);
            return;
        }
        // Does each part of the transaction if the item is accessible
        boolean error = false;
        for (int i = 0; i < ids.length; i++) {
            long inventoryid = endpoint.getInventoryContains_DAO().findInventoryID(ids[i]);
            if (inventoryid == p_storage.id) {
                endpoint.getInventoryContains_DAO().delete(ids[i], p_storage.id);
                endpoint.getInventoryContains_DAO().create(ids[i], p_toolbox.id);
            } else {
                error = true;
            }
        }
        // Sends the result to the client
        com.eclipsesource.json.JsonObject value = Json.object();
        value.add("action", "storageconfirm");
        value.add("result", error ? "syncerror" : "ok");
        sessionshandler.sendToSession(from, value);
    }

}
