/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.model.ejb.dbobjects;

import com.darkxell.common.pokemon.LearnedMove;

/**
 *
 * @author Darkxell
 */
public class DBLearnedmove {

    public long id;
    public int slot;
    public int moveid;
    public int ppmax;
    public boolean islinked;
    public int addedlevel;

    public DBLearnedmove(long id, int slot, int moveid, int ppmax, boolean islinked, int addedlevel) {
        this.id = id;
        this.slot = slot;
        this.moveid = moveid;
        this.ppmax = ppmax;
        this.islinked = islinked;
        this.addedlevel = addedlevel;
    }
    
    public DBLearnedmove(LearnedMove move){
        //this.id = move;
        this.slot = move.getSlot();
        this.moveid = move.getId();
        this.ppmax = move.getMaxPP();
        this.islinked = move.isLinked();
        this.addedlevel = move.getAddedLevel();
    }
    
    public LearnedMove toLearnedMove(){
        LearnedMove toreturn = new LearnedMove(this.id);
        
        
        
        
        
        return null;
    }
}
