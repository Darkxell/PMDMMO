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
public class DBItemstack {

    public long id;
    public int itemid;
    public long quantity;

    public DBItemstack(long id, int itemid, long quantity) {
        this.id = id;
        this.itemid = itemid;
        this.quantity = quantity;

    }
}
