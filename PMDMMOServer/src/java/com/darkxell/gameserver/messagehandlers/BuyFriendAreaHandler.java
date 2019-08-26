/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.gameserver.messagehandlers;

import com.darkxell.common.dbobject.DBPlayer;
import com.darkxell.common.zones.FriendArea;
import com.darkxell.gameserver.GameServer;
import com.darkxell.gameserver.GameSessionHandler;
import com.darkxell.gameserver.GameSessionInfo;
import com.darkxell.gameserver.MessageHandler;
import com.darkxell.gameserver.SessionsInfoHolder;
import com.eclipsesource.json.Json;
import java.util.ArrayList;
import javax.json.JsonObject;
import javax.websocket.Session;

/**
 *
 * @author Darkxell
 */
public class BuyFriendAreaHandler extends MessageHandler {

    public BuyFriendAreaHandler(GameServer endpoint) {
        super(endpoint);
    }

    @Override
    public void handleMessage(JsonObject json, Session from, GameSessionHandler sessionshandler) {
        GameSessionInfo si = SessionsInfoHolder.getInfo(from.getId());
        String area = json.getString("area", "");
        com.eclipsesource.json.JsonObject value = Json.object();
        value.add("action", "buyfriendarea");
        value.add("area", area);

        value.add("result", addArea(area, si.serverid, endpoint, false));

        sessionshandler.sendToSession(from, value);
    }

    /**
     * Adds the friendarea to the pmarsed player. This will do the required
     * checks on the player to veryfy he has the storyposition and enough money
     * to do so.
     *
     * @param force if this is true, the area addition will not check for money
     * and storyposition, and will be free. It will also accept any ID that does
     * not exist in the FriendArea enum.
     */
    public static String addArea(String areaid, long playerid, GameServer endpoint, boolean force) {
        FriendArea toadd = FriendArea.find(areaid);
        // Check if the area exists
        if (areaid.equals("") || (toadd == null && !force)) {
            return "unknownarea";
        }
        // Checks if the player already has the area
        ArrayList<String> areas = endpoint.getFriendAreas_DAO().findAreas(playerid);
        for (int i = 0; i < areas.size(); i++) {
            if (areas.get(i).equals(areaid)) {
                return "alreadyhas";
            }
        }
        // Checks if the player has enough money
        DBPlayer player = endpoint.getPlayerDAO().find(playerid);
        if (!force && player.moneyinbag < toadd.buyPrice()) {
            return "not_enough_money";
        }
        // Checks if the player can currently buy this area
        if (!force && !toadd.canBuy(player.storyposition)) {
            return "cannot_buy";
        }
        // Confirms and finishes
        if (!force) {
            player.moneyinbag -= toadd.buyPrice();
            endpoint.getPlayerDAO().update(player);
        }
        endpoint.getFriendAreas_DAO().create(playerid, areaid);
        return "success";

    }

}
