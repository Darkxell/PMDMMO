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
public class ChatMessageHandler extends MessageHandler {

    public ChatMessageHandler(GameServer endpoint) {
        super(endpoint);
    }

    @Override
    public void handleMessage(JsonObject json, Session from, GameSessionHandler sessionshandler) {
        //TODO : filter the message here
        String message = json.getString("message");

        if (message.startsWith("/")) {
            handleCommand(json, from, sessionshandler);
        } else {
            sessionshandler.sendToAllConnectedSessions(json);
            System.out.println(from.getId() + " said : " + message);
        }

    }

    private void handleCommand(JsonObject json, Session from, GameSessionHandler sessionshandler) {
        String message = json.getString("message");
        GameSessionInfo si = SessionsInfoHolder.getInfo(from.getId());
        String[] content = message.split(" ");
        if (content[0].equals("/whisper") || content[0].equals("/w")) {
            // Custom whisper case because everyone can whisper.
            String destination = SessionsInfoHolder.getSessionID(content[1]);
            com.eclipsesource.json.JsonObject whisperobject = new com.eclipsesource.json.JsonObject();
            whisperobject.add("action", "chatmessage");
            whisperobject.add("messagecolor", "#C549FF");
            String temp = message.substring(message.indexOf(' ')+1);
            whisperobject.add("message", "[to " + content[1] + "] :" + temp.substring(temp.indexOf(' ')));
            whisperobject.add("sendercolor", json.getString("sendercolor", "#FFFFFF"));
            whisperobject.add("sender", si.name);
            //Delivers a copy to the sender.
            sessionshandler.sendToSession(from, whisperobject);
            if (destination == null) {
                //If the user is not online
                com.eclipsesource.json.JsonObject errorobject = new com.eclipsesource.json.JsonObject();
                errorobject.add("action", "chatmessage");
                errorobject.add("messagecolor", "#CD1000");
                errorobject.add("message", "User is not logged in or does not exist.");
                errorobject.add("sendercolor", "#CD1000");
                errorobject.add("sender", "Error");
                sessionshandler.sendToSession(from, errorobject);
            } else {
                //If the user is online
                sessionshandler.sendToSession(destination, whisperobject);
            }
        } else {
            // Check if the user issuing the comand is OP and executes the command.
            //TODO: check op status.
            switch (content[0]) {
                case "":
                    break;
                default:
                    System.out.println("Unknown command from " + si.name + " : " + content[0]);
                    break;
            }
        }
    }

}
