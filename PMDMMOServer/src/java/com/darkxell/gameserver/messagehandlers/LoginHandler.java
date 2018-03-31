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
import com.darkxell.model.ejb.dbobjects.DBPlayer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.json.JsonObject;
import javax.websocket.Session;

/**
 *
 * @author Darkxell
 */
public class LoginHandler extends MessageHandler {

    public static final String HASHSALTTYPE_LOGIN = "login";

    public LoginHandler(GameServer endpoint) {
        super(endpoint);
    }

    @Override
    public void handleMessage(JsonObject json, Session from, GameSessionHandler sessionshandler) {

        try {
            if (!SessionsInfoHolder.infoExists(from.getId())) {
                System.err.println("Error at\ncom.darkxell.gameserver.messagehandlers.LoginHandler.handleMessage()\n" + from + " is not in the session info handler.");
                return;
            }
            GameSessionInfo si = SessionsInfoHolder.getInfo(from.getId());
            if (si.salt == null || si.salt.equals("")) {
                System.out.println("Could not connect a player because salt value for loging in was empty.");
                return;
            }
            DBPlayer player = endpoint.getPlayerDAO().find(json.getString("name", ""));
            if (player == null) {
                System.out.println("Could not connect a player because his name was not found in database.");
                return;
            }
            try {
                String serversidehashtester = sha256(player.passhash + si.salt + HASHSALTTYPE_LOGIN);
                si.salt = ""; // Salt is unique use
                if (serversidehashtester.equals(json.getString("passhash", "randomimpossiblestring"))) {
                    si.serverid = player.id;
                    si.isconnected = true;
                    System.err.println("Player logged in : " + si.name);
                } else {
                    System.out.println("Did not login " + player.name + ", password hash was incorrect.");
                }
            } catch (Exception e) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static String sha256(String input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; ++i) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

}
