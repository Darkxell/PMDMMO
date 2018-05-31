/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.gameserver;

/**
 * A GameSessionInfo object describes the attributes of a session.
 *
 * @author Darkxell
 */
public class GameSessionInfo {

    // COMMON DATA
    public boolean isconnected = false;
    public String salt = "";
    public String name = "DefaultServerName";
    public long serverid = -1l;//0 is the "not found" value. Default should not be 0.

    // FREEZONE DATA
    public String currentPokemon = "";
    public String freezoneID = "undefined";
    public double posFX = 0;
    public double posFY = 0;

}
