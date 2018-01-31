/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.chatserver;

import java.io.StringReader;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 *
 * @author Darkxell
 */
@ApplicationScoped
@ServerEndpoint("/chat")
public class ChatWebsocketServer {

    @Inject
    private DeviceSessionHandler sessionHandler;

    @OnOpen
    public void open(Session session) {
        if (sessionHandler == null) {
            System.err.println("Chat session handler was null, created a new one before adding a session to it.");
            this.sessionHandler = new DeviceSessionHandler();
        }
        sessionHandler.addSession(session);
    }

    @OnClose
    public void close(Session session) {
        sessionHandler.removeSession(session);
    }

    @OnError
    public void onError(Throwable error) {
        System.err.println("Chat websocket error :");
        error.printStackTrace();
    }

    @OnMessage
    public void handleMessage(String message, Session session) {
        try (JsonReader reader = Json.createReader(new StringReader(message))) {
            JsonObject jsonMessage = reader.readObject();
            if ("message".equals(jsonMessage.getString("action"))) {
                sessionHandler.sendToAllConnectedSessions(jsonMessage);
            }
        } catch (Exception e) {
            System.out.println(message);
            e.printStackTrace();
        }
    }

}
