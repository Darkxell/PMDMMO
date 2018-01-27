/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.gameserver;

import java.io.StringReader;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.darkxell.gameserver.freezones.FreezonePositionHandler;
import com.darkxell.gameserver.freezones.SessionOpenHandler;
import com.darkxell.model.ejb.PlayerDAO;
import javax.ejb.EJB;

/**
 *
 * @author Darkxell
 */
@ApplicationScoped
@ServerEndpoint("/game")
public class GameServer {

    @Inject
    private GameSessionHandler sessionHandler;

    @EJB
    private PlayerDAO playerDAO;

    /**
     * Called when a client opens a websocket connection with the server.
     */
    @OnOpen
    public void open(Session session) {
        sessionHandler.addSession(session);
    }

    /**
     * Called when a client closes his websocket connection with the server.
     */
    @OnClose
    public void close(Session session) {
        sessionHandler.removeSession(session);
        if (SessionsInfoHolder.infoExists(session.getId())) {
            SessionsInfoHolder.removeInfo(session.getId());
        }
    }

    @OnError
    public void onError(Throwable error) {
        System.err.println("Game websocket error :");
        error.printStackTrace();
    }

    /**
     * Main method called when the game server recieves a message. This method
     * redirects the message to the appropriate MessageHandler.
     */
    @OnMessage
    public void handleMessage(String message, Session session) {
        try (JsonReader reader = Json.createReader(new StringReader(message))) {
            JsonObject jsonMessage = reader.readObject();
            if ("sessioninfo".equals(jsonMessage.getString("action"))) {
                SessionOpenHandler soh = new SessionOpenHandler(this);
                soh.handleMessage(jsonMessage, session, sessionHandler);
            } else if ("freezoneposition".equals(jsonMessage.getString("action"))) {
                FreezonePositionHandler fph = new FreezonePositionHandler(this);
                fph.handleMessage(jsonMessage, session, sessionHandler);
            }
            
        } catch (Exception e) {
            System.out.println(message);
            e.printStackTrace();
        }
    }

}
