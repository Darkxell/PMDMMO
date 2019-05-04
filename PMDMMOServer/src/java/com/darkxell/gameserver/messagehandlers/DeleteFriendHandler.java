/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.gameserver.messagehandlers;

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
public class DeleteFriendHandler extends MessageHandler {

    public DeleteFriendHandler(GameServer endpoint) {
        super(endpoint);
    }

    @Override
    public void handleMessage(JsonObject json, Session from, GameSessionHandler sessionshandler) {
        GameSessionInfo si = SessionsInfoHolder.getInfo(from.getId());
        long pokemon = json.getJsonNumber("pokemonid").longValueExact();
        com.eclipsesource.json.JsonObject value = Json.object();
        value.add("action", "deletefriend");
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

        // Checks that the player owns this pokemon.
        long realplayerid = endpoint.getTeammember_DAO().findPlayerID(pokemon);
        if (realplayerid == 0 || realplayerid != si.serverid) {
            value.add("result", "error");
            sessionshandler.sendToSession(from, value);
            return;
        }

        // Removes the pokemon if still needed.
        endpoint.getTeammember_DAO().delete(si.serverid, pokemon);
        // Note that this only deletes the link, for archiving purposes. The actual pokemon still exists.
        value.add("result", "success");
        sessionshandler.sendToSession(from, value);
    }

}
