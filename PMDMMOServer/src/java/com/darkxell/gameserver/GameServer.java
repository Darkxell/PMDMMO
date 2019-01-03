/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.gameserver;

import com.darkxell.common.Registries;
import com.darkxell.common.util.Logger;
import com.darkxell.gameserver.messagehandlers.AcceptMissionHandler;
import com.darkxell.gameserver.messagehandlers.BankactionHandler;
import com.darkxell.gameserver.messagehandlers.ChatMessageHandler;
import com.darkxell.gameserver.messagehandlers.CreateAccountHandler;
import com.darkxell.gameserver.messagehandlers.DeleteMissionHandler;
import com.darkxell.gameserver.messagehandlers.DungeonendHandler;
import com.darkxell.gameserver.messagehandlers.DungeonstartHandler;
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
import com.darkxell.gameserver.messagehandlers.GetMissionsHandler;
import com.darkxell.gameserver.messagehandlers.InventoryRequestHandler;
import com.darkxell.gameserver.messagehandlers.ItemActionHandler;
import com.darkxell.gameserver.messagehandlers.LoginHandler;
import com.darkxell.gameserver.messagehandlers.MonsterRequestHandler;
import com.darkxell.gameserver.messagehandlers.NicknameHandler;
import com.darkxell.gameserver.messagehandlers.ObjectrequestHandler;
import com.darkxell.gameserver.messagehandlers.PublicKeyRequestHandler;
import com.darkxell.gameserver.messagehandlers.SaltResetHandler;
import com.darkxell.gameserver.messagehandlers.SetEncryptionKeyHandler;
import com.darkxell.gameserver.messagehandlers.TestResultHandler;
import com.darkxell.gameserver.messagehandlers.StorageactionHandler;
import com.darkxell.gameserver.messagehandlers.StorypositionAdvanceHandler;
import com.darkxell.model.ejb.DeployKeyDAO;
import com.darkxell.model.ejb.Holdeditem_DAO;
import com.darkxell.model.ejb.InventoryDAO;
import com.darkxell.model.ejb.Inventorycontains_DAO;
import com.darkxell.model.ejb.ItemstackDAO;
import com.darkxell.model.ejb.LearnedmoveDAO;
import com.darkxell.model.ejb.Learnedmove_DAO;
import com.darkxell.model.ejb.Missions_DAO;
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

    public static final String VERSION = "0.1.0-alpha.1";
    
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
    @EJB
    private Missions_DAO missions_DAO;
    @EJB
    private DeployKeyDAO deployKeyDAO;

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
        GameSessionInfo si = SessionsInfoHolder.getInfo(session.getId());

        System.out.println((si == null ? "null_si (this is a bug)" : si.name) + " just disconnected.");
        //Removes the session info and the game session info
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
        if (!GameServerSafe.iskeypairset) {
            GameServerSafe.setkeypair();
        }
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
            if (this.missions_DAO == null) {
                this.missions_DAO = new Missions_DAO();
            }
            if (this.deployKeyDAO == null) {
                this.deployKeyDAO = new DeployKeyDAO();
            }
            Logger.load("SERVER");
            Registries.load();
            System.out.println("DAOs and Registries loaded.");
            daoset = true;
        }
        if (!SessionsInfoHolder.infoExists(session.getId())) {
            SessionsInfoHolder.createDefaultInfo(session.getId());
        }
        GameSessionInfo infos = SessionsInfoHolder.getInfo(session.getId());
        try (JsonReader reader = Json.createReader(new StringReader(message))) {
            JsonObject jsonMessage = reader.readObject();
            //Decrypts if encrypted.
            try {
                int encrypted = jsonMessage.getInt("encrypted", 0);
                if (encrypted == 1 && infos.encryptionkey != null) {
                    String decryptedpayload = GameServerSafe.syncDecrypt(jsonMessage.toString(), infos.encryptionkey);
                    jsonMessage = Json.createReader(new StringReader(decryptedpayload)).readObject();
                }
            } catch (Exception e) {
                System.err.println("Could not parse an encrypted payload, trying to force it as non encrypted.");
                e.printStackTrace();
            }
            //Tests all possible payloads
            if (null != jsonMessage.getString("action", null)) {
                if (!jsonMessage.getString("action").equals("freezoneposition")) {
                    System.out.println("Got message from " + session.getId() + " : " + jsonMessage.getString("action"));
                }
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
                    case "publickeyrequest": {
                        PublicKeyRequestHandler hand = new PublicKeyRequestHandler(this);
                        hand.handleMessage(jsonMessage, session, sessionHandler);
                        break;
                    }
                    case "setencryptionkey": {
                        SetEncryptionKeyHandler hand = new SetEncryptionKeyHandler(this);
                        hand.handleMessage(jsonMessage, session, sessionHandler);
                        break;
                    }
                    case "login": {
                        LoginHandler hand = new LoginHandler(this);
                        hand.handleMessage(jsonMessage, session, sessionHandler);
                        break;
                    }
                    case "freezoneposition": {
                        if (!infos.isconnected) {
                            return;
                        }
                        FreezonePositionHandler hand = new FreezonePositionHandler(this);
                        hand.handleMessage(jsonMessage, session, sessionHandler);
                        break;
                    }
                    case "testresult": {
                        if (!infos.isconnected) {
                            return;
                        }
                        TestResultHandler hand = new TestResultHandler(this);
                        hand.handleMessage(jsonMessage, session, sessionHandler);
                        break;
                    }
                    case "objectrequest": {
                        if (!infos.isconnected) {
                            return;
                        }
                        ObjectrequestHandler hand = new ObjectrequestHandler(this);
                        hand.handleMessage(jsonMessage, session, sessionHandler);
                        break;
                    }
                    case "requestmonster": {
                        if (!infos.isconnected) {
                            return;
                        }
                        MonsterRequestHandler hand = new MonsterRequestHandler(this);
                        hand.handleMessage(jsonMessage, session, sessionHandler);
                        break;
                    }
                    case "requestinventory": {
                        if (!infos.isconnected) {
                            return;
                        }
                        InventoryRequestHandler hand = new InventoryRequestHandler(this);
                        hand.handleMessage(jsonMessage, session, sessionHandler);
                        break;
                    }
                    case "chatmessage": {
                        if (!infos.isconnected) {
                            return;
                        }
                        ChatMessageHandler hand = new ChatMessageHandler(this);
                        hand.handleMessage(jsonMessage, session, sessionHandler);
                        break;
                    }
                    case "itemaction": {
                        if (!infos.isconnected) {
                            return;
                        }
                        ItemActionHandler hand = new ItemActionHandler(this);
                        hand.handleMessage(jsonMessage, session, sessionHandler);
                        break;
                    }
                    case "dungeonstart": {
                        if (!infos.isconnected) {
                            return;
                        }
                        DungeonstartHandler hand = new DungeonstartHandler(this);
                        hand.handleMessage(jsonMessage, session, sessionHandler);
                        break;
                    }
                    case "dungeonend": {
                        if (!infos.isconnected) {
                            return;
                        }
                        DungeonendHandler hand = new DungeonendHandler(this);
                        hand.handleMessage(jsonMessage, session, sessionHandler);
                        break;
                    }
                    case "bankaction": {
                        if (!infos.isconnected) {
                            return;
                        }
                        BankactionHandler hand = new BankactionHandler(this);
                        hand.handleMessage(jsonMessage, session, sessionHandler);
                        break;
                    }
                    case "storageaction": {
                        if (!infos.isconnected) {
                            return;
                        }
                        StorageactionHandler hand = new StorageactionHandler(this);
                        hand.handleMessage(jsonMessage, session, sessionHandler);
                        break;
                    }
                    case "getmissions": {
                        GetMissionsHandler hand = new GetMissionsHandler(this);
                        hand.handleMessage(jsonMessage, session, sessionHandler);
                        break;
                    }
                    case "acceptmission": {
                        if (!infos.isconnected) {
                            return;
                        }
                        AcceptMissionHandler hand = new AcceptMissionHandler(this);
                        hand.handleMessage(jsonMessage, session, sessionHandler);
                        break;
                    }
                    case "deletemission": {
                        if (!infos.isconnected) {
                            return;
                        }
                        DeleteMissionHandler hand = new DeleteMissionHandler(this);
                        hand.handleMessage(jsonMessage, session, sessionHandler);
                        break;
                    }
                    case "nickname": {
                        if (!infos.isconnected) {
                            return;
                        }
                        NicknameHandler hand = new NicknameHandler(this);
                        hand.handleMessage(jsonMessage, session, sessionHandler);
                        break;
                    }
                    case "storyadvance": {
                        if (!infos.isconnected) {
                            return;
                        }
                        StorypositionAdvanceHandler hand = new StorypositionAdvanceHandler(this);
                        hand.handleMessage(jsonMessage, session, sessionHandler);
                        break;
                    }
                    default:
                        break;
                    // ADD other "action" json message types if needed.
                    // DON'T FORGET TO ADD THEM TO THE DOCUMENTATION!!!
                }
            } else {
                System.err.println("Could not parse a message because it has no action value. Message from " + session.getId() + " (" + infos.name + ")");
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

    public Missions_DAO getMissions_DAO() {
        return this.missions_DAO;
    }
    
     public DeployKeyDAO getDeployKeyDAO() {
        return this.deployKeyDAO;
    }

}
