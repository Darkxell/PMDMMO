/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.gameserver;

import javax.crypto.SecretKey;

/**
 * A GameSessionInfo object describes the attributes of a session.
 *
 * @author Darkxell
 */
public class GameSessionInfo {

    public static final byte current_nothing = 0;
    public static final byte current_dungeon = 1;
    public static final byte current_freezone = 2;

    // COMMON DATA
    public boolean isconnected = false;
    public String salt = "";
    public SecretKey encryptionkey = null;
    public String name = "NotLoggedYet";
    public long serverid = -1l;//0 is the "not found" value. Default should not be 0.
    public byte currentdoing = current_nothing;

    // FREEZONE DATA
    public String currentPokemon = "";
    public String freezoneID = "undefined";
    public double posFX = 0;
    public double posFY = 0;

    // DUNGEON DATA
    public int currentdungeon = -1;
    public long currentdungeonseed = 0l;

}
