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
public class DBPlayer {

    public long id;
    public String name;
    public String passhash;
    public long moneyinbank;
    public long moneyinbag;
    public int storyposition;
    public ArrayList<DatabaseIdentifier> pokemonsinzones;
    public ArrayList<DatabaseIdentifier> pokemonsinparty;
    public DatabaseIdentifier mainpokemon;
    public DatabaseIdentifier toolboxinventory;
    public DatabaseIdentifier storageinventory;

    public DBPlayer(long id, String name, String passhash, long moneyinbank, long moneyinbag,int storyposition,
            ArrayList<DatabaseIdentifier> pokemonsinzones, ArrayList<DatabaseIdentifier> pokemonsinparty,
            DatabaseIdentifier mainpokemon, DatabaseIdentifier toolboxinventory, DatabaseIdentifier storageinventory) {
        this.id = id;
        this.name = name;
        this.passhash = passhash;
        this.moneyinbag = moneyinbag;
        this.moneyinbank = moneyinbank;
        this.storyposition = storyposition;
        this.pokemonsinparty = pokemonsinparty;
        this.pokemonsinzones = pokemonsinzones;
        this.mainpokemon = mainpokemon;
        this.toolboxinventory = toolboxinventory;
        this.storageinventory = storageinventory;
    }
}
