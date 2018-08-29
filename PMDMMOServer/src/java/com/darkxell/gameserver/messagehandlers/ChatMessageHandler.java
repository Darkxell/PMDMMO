/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.gameserver.messagehandlers;

import com.darkxell.gameserver.GameServer;
import com.darkxell.gameserver.GameSessionHandler;
import com.darkxell.gameserver.MessageHandler;
import javax.json.JsonObject;
import javax.websocket.Session;

/**
 *
 * @author Darkxell
 */
public class ChatMessageHandler extends MessageHandler {

    public ChatMessageHandler(GameServer endpoint) {
        super(endpoint);
    }

    @Override
    public void handleMessage(JsonObject json, Session from, GameSessionHandler sessionshandler) {
        //TODO : filter the message here
        if ("chatmessage".equals(json.getString("action"))) {
            sessionshandler.sendToAllConnectedSessions(json);
        }
        System.out.println(from.getId() + " said : " + json);

    }

}
