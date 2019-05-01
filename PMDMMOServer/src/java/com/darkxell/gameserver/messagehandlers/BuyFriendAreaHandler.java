/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.gameserver.messagehandlers;

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

        if (addArea(area, si.serverid, endpoint).equals("success")) {
            value.add("result", "success");
        } else {
            value.add("result", "error");
        }

        sessionshandler.sendToSession(from, value);
    }

    public static String addArea(String areaid, long playerid, GameServer endpoint) {
        if (!areaid.equals("")) {
            boolean possess = false;
            // Checks if the player already has the area
            ArrayList<String> areas = endpoint.getFriendAreas_DAO().findAreas(playerid);
            for (int i = 0; i < areas.size(); i++) {
                if (areas.get(i).equals(areaid)) {
                    possess = true;
                }
            }
            if (possess) {
                return "alreadyhas";
            } else {
                endpoint.getFriendAreas_DAO().create(playerid, areaid);
                return "success";
            }
        } else {
            return "void area";
        }
    }

}
