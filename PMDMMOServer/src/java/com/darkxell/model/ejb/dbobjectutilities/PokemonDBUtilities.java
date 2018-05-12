/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.model.ejb.dbobjectutilities;

import com.darkxell.common.item.ItemStack;
import com.darkxell.common.pokemon.BaseStats;
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.gameserver.GameServer;
import com.darkxell.common.dbobject.DBItemstack;
import com.darkxell.common.dbobject.DBLearnedmove;
import com.darkxell.common.dbobject.DBPokemon;
import java.util.ArrayList;

/**
 *
 * @author Darkxell
 */
public class PokemonDBUtilities {

    /**
     * Shortuct method to save a pokemon in the database. The pokemon will be
     * created as a new pokemon assigned to the player parsed.
     */
    public static void savePokemonInDatabase(Pokemon tosave, GameServer endpoint, long playerid, byte location) {
        DBPokemon pk = new DBPokemon(playerid, tosave.species().id, 0, tosave.getAbility().id, tosave.gender(), tosave.getRawNickname(), tosave.getLevel(), tosave.getExperience(), tosave.getBaseStats().getAttack(), tosave.getBaseStats().getDefense(), tosave.getBaseStats().getSpecialAttack(), tosave.getBaseStats().getSpecialDefense(), location, null, null);
        long pokemonid = endpoint.getPokemonDAO().create(pk);
        if (tosave.getItem() != null) {
            DBItemstack itemstack = new DBItemstack(0, tosave.getItem().getId(), tosave.getItem().getQuantity());
            long holdeditemid = endpoint.getItemstackDAO().create(itemstack);
            endpoint.getHoldeditem_DAO().create(playerid, holdeditemid);
        }
        for (int i = 0; i < tosave.moveCount(); ++i) {
            LearnedMove move = tosave.move(i);
            DBLearnedmove dbmove = new DBLearnedmove(0, i, move.getId(), move.getMaxPP(), move.isLinked(), 0);
            long moveid = endpoint.getLearnedmoveDAO().create(dbmove);
            endpoint.getLearnedmove_DAO().create(pokemonid, moveid);
        }
    }

    public static Pokemon getPokemonObjectFromDBPokemon(DBPokemon dbpk, GameServer endpoint) {
        DBItemstack holdeditem = endpoint.getItemstackDAO().find(endpoint.getHoldeditem_DAO().findStackID(dbpk.id));
        ItemStack holded = new ItemStack(holdeditem.itemid, (int) holdeditem.quantity);
        Pokemon toreturn = new Pokemon((int) dbpk.id, dbpk.specieid, dbpk.nickname, holded, new BaseStats(dbpk.stat_atk, dbpk.stat_def, dbpk.stat_hp, dbpk.stat_speatk, dbpk.stat_spedef, 1), dbpk.abilityid, (int) dbpk.experience, dbpk.level, null, null, null, null, (byte) dbpk.gender, 0, false);
        //TODO : add the moves here
        return toreturn;
    }

}
