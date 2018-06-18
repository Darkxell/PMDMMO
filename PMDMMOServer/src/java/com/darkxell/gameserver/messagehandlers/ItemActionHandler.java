/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.gameserver.messagehandlers;

import com.darkxell.common.dbobject.DBItemstack;
import com.darkxell.common.dbobject.DBPokemon;
import com.darkxell.gameserver.GameServer;
import com.darkxell.gameserver.GameSessionHandler;
import com.darkxell.gameserver.GameSessionInfo;
import com.darkxell.gameserver.MessageHandler;
import com.darkxell.gameserver.SessionsInfoHolder;
import com.eclipsesource.json.Json;
import javax.json.JsonObject;
import javax.websocket.Session;

/**
 *
 * @author Darkxell
 */
public class ItemActionHandler extends MessageHandler {

    public ItemActionHandler(GameServer endpoint) {
        super(endpoint);
    }

    @Override
    public void handleMessage(JsonObject json, Session from, GameSessionHandler sessionshandler) {
        //Gets the json values and prepares the answer
        GameSessionInfo si = SessionsInfoHolder.getInfo(from.getId());
        String value = json.getJsonString("value").getString().toLowerCase();
        long itemid = json.getInt("item", 0);
        long pokemonid = json.getInt("pokemon", 0);
        com.eclipsesource.json.JsonObject answer = Json.object();
        answer.add("action", "itemactionresult");
        if (itemid != 0) {
            answer.add("item", itemid);
        }
        if (pokemonid != 0) {
            answer.add("pokemon", pokemonid);
        }
        //Gets instantly out if the payload caller doesn't own the item or the pokemon
        if (si.serverid != getItemOwnerId(itemid)) {
            return;
        }
        if (pokemonid != 0 && endpoint.getTeammember_DAO().findPlayerID(pokemonid) != si.serverid) {
            return;
        }
        //Processes the wanted action
        switch (value) {
            //Destroys the wanted item
            case "trash":
                deleteItem(itemid);
                answer.add("value", "trashsuccess");
                sessionshandler.sendToSession(from, answer);
                break;
            //Takes an item held by a pokemon
            case "take":
                DBPokemon dbp = endpoint.getPokemonDAO().find(pokemonid);
                if (dbp.holdeditem == null || dbp.holdeditem.id == 0) {
                    answer.add("value", "pokemonhasnoitem");
                    sessionshandler.sendToSession(from, answer);
                    return;
                }
                long destinationinventory = endpoint.getPlayerDAO().find(si.serverid).toolboxinventory.id;
                endpoint.getHoldeditem_DAO().delete(dbp.holdeditem.id, pokemonid);
                //TODO: don't add if the toolbox is full
                endpoint.getInventoryContains_DAO().create(itemid, destinationinventory);
                answer.add("value", "takesuccess");
                sessionshandler.sendToSession(from, answer);
                break;
            //Give an item to a pokemon from an inventory
            case "give":
                DBPokemon dbpk = endpoint.getPokemonDAO().find(pokemonid);
                if (!(dbpk.holdeditem == null || dbpk.holdeditem.id == 0)) {
                    answer.add("value", "pokemonhasitem");
                    sessionshandler.sendToSession(from, answer);
                    return;
                }
                long sourceinventory = endpoint.getInventoryContains_DAO().findInventoryID(itemid);
                if (sourceinventory != 0) {
                    endpoint.getInventoryContains_DAO().delete(itemid, sourceinventory);
                    endpoint.getHoldeditem_DAO().create(itemid, pokemonid);
                    answer.add("value", "givesuccess");
                    sessionshandler.sendToSession(from, answer);
                }
                break;
            default:
                answer.add("value", "invalidrequest");
                break;
        }
    }

    /**
     * Utility method that returns the ID of the player owning the item with the
     * parsed itemID.
     *
     * @return the player id, or 0 if the itemcannot be found correctly.
     */
    private long getItemOwnerId(long itemid) {
        long inventoryid = endpoint.getInventoryContains_DAO().findInventoryID(itemid);
        if (inventoryid != 0) {
            //Item has been found and is in an inventory
            long inv_toolbox = endpoint.getToolbox_DAO().findPlayerID(inventoryid);
            if (inv_toolbox != 0) {
                return inv_toolbox;
            }
            long inv_storage = endpoint.getPlayerStorage_DAO().findPlayerID(inventoryid);
            if (inv_storage != 0) {
                return inv_storage;
            }
            return 0;
        } else {
            //Item is a pokemon held item, or simply doesn't exist
            long pkmn = endpoint.getHoldeditem_DAO().findPokemonID(itemid);
            if (pkmn != 0) {
                return endpoint.getTeammember_DAO().findPlayerID(pkmn);
            }
            return 0;
        }
    }

    /**
     * Deletes the wanted item from the database.
     */
    private void deleteItem(long itemid) {
        DBItemstack itemstack = endpoint.getItemstackDAO().find(itemid);
        long matchedid = endpoint.getInventoryContains_DAO().findInventoryID(itemid);
        if (matchedid != 0) {
            endpoint.getInventoryContains_DAO().delete(itemid, matchedid);
        }
        matchedid = endpoint.getHoldeditem_DAO().findPokemonID(itemid);
        if (matchedid != 0) {
            endpoint.getHoldeditem_DAO().delete(itemid, matchedid);
        }
        endpoint.getItemstackDAO().delete(itemstack);
    }

}
