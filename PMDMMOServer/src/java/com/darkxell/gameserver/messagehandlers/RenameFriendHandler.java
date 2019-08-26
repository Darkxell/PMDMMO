/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.gameserver.messagehandlers;

import com.darkxell.common.dbobject.DBPokemon;
import com.darkxell.gameserver.GameServer;
import com.darkxell.gameserver.GameSessionHandler;
import com.darkxell.gameserver.GameSessionInfo;
import com.darkxell.gameserver.MessageHandler;
import com.darkxell.gameserver.SessionsInfoHolder;
import com.eclipsesource.json.Json;
import java.util.ArrayList;
import javax.json.JsonObject;
import javax.websocket.Session;

/**
 *
 * @author Darkxell
 */
public class RenameFriendHandler extends MessageHandler {

    public RenameFriendHandler(GameServer endpoint) {
        super(endpoint);
    }

    @Override
    public void handleMessage(JsonObject json, Session from, GameSessionHandler sessionshandler) {
        GameSessionInfo si = SessionsInfoHolder.getInfo(from.getId());
        long pokemon = json.getJsonNumber("pokemonid").longValueExact();
        String name = json.getString("name", "");
        com.eclipsesource.json.JsonObject value = Json.object();
        value.add("action", "renamefriend");
        value.add("pokemonid", pokemon);
        
         // Checks that the pokemon is not in the player's team.
        ArrayList<Long> team = endpoint.getTeammember_DAO().findPokemonsIDinTeam(si.serverid);
        for (Long long1 : team) {
            if (long1 == pokemon) {
                value.add("result", "in_team");
                sessionshandler.sendToSession(from, value);
                return;
            }
        }

        // Checks that the player owns this pokemon and the name is valid.
        long realplayerid = endpoint.getTeammember_DAO().findPlayerID(pokemon);
        if ((realplayerid == 0 || realplayerid != si.serverid) && !name.equals("")) {
            value.add("result", "error");
            sessionshandler.sendToSession(from, value);
            return;
        }
        
        // Renames the pokemon if still needed.
        DBPokemon pkmn = endpoint.getPokemonDAO().find(pokemon);
        pkmn.nickname = name;
        endpoint.getPokemonDAO().update(pkmn);
        value.add("result", "success");
        sessionshandler.sendToSession(from, value);
        
    }
    
}
