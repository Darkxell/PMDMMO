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
        if (payloadvalue.equals("withdraw")) {
            dbi_from = endpoint.getInventoryDAO().find(dbplayer.storageinventory.id);
            dbi_to = endpoint.getInventoryDAO().find(dbplayer.toolboxinventory.id);
        } else if (payloadvalue.equals("deposit")) {
            dbi_from = endpoint.getInventoryDAO().find(dbplayer.toolboxinventory.id);
            dbi_to = endpoint.getInventoryDAO().find(dbplayer.storageinventory.id);
        } else {
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
        //Checks if there is enough space in the inventory for the transaction
        int space = 0;
        for (int i = 0; i < items_ids.length; i++) {
            space += items_quantities[i];
        }
        if (space + dbi_to.content.size() > dbi_to.maxsize) {
            com.eclipsesource.json.JsonObject value = Json.object();
            value.add("action", "storageconfirm");
            if (payloadvalue.equals("withdraw")) {
                value.add("result", "inventoryfull");
            } else if (payloadvalue.equals("deposit")) {
                value.add("result", "storagefull");
            }
            sessionshandler.sendToSession(from, value);
            return;
        }
        // Does each part of the transaction if the item is accessible
        for (int i = 0; i < items_ids.length; i++) {
            long inventoryid = endpoint.getInventoryContains_DAO().findInventoryID(items_ids[i]);
            DBItemstack sourceitemstack = endpoint.getItemstackDAO().find(items_ids[i]);
            if (inventoryid == dbi_from.id && sourceitemstack.quantity >= items_quantities[i] && items_quantities[i] != 0) {
                if (items_quantities[i] == sourceitemstack.quantity) {
                    endpoint.getInventoryContains_DAO().delete(items_ids[i], dbi_from.id);
                    endpoint.getInventoryContains_DAO().create(items_ids[i], dbi_to.id);
                } else {
                    sourceitemstack.quantity -= items_quantities[i];
                    endpoint.getItemstackDAO().update(sourceitemstack);
                    DBItemstack newstack = new DBItemstack(0, sourceitemstack.itemid, items_quantities[i]);
                    long newid = endpoint.getItemstackDAO().create(newstack);
                    endpoint.getInventoryContains_DAO().create(newid, dbi_to.id);
                }
            }
        }
        // Sends the result to the client
        com.eclipsesource.json.JsonObject value = Json.object();
        value.add("action", "storageconfirm");
        value.add("result", "ok");
        sessionshandler.sendToSession(from, value);
    }

}
