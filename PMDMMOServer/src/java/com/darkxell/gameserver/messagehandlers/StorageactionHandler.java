/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.gameserver.messagehandlers;

import com.darkxell.common.dbobject.DBInventory;
import com.darkxell.common.dbobject.DBItemstack;
import com.darkxell.common.dbobject.DBPlayer;
import com.darkxell.gameserver.GameServer;
import com.darkxell.gameserver.GameSessionHandler;
import com.darkxell.gameserver.GameSessionInfo;
import com.darkxell.gameserver.MessageHandler;
import com.darkxell.gameserver.SessionsInfoHolder;
import com.darkxell.gameserver.servermechanics.GiveManager;
import com.eclipsesource.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.websocket.Session;

/**
 *
 * @author Darkxell
 */
public class StorageactionHandler extends MessageHandler {
    
    public StorageactionHandler(GameServer endpoint) {
        super(endpoint);
    }
    
    @Override
    public void handleMessage(JsonObject json, Session from, GameSessionHandler sessionshandler) {
        GameSessionInfo si = SessionsInfoHolder.getInfo(from.getId());
        String payloadvalue = json.getString("value");
        JsonArray jsonids = json.getJsonArray("items");
        JsonArray jsonquant = json.getJsonArray("quantities");
        DBPlayer dbplayer = endpoint.getPlayerDAO().find(si.serverid);
        DBInventory dbi_from, dbi_to;
        switch (payloadvalue) {
            case "withdraw":
                dbi_from = endpoint.getInventoryDAO().find(dbplayer.storageinventory.id);
                dbi_to = endpoint.getInventoryDAO().find(dbplayer.toolboxinventory.id);
                break;
            case "deposit":
                dbi_from = endpoint.getInventoryDAO().find(dbplayer.toolboxinventory.id);
                dbi_to = endpoint.getInventoryDAO().find(dbplayer.storageinventory.id);
                break;
            default:
                return;
        }
        long[] items_ids = new long[jsonids.size()];
        long[] items_quantities = new long[jsonquant.size()];
        for (int i = 0; i < items_ids.length; i++) {
            items_ids[i] = jsonids.getJsonNumber(i).longValueExact();
        }
        for (int i = 0; i < items_quantities.length; i++) {
            items_quantities[i] = jsonquant.getJsonNumber(i).longValueExact();
        }
        if (items_ids.length != items_quantities.length) {
            return;
        }
        boolean full = false;
        // Does each part of the transaction if the item is accessible
        for (int i = 0; i < items_ids.length; i++) {
            long inventoryid = endpoint.getInventoryContains_DAO().findInventoryID(items_ids[i]);
            if (inventoryid == dbi_from.id) {
                DBItemstack sourceitemstack = endpoint.getItemstackDAO().find(items_ids[i]);
                if (sourceitemstack.quantity <= items_quantities[i] || items_quantities[i] <= 0) {
                    items_quantities[i] = sourceitemstack.quantity;
                }
                if (sourceitemstack.quantity == items_quantities[i]) {
                    endpoint.getInventoryContains_DAO().delete(sourceitemstack.id, dbi_from.id);
                    endpoint.getItemstackDAO().delete(sourceitemstack);
                } else {
                    sourceitemstack.quantity -= items_quantities[i];
                    endpoint.getItemstackDAO().update(sourceitemstack);
                }
                full = GiveManager.giveItem(sourceitemstack.itemid, (int) items_quantities[i], si, endpoint, payloadvalue.equals("deposit"));
            }
            
        }
        // Sends the result to the client
        com.eclipsesource.json.JsonObject value = Json.object();
        value.add("action", "storageconfirm");
        if (full) {
            if (payloadvalue.equals("withdraw")) {
                value.add("result", "inventoryfull");
            } else if (payloadvalue.equals("deposit")) {
                value.add("result", "storagefull");
            }
        } else {
            value.add("result", "ok");
        }
        sessionshandler.sendToSession(from, value);
    }
    
}
