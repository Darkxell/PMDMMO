/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.gameserver.messagehandlers;

import com.darkxell.gameserver.GameServer;
import com.darkxell.gameserver.GameSessionHandler;
import com.darkxell.gameserver.GameSessionInfo;
import com.darkxell.gameserver.MessageHandler;
import com.darkxell.gameserver.SessionsInfoHolder;
import javax.json.JsonObject;
import javax.websocket.Session;

/**
 *
 * @author Darkxell
 */
public class DungeonendHandler extends MessageHandler{

    public DungeonendHandler(GameServer endpoint) {
        super(endpoint);
    }

    @Override
    public void handleMessage(JsonObject json, Session from, GameSessionHandler sessionshandler) {
        GameSessionInfo si = SessionsInfoHolder.getInfo(from.getId());
        System.out.println(si.name + " finished a dungeon, processing...");
        
        
        
        
        
        
        
        
        
    }
    
}
