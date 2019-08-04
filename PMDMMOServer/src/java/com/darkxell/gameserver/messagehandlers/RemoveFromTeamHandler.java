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
public class RemoveFromTeamHandler extends MessageHandler {

    public RemoveFromTeamHandler(GameServer endpoint) {
        super(endpoint);
    }

    @Override
    public void handleMessage(JsonObject json, Session from, GameSessionHandler sessionshandler) {
        GameSessionInfo si = SessionsInfoHolder.getInfo(from.getId());
        long pokemon = json.getJsonNumber("pokemonid").longValueExact();
        com.eclipsesource.json.JsonObject value = Json.object();
        value.add("action", "removefromteam");
        value.add("pokemonid", pokemon);

        ArrayList<Long> team = endpoint.getTeammember_DAO().findPokemonsIDinTeam(si.serverid);

        //Finds the pokemon position in the player's team. -1 if not found.
        byte positioninteam = (byte) endpoint.getTeammember_DAO().findLocation(pokemon);

        if (positioninteam < 0 || pokemon <= 0) {
            value.add("result", "error");
        } else {
            try {
                switch (positioninteam) {
                    case 1:
                        //You can't remove the team leader. Change it first.
                        value.add("result", "error");
                        break;
                    //Otherwise, shift from the removed pokemon.
                    case 2:
                        if (team.size() >= 3) {
                            endpoint.getTeammember_DAO().update(si.serverid, team.get(2), (byte) 2);
                        }
                    case 3:
                        if (team.size() >= 4) {
                            endpoint.getTeammember_DAO().update(si.serverid, team.get(3), (byte) 3);
                        }
                    case 4:
                        endpoint.getTeammember_DAO().update(si.serverid, pokemon, (short) 0);
                        value.add("result", "success");
                        break;
                }
            } catch (Exception e) {
                value.add("result", "error");
                e.printStackTrace();
            }
        }
        sessionshandler.sendToSession(from, value);
    }

}
