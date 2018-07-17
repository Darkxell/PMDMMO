/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.gameserver;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.json.JsonObject;
import javax.websocket.Session;

/**
 * @author Darkxell This class should have an instance per gameserver. It holds
 * the information about all sessions with the server. This class contains
 * methods able to communicate with the wanted sessions.
 */
@ApplicationScoped
public class GameSessionHandler {

    private final Set<Session> sessions = new HashSet<>();

    public void addSession(Session session) {
        sessions.add(session);
    }

    public void removeSession(Session session) {
        sessions.remove(session);
    }

    public void sendToAllConnectedSessions(JsonObject message) {
        sessions.forEach((session) -> {
            sendToSession(session, message);
        });
    }

    public void sendToSession(Session session, JsonObject message) {
        try {
            GameSessionInfo sessioninfo = SessionsInfoHolder.getInfo(session.getId());
            boolean sent = false;
            try {
                if (sessioninfo.encryptionkey != null) {
                    session.getBasicRemote().sendText(GameServerSafe.syncEncrypt(message.toString(), sessioninfo.encryptionkey));
                    sent = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!sent) {
                session.getBasicRemote().sendText(message.toString());
            }
        } catch (IOException ex) {
            sessions.remove(session);
            System.err.println("Error while sending a message");
        }
    }

    public void sendToSession(Session session, com.eclipsesource.json.JsonObject message) {
        GameSessionInfo sessioninfo = SessionsInfoHolder.getInfo(session.getId());
        try {
            if (sessioninfo.encryptionkey != null) {
                session.getBasicRemote().sendText(GameServerSafe.syncEncrypt(message.toString(), sessioninfo.encryptionkey));
            } else {
                session.getBasicRemote().sendText(message.toString());
            }
        } catch (IOException ex) {
            sessions.remove(session);
            System.err.println("Error while sending a message");
        } catch (Exception ex) {
            Logger.getLogger(GameSessionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
