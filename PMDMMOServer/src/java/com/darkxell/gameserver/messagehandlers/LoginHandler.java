/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.gameserver.messagehandlers;

import com.darkxell.common.player.Player;
import com.darkxell.gameserver.GameServer;
import com.darkxell.gameserver.GameSessionHandler;
import com.darkxell.gameserver.GameSessionInfo;
import com.darkxell.gameserver.MessageHandler;
import com.darkxell.gameserver.SessionsInfoHolder;
import com.darkxell.common.dbobject.DBPlayer;
import com.eclipsesource.json.Json;
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
            GameSessionInfo si = SessionsInfoHolder.getInfo(from.getId());
            if (si.salt == null || si.salt.equals("")) {
                System.out.println("Could not connect a player because salt value for loging in was empty.");
                JsonObject value = javax.json.Json.createObjectBuilder()
                        .add("action", "logininfo")
                        .add("value", "ui.login.salterror")
                        .build();
                sessionshandler.sendToSession(from, value);
                return;
            }
            DBPlayer player = endpoint.getPlayerDAO().find(json.getString("name", ""));
            if (player == null) {
                System.out.println("Could not connect a player because his name was not found in database.");
                JsonObject value = javax.json.Json.createObjectBuilder()
                        .add("action", "logininfo")
                        .add("value", "ui.login.loginunknown")
                        .build();
                sessionshandler.sendToSession(from, value);
                return;
            } else if (player.isbanned) {
                System.out.println("Could not connect a player because he is banned.");
                JsonObject value = javax.json.Json.createObjectBuilder()
                        .add("action", "logininfo")
                        .add("value", "ui.login.banned")
                        .build();
                sessionshandler.sendToSession(from, value);
                return;
            }
            try {
                String serversidehashtester = sha256(player.passhash + si.salt + HASHSALTTYPE_LOGIN);
                si.name = player.name;
                si.salt = ""; // Salt is unique use
                if (serversidehashtester.equals(json.getString("passhash", "randomimpossiblestring"))) {
                    si.serverid = player.id;
                    si.isconnected = true;
                    si.currentdoing = GameSessionInfo.current_freezone;
                    System.err.println("Player logged in : " + si.name);

                    com.eclipsesource.json.JsonObject value = Json.object();
                    value.add("action", "login");
                    value.add("player", player.toJson());
                    System.out.println(player.toJson());
                    sessionshandler.sendToSession(from, value);

                } else {
                    System.out.println("Did not login " + player.name + ", password hash was incorrect.");
                    JsonObject value = javax.json.Json.createObjectBuilder()
                            .add("action", "logininfo")
                            .add("value", "ui.login.loginunmatched")
                            .build();
                    sessionshandler.sendToSession(from, value);
                }
            } catch (Exception e) {
                e.printStackTrace();
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
