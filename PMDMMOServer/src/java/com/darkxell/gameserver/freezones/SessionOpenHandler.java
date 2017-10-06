/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.gameserver.freezones;

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
public class SessionOpenHandler implements MessageHandler{

    @Override
    public void handleMessage(JsonObject json, Session from, GameSessionHandler sessionshandler) {
        if(!SessionsInfoHolder.infoExists(from.getId()))
                SessionsInfoHolder.createDefaultInfo(from.getId());
        GameSessionInfo si = SessionsInfoHolder.getInfo(from.getId());
        si.name = json.getString("name");
        si.isconnected = true;
    }
    
}
