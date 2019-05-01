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
import com.eclipsesource.json.JsonArray;
import java.util.ArrayList;
import javax.json.JsonObject;
import javax.websocket.Session;

/**
 *
 * @author Darkxell
 */
public class GetFriendAreasHandler extends MessageHandler {

    public GetFriendAreasHandler(GameServer endpoint) {
        super(endpoint);
    }

    @Override
    public void handleMessage(JsonObject json, Session from, GameSessionHandler sessionshandler) {
        GameSessionInfo si = SessionsInfoHolder.getInfo(from.getId());
        ArrayList<String> zones = endpoint.getFriendAreas_DAO().findAreas(si.serverid);
        com.eclipsesource.json.JsonObject value = Json.object();
        value.add("action", "getfriendareas");
        JsonArray arr = new JsonArray();
        for (int i = 0; i < zones.size(); ++i) {
            arr.add(zones.get(i));
        }
        value.add("areas",arr);
        sessionshandler.sendToSession(from, value);
    }

}
