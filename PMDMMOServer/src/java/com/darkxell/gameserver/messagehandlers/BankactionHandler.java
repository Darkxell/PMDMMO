/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.gameserver.messagehandlers;

import com.darkxell.common.dbobject.DBPlayer;
import com.darkxell.gameserver.GameServer;
import com.darkxell.gameserver.GameSessionHandler;
import com.darkxell.gameserver.GameSessionInfo;
import com.darkxell.gameserver.MessageHandler;
import com.darkxell.gameserver.SessionsInfoHolder;
import com.eclipsesource.json.Json;
import javax.json.JsonObject;
import javax.websocket.Session;

/**
 *
 * @author Darkxell
 */
public class BankactionHandler extends MessageHandler {

    public BankactionHandler(GameServer endpoint) {
        super(endpoint);
    }

    @Override
    public void handleMessage(JsonObject json, Session from, GameSessionHandler sessionshandler) {
        GameSessionInfo si = SessionsInfoHolder.getInfo(from.getId());
        DBPlayer player = endpoint.getPlayerDAO().find(si.serverid);
        int deposit = json.getInt("money");
        //Does the money transfers if they are possible
        if (deposit > 0) {
            if (player.moneyinbag >= deposit) {
                player.moneyinbag -= deposit;
                player.moneyinbank += deposit;
                endpoint.getPlayerDAO().update(player);
            }
        } else if (deposit < 0) {
            int withdraw = -deposit;
            if (player.moneyinbank >= withdraw) {
                player.moneyinbank -= withdraw;
                player.moneyinbag += withdraw;
                endpoint.getPlayerDAO().update(player);
            }
        }
        //Returns the information to the player
        com.eclipsesource.json.JsonObject value = Json.object();
        value.add("action", "bankactionconfirm");
        value.add("moneyinbag", player.moneyinbag);
        value.add("moneyinbank", player.moneyinbank);
        sessionshandler.sendToSession(from, value);
    }

}
