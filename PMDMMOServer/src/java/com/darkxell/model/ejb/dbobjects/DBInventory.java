/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.model.ejb.dbobjects;

import java.util.ArrayList;

/**
 *
 * @author Darkxell
 */
public class DBInventory {

    public long id;
    public int maxsize;
    public ArrayList<DatabaseIdentifier> content;

    public DBInventory(long id, int maxsize, ArrayList<DatabaseIdentifier> content) {
        this.id = id;
        this.maxsize = maxsize;
        this.content = content;
    }

}
