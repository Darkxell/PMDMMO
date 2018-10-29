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
import javax.json.JsonObject;
import javax.websocket.Session;

/**
 *
 * @author Darkxell
 */
public class NicknameHandler extends MessageHandler {

    public NicknameHandler(GameServer endpoint) {
        super(endpoint);
    }

    @Override
    public void handleMessage(JsonObject json, Session from, GameSessionHandler sessionshandler) {
        GameSessionInfo si = SessionsInfoHolder.getInfo(from.getId());
        String nickname = json.getJsonString("nickname").getString();
        long pokemonid = json.getInt("pokemonid", 0);
        if (si.serverid == endpoint.getTeammember_DAO().findPlayerID(pokemonid)) {
            DBPokemon pkmn = endpoint.getPokemonDAO().find(pokemonid);
            pkmn.nickname = nickname;
            endpoint.getPokemonDAO().update(pkmn);
            System.out.println("Changed " + si.name + "'s pokemon (" + pokemonid + ") nickname to : " + nickname);
        } else {
            System.err.println("Player " + si.name + " tried to nickname a pokemon he doesn't own. id:" + pokemonid + " / nickname:" + nickname);
        }
    }

}
