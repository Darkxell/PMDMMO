/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.gameserver.messagehandlers;

import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.pokemon.PokemonRegistry;
import com.darkxell.common.pokemon.PokemonSpecies;
import com.darkxell.gameserver.GameServer;
import com.darkxell.gameserver.GameSessionHandler;
import com.darkxell.gameserver.GameSessionInfo;
import com.darkxell.gameserver.MessageHandler;
import com.darkxell.gameserver.SessionsInfoHolder;
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
        if (!si.isconnected) {
            return;
        }
        int mainid = json.getJsonNumber("mainid").intValue();
        int offid = json.getJsonNumber("offid").intValue();
        int maingender = json.getJsonNumber("maingender").intValue();
        int offgender = json.getJsonNumber("offgender").intValue();
        // TODO : Check that mainID is a valid number
        
        PokemonSpecies espece = PokemonRegistry.find(mainid);
        Pokemon main = espece.generate(new Random(), 5);
        espece = PokemonRegistry.find(offid);
        Pokemon off = espece.generate(new Random(), 5);
        
        // PokemonDBUtilities.savePokemonInDatabase(main, endpoint, si.serverid, (byte)1);
        // PokemonDBUtilities.savePokemonInDatabase(off, endpoint, si.serverid, (byte)2);
        
        System.out.println("recieved test result : " + mainid +" and "+ offid);
        
    }

}
