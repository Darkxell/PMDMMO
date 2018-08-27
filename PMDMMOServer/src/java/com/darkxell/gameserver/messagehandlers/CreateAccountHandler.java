/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.gameserver.messagehandlers;

import com.darkxell.gameserver.GameServer;
import com.darkxell.gameserver.GameSessionHandler;
import com.darkxell.gameserver.MessageHandler;
import com.darkxell.gameserver.SessionsInfoHolder;
import com.darkxell.common.dbobject.DBInventory;
import com.darkxell.common.dbobject.DBPlayer;
import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.Session;

/**
 *
 * @author Darkxell
 */
public class CreateAccountHandler extends MessageHandler {

    public CreateAccountHandler(GameServer endpoint) {
        super(endpoint);
    }

    @Override
    public void handleMessage(JsonObject json, Session from, GameSessionHandler sessionshandler) {

        try {

            String name = json.getJsonString("name").getString();
            String passhash = json.getJsonString("passhash").getString();

            DBPlayer newplayer = new DBPlayer(0, name, passhash, 0, 0, 0, null, null, null, null, null, null, 0);
            newplayer.id = endpoint.getPlayerDAO().create(newplayer);
            if (newplayer.id == 0) {
                JsonObject value = Json.createObjectBuilder()
                        .add("action", "logininfo")
                        .add("value", "ui.login.nametaken")
                        .build();
                sessionshandler.sendToSession(from, value);
                return; // Player was not created successfully.
            }
            DBInventory toolbox = new DBInventory(0, 20, null);
            toolbox.id = endpoint.getInventoryDAO().create(toolbox);
            DBInventory playerstorage = new DBInventory(0, 1500, null);
            playerstorage.id = endpoint.getInventoryDAO().create(playerstorage);
            endpoint.getToolbox_DAO().create(newplayer.id, toolbox.id);
            endpoint.getPlayerStorage_DAO().create(newplayer.id, playerstorage.id);
            System.out.println("New player created! : " + name);
            JsonObject value = Json.createObjectBuilder()
                    .add("action", "logininfo")
                    .add("value", "ui.login.accountcreated")
                    .build();
            sessionshandler.sendToSession(from, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
