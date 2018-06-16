/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.gameserver.messagehandlers;

import com.darkxell.common.dbobject.DBInventory;
import com.darkxell.common.dbobject.DBItemstack;
import com.darkxell.common.dbobject.DBLearnedmove;
import com.darkxell.common.dbobject.DBPokemon;
import com.darkxell.common.dbobject.DatabaseIdentifier;
import com.darkxell.common.item.ItemStack;
import com.darkxell.gameserver.GameServer;
import com.darkxell.gameserver.GameSessionHandler;
import com.darkxell.gameserver.GameSessionInfo;
import com.darkxell.gameserver.MessageHandler;
import com.darkxell.gameserver.SessionsInfoHolder;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import java.util.HashMap;
import javax.json.JsonObject;
import javax.websocket.Session;

/**
 *
 * @author Darkxell
 */
public class DungeonendHandler extends MessageHandler {

    public DungeonendHandler(GameServer endpoint) {
        super(endpoint);
    }

    @Override
    public void handleMessage(JsonObject json, Session from, GameSessionHandler sessionshandler) {
        GameSessionInfo si = SessionsInfoHolder.getInfo(from.getId());
        System.out.println(si.name + " finished a dungeon, processing...");
        //Gets the json values and creates the lookuptables
        com.eclipsesource.json.JsonObject jsonm = Json.parse(json.toString()).asObject();
        com.eclipsesource.json.JsonObject outcome = jsonm.get("outcome").asObject();
        com.eclipsesource.json.JsonObject player = jsonm.get("player").asObject();
        com.eclipsesource.json.JsonObject inventory = jsonm.get("inventory").asObject();
        HashMap<Long, DBPokemon> lookuptable_pokemons = new HashMap<>();
        HashMap<Long, DBLearnedmove> lookuptable_moves = new HashMap<>();
        HashMap<Long, DBItemstack> lookuptable_items = new HashMap<>();
        for (JsonValue j : jsonm.get("team").asArray()) {
            DBPokemon pkmn = new DBPokemon();
            pkmn.read(j.asObject());
            lookuptable_pokemons.put(pkmn.id, pkmn);
        }
        for (JsonValue j : jsonm.get("moves").asArray()) {
            DBLearnedmove move = new DBLearnedmove();
            move.read(j.asObject());
            lookuptable_moves.put(move.id, move);
        }
        for (JsonValue j : jsonm.get("items").asArray()) {
            DBItemstack item = new DBItemstack();
            item.read(j.asObject());
            lookuptable_items.put(item.id, item);
        }
        //Updates the player toolbox
        DBInventory toolbox_original = endpoint.getInventoryDAO().find(endpoint.getToolbox_DAO().findInventoryID(si.serverid));
        DBInventory toolbox_new = new DBInventory();
        toolbox_new.read(inventory);
        for (DatabaseIdentifier obj : toolbox_original.content) {
            if (!toolbox_new.content.contains(obj)) {
                DBItemstack tempstack = new DBItemstack();
                tempstack.id = obj.id;
                endpoint.getItemstackDAO().delete(tempstack);
                endpoint.getInventoryContains_DAO().delete(obj.id, toolbox_original.id);
            }
        }
        for (DatabaseIdentifier obj : toolbox_new.content) {
            if (obj.id <= -1) {
                long newitemid = endpoint.getItemstackDAO().create(lookuptable_items.get(obj.id));
                endpoint.getInventoryContains_DAO().create(newitemid, toolbox_original.id);
            }
        }
        //Updates the player
        //TODO
        //Updates the pokemons, their moves and held items
        //TODO
    }

}
