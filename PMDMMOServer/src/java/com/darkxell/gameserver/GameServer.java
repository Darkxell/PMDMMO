/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.gameserver;

import com.darkxell.common.dungeon.DungeonRegistry;
import com.darkxell.common.item.ItemRegistry;
import com.darkxell.common.move.MoveRegistry;
import com.darkxell.common.pokemon.PokemonRegistry;
import com.darkxell.common.trap.TrapRegistry;
import com.darkxell.common.util.Logger;
import com.darkxell.gameserver.messagehandlers.CreateAccountHandler;
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

import com.darkxell.gameserver.messagehandlers.FreezonePositionHandler;
import com.darkxell.gameserver.messagehandlers.LoginHandler;
import com.darkxell.gameserver.messagehandlers.SaltResetHandler;
import com.darkxell.gameserver.messagehandlers.TestResultHandler;
import com.darkxell.model.ejb.Holdeditem_DAO;
import com.darkxell.model.ejb.InventoryDAO;
import com.darkxell.model.ejb.Inventorycontains_DAO;
import com.darkxell.model.ejb.ItemstackDAO;
import com.darkxell.model.ejb.LearnedmoveDAO;
import com.darkxell.model.ejb.Learnedmove_DAO;
import com.darkxell.model.ejb.PlayerDAO;
import com.darkxell.model.ejb.Playerstorage_DAO;
import com.darkxell.model.ejb.PokemonDAO;
import com.darkxell.model.ejb.Teammember_DAO;
import com.darkxell.model.ejb.Toolbox_DAO;
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
    @EJB
    private Holdeditem_DAO holdeditem_DAO;
    @EJB
    private InventoryDAO inventoryDAO;
    @EJB
    private Inventorycontains_DAO inventorycontains_DAO;
    @EJB
    private ItemstackDAO itemstackDAO;
    @EJB
    private LearnedmoveDAO learnedmoveDAO;
    @EJB
    private Learnedmove_DAO learnedmove_DAO;
    @EJB
    private Playerstorage_DAO playerstorage_DAO;
    @EJB
    private PokemonDAO pokemonDAO;
    @EJB
    private Teammember_DAO teammember_DAO;
    @EJB
    private Toolbox_DAO toolbox_DAO;

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
            if (this.holdeditem_DAO == null) {
                this.holdeditem_DAO = new Holdeditem_DAO();
            }
            if (this.inventoryDAO == null) {
                this.inventoryDAO = new InventoryDAO();
            }
            if (this.inventorycontains_DAO == null) {
                this.inventorycontains_DAO = new Inventorycontains_DAO();
            }
            if (this.itemstackDAO == null) {
                this.itemstackDAO = new ItemstackDAO();
            }
            if (this.learnedmoveDAO == null) {
                this.learnedmoveDAO = new LearnedmoveDAO();
            }
            if (this.learnedmove_DAO == null) {
                this.learnedmove_DAO = new Learnedmove_DAO();
            }
            if (this.playerstorage_DAO == null) {
                this.playerstorage_DAO = new Playerstorage_DAO();
            }
            if (this.pokemonDAO == null) {
                this.pokemonDAO = new PokemonDAO();
            }
            if (this.teammember_DAO == null) {
                this.teammember_DAO = new Teammember_DAO();
            }
            if (this.toolbox_DAO == null) {
                this.toolbox_DAO = new Toolbox_DAO();
            }
            Logger.loadServer();
            PokemonRegistry.load();
            MoveRegistry.load();
            ItemRegistry.load();
            TrapRegistry.load();
            DungeonRegistry.load();
            System.out.println("DAOs and Registries loaded.");
            daoset = true;
        }
        if (!SessionsInfoHolder.infoExists(session.getId())) {
            SessionsInfoHolder.createDefaultInfo(session.getId());
        }
        try (JsonReader reader = Json.createReader(new StringReader(message))) {
            JsonObject jsonMessage = reader.readObject();
            if (null != jsonMessage.getString("action")) {
                switch (jsonMessage.getString("action")) {
                    case "createaccount": {
                        CreateAccountHandler hand = new CreateAccountHandler(this);
                        hand.handleMessage(jsonMessage, session, sessionHandler);
                        break;
                    }
                    case "saltreset": {
                        SaltResetHandler hand = new SaltResetHandler(this);
                        hand.handleMessage(jsonMessage, session, sessionHandler);
                        break;
                    }
                    case "login": {
                        LoginHandler hand = new LoginHandler(this);
                        hand.handleMessage(jsonMessage, session, sessionHandler);
                        break;
                    }
                    case "freezoneposition": {
                        FreezonePositionHandler hand = new FreezonePositionHandler(this);
                        hand.handleMessage(jsonMessage, session, sessionHandler);
                        break;
                    }
                    case "testresult": {
                        TestResultHandler hand = new TestResultHandler(this);
                        hand.handleMessage(jsonMessage, session, sessionHandler);
                        break;
                    }
                    default:
                        break;
                    // ADD other "action" json message types if needed.
                    // DON'T FORGET TO ADD THEM TO THE DOCUMENTATION!!!
                }
            }
        } catch (Exception e) {
            System.out.println(message);
            e.printStackTrace();
        }
    }

    public PlayerDAO getPlayerDAO() {
        return this.playerDAO;
    }

    public Holdeditem_DAO getHoldeditem_DAO() {
        return this.holdeditem_DAO;
    }

    public InventoryDAO getInventoryDAO() {
        return this.inventoryDAO;
    }

    public Inventorycontains_DAO getInventoryContains_DAO() {
        return this.inventorycontains_DAO;
    }

    public ItemstackDAO getItemstackDAO() {
        return this.itemstackDAO;
    }

    public LearnedmoveDAO getLearnedmoveDAO() {
        return this.learnedmoveDAO;
    }

    public Learnedmove_DAO getLearnedmove_DAO() {
        return this.learnedmove_DAO;
    }

    public Playerstorage_DAO getPlayerStorage_DAO() {
        return this.playerstorage_DAO;
    }

    public PokemonDAO getPokemonDAO() {
        return this.pokemonDAO;
    }

    public Teammember_DAO getTeammember_DAO() {
        return this.teammember_DAO;
    }

    public Toolbox_DAO getToolbox_DAO() {
        return this.toolbox_DAO;
    }

}
