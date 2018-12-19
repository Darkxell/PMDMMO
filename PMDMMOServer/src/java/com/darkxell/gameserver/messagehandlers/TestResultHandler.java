/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.gameserver.messagehandlers;

import com.darkxell.common.Registries;
import com.darkxell.common.dbobject.DBPlayer;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.pokemon.PokemonSpecies;
import com.darkxell.gameserver.GameServer;
import com.darkxell.gameserver.GameSessionHandler;
import com.darkxell.gameserver.GameSessionInfo;
import com.darkxell.gameserver.MessageHandler;
import com.darkxell.gameserver.SessionsInfoHolder;
import com.eclipsesource.json.Json;
import java.util.Random;
import javax.json.JsonObject;
import javax.websocket.Session;

/**
 *
 * @author Darkxell
 */
public class TestResultHandler extends MessageHandler {

    public TestResultHandler(GameServer endpoint) {
        super(endpoint);
    }

    @Override
    public void handleMessage(JsonObject json, Session from, GameSessionHandler sessionshandler) {
        GameSessionInfo si = SessionsInfoHolder.getInfo(from.getId());
        int mainid = json.getJsonNumber("mainid").intValue();
        int offid = json.getJsonNumber("offid").intValue();
        int maingender = json.getJsonNumber("maingender").intValue();
        int offgender = json.getJsonNumber("offgender").intValue();
        // TODO : Check that mainID is a valid number

        DBPlayer pl = endpoint.getPlayerDAO().find(si.serverid);
        if (pl.storyposition != 0) {
            System.out.println("Player storyposition was not 0, didn't accept test results.");
            return;
        }

        PokemonSpecies espece = Registries.species().find(mainid);
        Pokemon main = espece.generate(new Random(), 5);
        espece = Registries.species().find(offid);
        Pokemon off = espece.generate(new Random(), 5);

        // Creates the main pokemon in db
        long t = endpoint.getPokemonDAO().create(main.getData());
        if (t > 0) {
            endpoint.getTeammember_DAO().create(si.serverid, t, (byte) 1);
            for (int i = 0; i < main.moveCount(); ++i) {
                long xtsd = endpoint.getLearnedmoveDAO().create(main.move(i).getData());
                if (xtsd > 0) {
                    endpoint.getLearnedmove_DAO().create(t, xtsd);
                }
            }
        }
        // Creates the partner pokemon
        t = endpoint.getPokemonDAO().create(off.getData());
        if (t > 0) {
            endpoint.getTeammember_DAO().create(si.serverid, t, (byte) 2);
            for (int i = 0; i < off.moveCount(); ++i) {
                long xtsd = endpoint.getLearnedmoveDAO().create(off.move(i).getData());
                if (xtsd > 0) {
                    endpoint.getLearnedmove_DAO().create(t, xtsd);
                }
            }
        }
        //Increments storyposition
        pl.storyposition = 1;
        endpoint.getPlayerDAO().update(pl);

        System.out.println("recieved test result : " + mainid + " and " + offid);

        com.eclipsesource.json.JsonObject value = Json.object();
        value.add("action", "testresultrecieved");
        sessionshandler.sendToSession(from, value);

    }

}
