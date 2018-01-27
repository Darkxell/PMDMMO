/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.model.ejb.dbobjects;

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

    public DBLearnedmove(long id, int slot, int moveid, int ppmax, boolean islinked, int adedlevel) {
        this.id = id;
        this.slot = slot;
        this.moveid = moveid;
        this.ppmax = ppmax;
        this.islinked = islinked;
        this.addedlevel = addedlevel;
    }
}
