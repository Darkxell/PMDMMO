/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.gameserver.messagehandlers;

import com.darkxell.gameserver.GameServer;
import com.darkxell.gameserver.GameSessionHandler;
import com.darkxell.gameserver.MessageHandler;
import com.darkxell.gameserver.SessionsInfoHolder;
import com.darkxell.model.ejb.dbobjects.DBPlayer;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.websocket.Session;

/**
 *
 * @author Darkxell
 */
public class CreateAccountHandler extends MessageHandler {

    public CreateAccountHandler(GameServer endpoint) {
        super(endpoint);
    }

    @Override
    public void handleMessage(JsonObject json, Session from, GameSessionHandler sessionshandler) {

        try {
            if (!SessionsInfoHolder.infoExists(from.getId())) {
                System.err.println("Error at\ncom.darkxell.gameserver.messagehandlers.FreezonePositionHandler.handleMessage()\n" + from + " is not in the session info handler.");
                return;
            }
            
            String name = json.getJsonString("name").getString();
            long passhash = Long.parseLong(json.getJsonString("passhash").getString(), 36);
            DBPlayer newplayer = new DBPlayer(0, name, passhash, 0, 0, null, null, null, null, null);
            
            endpoint.getPlayerDAO().create(newplayer);
            System.out.println("New player created! : " + name);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
