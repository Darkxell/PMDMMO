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
public class AddToTeamHandler extends MessageHandler {

    public AddToTeamHandler(GameServer endpoint) {
        super(endpoint);
    }

    @Override
    public void handleMessage(JsonObject json, Session from, GameSessionHandler sessionshandler) {
        GameSessionInfo si = SessionsInfoHolder.getInfo(from.getId());
        long pokemon = json.getJsonNumber("pokemonid").longValueExact();
        ArrayList<Long> team = endpoint.getTeammember_DAO().findPokemonsIDinTeam(si.serverid);

        long pkmnowner = endpoint.getTeammember_DAO().findPlayerID(pokemon);

        com.eclipsesource.json.JsonObject value = Json.object();
        value.add("action", "addtoteam");
        value.add("pokemonid", pokemon);
        if (pkmnowner != si.serverid) {
            value.add("result", "error");
        } else if (team.size() >= 4) {
            value.add("result", "team_full");
        } else {
            try {
                endpoint.getTeammember_DAO().update(pkmnowner, pokemon, (short) (team.size() + 1));
                value.add("result", "success");
            } catch (Exception e) {
                value.add("result", "error");
                e.printStackTrace();
            }
        }
        sessionshandler.sendToSession(from, value);
    }

}
