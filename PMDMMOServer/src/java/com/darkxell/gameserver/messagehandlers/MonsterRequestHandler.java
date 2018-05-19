/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.gameserver.messagehandlers;

import com.darkxell.common.dbobject.DBPokemon;
import com.darkxell.gameserver.GameServer;
import com.darkxell.gameserver.GameSessionHandler;
import com.darkxell.gameserver.MessageHandler;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import javax.json.JsonObject;
import javax.websocket.Session;

/**
 *
 * @author Darkxell
 */
public class MonsterRequestHandler extends MessageHandler {

    public MonsterRequestHandler(GameServer endpoint) {
        super(endpoint);
    }

    @Override
    public void handleMessage(JsonObject json, Session from, GameSessionHandler sessionshandler) {
        int id = json.getJsonNumber("id").intValue();
        DBPokemon pokemon = endpoint.getPokemonDAO().find(id);

        com.eclipsesource.json.JsonObject value = Json.object();
        value.add("action", "requestmonster");
        value.add("object", pokemon.toJson());
        if (pokemon.holdeditem != null) {
            value.add("item", endpoint.getItemstackDAO().find(pokemon.holdeditem.id).toJson());
        }
        JsonArray moves = new JsonArray();

        if (pokemon.learnedmoves != null) {
            for (int i = 0; i < pokemon.learnedmoves.size(); ++i) {
                moves.add(endpoint.getLearnedmoveDAO().find(pokemon.learnedmoves.get(i).id).toJson());
            }
        }

        value.add("moves", moves);
        sessionshandler.sendToSession(from, value);
    }

}
