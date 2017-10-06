/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.gameserver;

/**
 * A GameSessionInfo object describes the attributes of a session.
 * @author Darkxell
 */
public class GameSessionInfo {
    
    // COMMON DATA
    
    public boolean isconnected = false;
    public String name = "DefaultServerName";
    
    // FREEZONE DATA
    
    public String currentPokemon = "";
    public String freezoneID = "";
    public int posFX = 0;
    public int posFY = 0;
    
}
