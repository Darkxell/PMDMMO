/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.gameserver;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author Darkxell
 */
@ApplicationScoped
@ServerEndpoint("/game")
public class GameServer {
    
     @Inject
    private GameSessionHandler sessionHandler;
    
    @OnOpen
        public void open(Session session) {
            sessionHandler.addSession(session);
    }

     @OnClose
    public void close(Session session) {
        sessionHandler.removeSession(session);
    }

    @OnError
    public void onError(Throwable error) {
        System.err.println("Game websocket error :");
        error.printStackTrace();
    }

     @OnMessage
    public void handleMessage(String message, Session session) {
        /*try (JsonReader reader = Json.createReader(new StringReader(message))) {
            JsonObject jsonMessage = reader.readObject();
            if ("message".equals(jsonMessage.getString("action"))) {
                sessionHandler.sendToAllConnectedSessions(jsonMessage);
            }
        } catch(Exception e){
            System.out.println(message);
            e.printStackTrace();
        }*/
    }
    
}
