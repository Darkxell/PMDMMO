/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.gameserver.messagehandlers;

import com.darkxell.common.dbobject.DBPlayer;
import com.darkxell.gameserver.DeployKeyHandler;
import com.darkxell.gameserver.GameServer;
import com.darkxell.gameserver.GameSessionHandler;
import com.darkxell.gameserver.GameSessionInfo;
import com.darkxell.gameserver.MessageHandler;
import com.darkxell.gameserver.SessionsInfoHolder;
import com.darkxell.gameserver.servermechanics.GiveManager;
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
            String temp = message.substring(message.indexOf(' ') + 1);
            whisperobject.add("message", temp.substring(temp.indexOf(' ')));
            whisperobject.add("sendercolor", "#C549FF");
            whisperobject.add("sender", si.name + " to " + content[1]);
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
            DBPlayer issuer = endpoint.getPlayerDAO().find(si.serverid);
            if (issuer.isop) {
                com.eclipsesource.json.JsonObject errorobject = new com.eclipsesource.json.JsonObject();
                errorobject.add("action", "chatmessage");
                errorobject.add("messagecolor", "#CD1000");
                errorobject.add("sendercolor", "#CD1000");
                errorobject.add("sender", "Help");
                String feedbackMessage = "";
                boolean sendfeedback = false;
                switch (content[0]) {
                    case "/info": // Manages the INFO command
                        switch (content[1]) {
                            case "mysid":
                                feedbackMessage = "Your server id is : " + si.serverid;
                                sendfeedback = true;
                                break;
                            case "idcheck":
                                try {
                                    feedbackMessage = "Player " + content[2] + " id is " + endpoint.getPlayerDAO().find(content[2]).id;
                                } catch (Exception e) {
                                    feedbackMessage = "Could not check the id for that name. Usage is: /info idcheck <playername>";
                                }
                                sendfeedback = true;
                                break;
                            case "check":
                                try {
                                    feedbackMessage = "Player " + content[2] + " json dump is : " + endpoint.getPlayerDAO().find(content[2]).toJson();
                                } catch (Exception e) {
                                    feedbackMessage = "Could not jsondump that id. Usage is: /info check <playerid>";
                                }
                                sendfeedback = true;
                                break;
                            default:
                                feedbackMessage = "Usage is incorrect. Try /info <param> with one of the following param values : mysid, check, idcheck";
                                sendfeedback = true;
                                break;
                        }
                        break;
                    case "/give":// Manages the GIVE command
                        try {
                            int playerid = Integer.parseInt(content[1]);
                            int itemid = Integer.parseInt(content[2]);
                            int quantity = Integer.parseInt(content[3]);
                            GiveManager.giveItem(itemid, quantity, playerid, endpoint, true);
                            feedbackMessage = "Gave " + quantity + " itemid:" + itemid + " to player id:" + playerid;
                        } catch (Exception e) {
                            feedbackMessage = "Could not give that item. Usage is: /give <playerid> <item> <quantity>";
                        }
                        sendfeedback = true;
                        break;
                    case "/unlockfriendarea":// Manages the UNLOCKFRIENDAREA command
                        try {
                            int playerid = Integer.parseInt(content[1]);
                            String areaid = content[2];
                            String feedback = BuyFriendAreaHandler.addArea(areaid, playerid, endpoint,true);
                            if (feedback.equals("success")) {
                                feedbackMessage = "Unlocked " + areaid + " for player " + playerid + ".";
                            } else {
                                feedbackMessage = "Could not unlock " + areaid + " with error : " + feedback;
                            }
                        } catch (Exception e) {
                            feedbackMessage = "Could not unlock that freezone. Usage is: /unlockfriendarea <playerid> <areaid>";
                        }
                        sendfeedback = true;
                        break;
                    case "/setstoryposition":// Manages the SETSTORYPOSITION command
                        try {
                            int playerid = Integer.parseInt(content[1]);
                            int position = Integer.parseInt(content[2]);
                            DBPlayer toset = endpoint.getPlayerDAO().find(playerid);
                            toset.storyposition = position;
                            endpoint.getPlayerDAO().update(toset);
                            feedbackMessage = "Player " + playerid + " storyposition was set to " + position + ".";
                        } catch (Exception e) {
                            feedbackMessage = "Could not set the storyposition. Usage is: /setstoryposition <playerid> <position>";
                        }
                        sendfeedback = true;
                        break;
                    case "/ban"://Manages the BAN command
                        try {
                            int playerid = Integer.parseInt(content[1]);
                            DBPlayer bannedplayer = endpoint.getPlayerDAO().find(playerid);
                            bannedplayer.isbanned = true;
                            endpoint.getPlayerDAO().update(bannedplayer);
                            feedbackMessage = "Player " + playerid + " has been banned forever. Goodbye!";
                        } catch (Exception e) {
                            feedbackMessage = "Could not ban that player. Usage is: /ban <playerid>";
                        }
                        sendfeedback = true;
                        break;
                    case "/deploykey"://Manages the DEPLOYKEY command
                        String key = DeployKeyHandler.generateKey(endpoint);
                        feedbackMessage = "A new deploy key has been generated: " + key;
                        sendfeedback = true;
                        break;
                    case "/help":// Help utility
                        feedbackMessage = "Help message needs to be coded. Available commands are: /info /give /setstoryposition /ban /deploykey /help /unlockfriendarea";
                        sendfeedback = true;
                        break;
                    default:
                        System.out.println("Unknown command from " + si.name + " : " + content[0]);
                        break;
                }
                if (sendfeedback) {
                    errorobject.add("message", feedbackMessage);
                    sessionshandler.sendToSession(from, errorobject);
                }
            } else {
                //Error message if the user is not op
                com.eclipsesource.json.JsonObject errorobject = new com.eclipsesource.json.JsonObject();
                errorobject.add("action", "chatmessage");
                errorobject.add("messagecolor", "#CD1000");
                errorobject.add("sendercolor", "#CD1000");
                errorobject.add("sender", "Error");
                errorobject.add("message", "You do not have required permissions to execute this. Contact an administrator if you think this is a mistake.");
                sessionshandler.sendToSession(from, errorobject);
            }
        }
    }

}
