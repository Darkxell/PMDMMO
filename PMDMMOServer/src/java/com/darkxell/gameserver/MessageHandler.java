/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.gameserver;

import javax.json.JsonObject;
import javax.websocket.Session;

/**
 * A messageHandler is an object that describes the behavior of the server when recieving a specific message from a session.
 * @author Darkxell
 */
public interface MessageHandler {
    
    /**Describes what this messageHandler should do when recieving a message from a session.*/
    public abstract void handleMessage(JsonObject json,Session from,GameSessionHandler sessionshandler);
    
}
