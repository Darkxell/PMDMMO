/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.model.ejb.dbobjectutilities;

import com.darkxell.common.player.Player;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.gameserver.GameServer;
import com.darkxell.common.dbobject.DBPlayer;
import com.darkxell.common.dbobject.DBPokemon;
import com.eclipsesource.json.JsonObject;

/**
 *
 * @author Darkxell
 */
public class PlayerDBUtilities {

    public static JsonObject generateFullPlayerObject(long id, GameServer endpoint) {
        DBPlayer player = endpoint.getPlayerDAO().find(id);
        Player toreturn = new Player();
        toreturn.moneyInBank = player.moneyinbank;
        toreturn.money = player.moneyinbag;
        toreturn.name = player.name;
        toreturn.currentStoryline = player.storyposition;
        // Find the leader pokemon
        DBPokemon leader = endpoint.getPokemonDAO().find(endpoint.getTeammember_DAO().findPokemonID(id, (byte)1));
        toreturn.setLeaderPokemon(PokemonDBUtilities.getPokemonObjectFromDBPokemon(leader, endpoint));
        // Find the teammate pokemon
        DBPokemon ally1 = endpoint.getPokemonDAO().find(endpoint.getTeammember_DAO().findPokemonID(id, (byte)2));
        toreturn.addAlly(PokemonDBUtilities.getPokemonObjectFromDBPokemon(ally1, endpoint));
        

        //TODO: implement inventory linking
        return toreturn.toJson();
    }

}
