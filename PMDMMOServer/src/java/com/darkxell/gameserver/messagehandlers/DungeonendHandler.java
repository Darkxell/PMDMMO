/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.gameserver.messagehandlers;

import com.darkxell.common.dbobject.DBInventory;
import com.darkxell.common.dbobject.DBItemstack;
import com.darkxell.common.dbobject.DBLearnedmove;
import com.darkxell.common.dbobject.DBPlayer;
import com.darkxell.common.dbobject.DBPokemon;
import com.darkxell.common.dbobject.DatabaseIdentifier;
import com.darkxell.gameserver.GameServer;
import com.darkxell.gameserver.GameSessionHandler;
import com.darkxell.gameserver.GameSessionInfo;
import com.darkxell.gameserver.MessageHandler;
import com.darkxell.gameserver.SessionsInfoHolder;
import com.darkxell.gameserver.servermechanics.MissionEndManager;
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
        long starttime = System.currentTimeMillis();
        System.out.println(si.name + " finished a dungeon, processing...");
        //Gets the json values and creates the lookuptables
        com.eclipsesource.json.JsonObject jsonm = Json.parse(json.toString()).asObject();
        com.eclipsesource.json.JsonObject outcome = jsonm.get("outcome").asObject();
        boolean successcheck = jsonm.getBoolean("success", false);
        com.eclipsesource.json.JsonObject player = jsonm.get("player").asObject();
        com.eclipsesource.json.JsonObject inventory = jsonm.get("inventory").asObject();
        com.eclipsesource.json.JsonArray missionscomplete = jsonm.get("completedmissions").asArray();
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
        if (toolbox_original.content != null) {
            for (DatabaseIdentifier obj : toolbox_original.content) {
                if (!toolbox_new.content.contains(obj)) {
                    DBItemstack tempstack = new DBItemstack();
                    tempstack.id = obj.id;
                    endpoint.getItemstackDAO().delete(tempstack);
                    endpoint.getInventoryContains_DAO().delete(obj.id, toolbox_original.id);
                } else {
                    endpoint.getItemstackDAO().update(lookuptable_items.get(obj.id));
                }
            }
        }
        if (toolbox_new.content != null) {
            for (DatabaseIdentifier obj : toolbox_new.content) {
                if (obj.id <= -1 || toolbox_original.content == null || !toolbox_original.content.contains(obj)) {
                    long newitemid = endpoint.getItemstackDAO().create(lookuptable_items.get(obj.id));
                    endpoint.getInventoryContains_DAO().create(newitemid, toolbox_original.id);
                }
            }
        }
        //Updates the player's money. Pokemons are not updated yet.
        DBPlayer player_original = endpoint.getPlayerDAO().find(si.serverid);
        DBPlayer player_new = new DBPlayer();
        player_new.read(player);
        player_original.moneyinbag = player_new.moneyinbag;
        endpoint.getPlayerDAO().update(player_original);
        //Updates the pokemons, their moves and held items
        updatePokemon(lookuptable_items, lookuptable_moves, lookuptable_pokemons.get(player_new.mainpokemon.id));
        if (player_new.pokemonsinparty != null) {
            for (DatabaseIdentifier pkm : player_new.pokemonsinparty) {
                updatePokemon(lookuptable_items, lookuptable_moves, lookuptable_pokemons.get(pkm.id));
            }
        }
        if (player_new.pokemonsinzones != null) {
            for (DatabaseIdentifier pkm : player_new.pokemonsinzones) {
                if (lookuptable_pokemons.containsKey(pkm.id)) {
                    updatePokemon(lookuptable_items, lookuptable_moves, lookuptable_pokemons.get(pkm.id));
                }
            }
        }
        //Updates the player's team
        endpoint.getTeammember_DAO().delete(si.serverid, player_original.mainpokemon.id);
        if (player_original.pokemonsinparty != null) {
            for (DatabaseIdentifier tdl : player_original.pokemonsinparty) {
                endpoint.getTeammember_DAO().delete(si.serverid, tdl.id);
            }
        }
        endpoint.getTeammember_DAO().create(player_original.id, lookuptable_pokemons.get(player_new.mainpokemon.id).id, (byte) 1);
        byte i = 2;
        for (JsonValue j : player.get("pokemonsinparty").asArray()) {
            endpoint.getTeammember_DAO().create(player_original.id, lookuptable_pokemons.get(j.asLong()).id, i);
            ++i;
        }
        //Manages the mission ends
        for (JsonValue jsonValue : missionscomplete) {
            MissionEndManager.manageMissionCompletion(si, endpoint, jsonValue.asString());
        }
        // Manage the player's storyposition
        manageStoryposition(successcheck, si);
        //Resets the gamesessioninfo to be in a freezone
        si.currentdungeon = -1;
        si.currentdoing = GameSessionInfo.current_freezone;
        //Sends the response to the client
        com.eclipsesource.json.JsonObject answer = Json.object();
        answer.add("action", "dungeonendconfirm");
        answer.add("outcome", jsonm.get("outcome"));
        sessionshandler.sendToSession(from, answer);
        System.out.println("Finished processing dungeonend for " + si.name + " in " + ((System.currentTimeMillis() - starttime) / 1000) + "seconds");
    }

    /**
     * Updates a pokemon that exists. This will set it's item to the correct
     * value (creating it if it's not in database)
     *
     * @return the ID of the pokemon
     */
    private long updatePokemon(HashMap<Long, DBItemstack> lookuptable_items, HashMap<Long, DBLearnedmove> lookuptable_moves, DBPokemon pokemon) {
        //Creates the pokemon if it doesn't exist yet
        if (pokemon.id <= -1) {
            pokemon.id = endpoint.getPokemonDAO().create(pokemon);
        } else {
            endpoint.getPokemonDAO().update(pokemon);
        }
        DBPokemon original = endpoint.getPokemonDAO().find(pokemon.id);
        //Updates the held item
        if (!((pokemon.holdeditem == null || pokemon.holdeditem.id == 0) && (original.holdeditem == null || original.holdeditem.id == 0))) {
            if ((pokemon.holdeditem == null || pokemon.holdeditem.id == 0) && !(original.holdeditem == null || original.holdeditem.id == 0)) {
                DBItemstack todelete = new DBItemstack();
                todelete.id = original.holdeditem.id;
                endpoint.getItemstackDAO().delete(todelete);
                endpoint.getHoldeditem_DAO().delete(todelete.id, pokemon.id);
            } else if (original.holdeditem == null || !original.holdeditem.equals(pokemon.holdeditem)) {
                long itemid = endpoint.getItemstackDAO().create(lookuptable_items.get(pokemon.holdeditem.id));
                endpoint.getHoldeditem_DAO().create(itemid, pokemon.id);
            }
        }
        //Updates the pokemon moves
        DBPokemon pokemon_original = endpoint.getPokemonDAO().find(pokemon.id);
        if (pokemon_original.learnedmoves != null) {
            for (DatabaseIdentifier mov : pokemon_original.learnedmoves) {
                if (!pokemon.learnedmoves.contains(mov)) {
                    DBLearnedmove tempmove = new DBLearnedmove();
                    tempmove.id = mov.id;
                    endpoint.getLearnedmoveDAO().delete(tempmove);
                    endpoint.getLearnedmove_DAO().delete(mov.id, pokemon.id);
                } else {
                    endpoint.getLearnedmoveDAO().update(lookuptable_moves.get(mov.id));
                }
            }
        }
        if (pokemon.learnedmoves != null) {
            for (DatabaseIdentifier mov : pokemon.learnedmoves) {
                if (mov.id <= -1) {
                    long newmoveid = endpoint.getLearnedmoveDAO().create(lookuptable_moves.get(mov.id));
                    endpoint.getLearnedmove_DAO().create(pokemon.id, newmoveid);
                }
            }
        }
        return pokemon.id;
    }

    /**
     * Manages the storyposition of the player when he ends a dungeon.
     */
    private void manageStoryposition(boolean success, GameSessionInfo si) {
        DBPlayer player = endpoint.getPlayerDAO().find(si.serverid);
        boolean needcommit = false;
        int newstoryposition = 0;
        switch (si.currentdungeon) {
            case 1:
                if (player.storyposition == 2 && success) {
                    newstoryposition = 3;
                }
                needcommit = true;
                break;
        }
        if (needcommit) {
            player.storyposition = newstoryposition;
            endpoint.getPlayerDAO().update(player);
        }
    }

}
