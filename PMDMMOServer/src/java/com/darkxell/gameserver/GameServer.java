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
import com.darkxell.model.ejb.dbobjects.DBPlayer;
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

    /**
     * Flag marking if the DAOs in this class have been instancied. This
     * basically replace the whole DAOFactory pattern since only this class will
     * ever have to access the database.
     */
    private boolean daoset = false;

    @EJB
    private PlayerDAO playerDAO;

    /**
     * Called when a client opens a websocket connection with the server.
     */
    @OnOpen
    public void open(Session session) {
        if (sessionHandler == null) {
            System.err.println("Game session handler was null, created a new one before adding a session to it.");
            this.sessionHandler = new GameSessionHandler();
        }
        sessionHandler.addSession(session);
    }

    /**
     * Called when a client closes his websocket connection with the server.
     */
    @OnClose
    public void close(Session session) {
        if (sessionHandler == null) {
            System.err.println("Game session handler was null, created a new one before adding a session to it.");
            this.sessionHandler = new GameSessionHandler();
        }
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
        if (!daoset) {
            if (this.playerDAO == null) {
                this.playerDAO = new PlayerDAO();
            }
            //ADD new DAOs
            daoset = true;
        }
        try (JsonReader reader = Json.createReader(new StringReader(message))) {
            JsonObject jsonMessage = reader.readObject();
            if ("sessioninfo".equals(jsonMessage.getString("action"))) {
                SessionOpenHandler soh = new SessionOpenHandler(this);
                soh.handleMessage(jsonMessage, session, sessionHandler);
            } else if ("freezoneposition".equals(jsonMessage.getString("action"))) {
                FreezonePositionHandler fph = new FreezonePositionHandler(this);
                fph.handleMessage(jsonMessage, session, sessionHandler);
            }
            //ADD other "action" json message types if needed.
            // DON'T FORGET TO ADD THEM TO THE DOCUMENTATION!!!
            playerDAO.create(null);
        } catch (Exception e) {
            System.out.println(message);
            e.printStackTrace();
        }
    }

}
