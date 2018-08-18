/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.gameserver.messagehandlers;

import com.darkxell.gameserver.GameServer;
import com.darkxell.gameserver.GameSessionHandler;
import com.darkxell.gameserver.MessageHandler;
import com.darkxell.gameserver.RamMissionHolder;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import java.util.ArrayList;
import javax.json.JsonObject;
import javax.websocket.Session;

/**
 *
 * @author Darkxell
 */
public class GetMissionsHandler extends MessageHandler {

    public GetMissionsHandler(GameServer endpoint) {
        super(endpoint);
    }

    @Override
    public void handleMessage(JsonObject json, Session from, GameSessionHandler sessionshandler) {
        com.eclipsesource.json.JsonObject value = Json.object();
        value.add("action", "getmissions");
        JsonArray array = new JsonArray();
        ArrayList missionscopy = RamMissionHolder.getMissions();
        for (int i = 0; i < missionscopy.size(); i++) {
            array.add(missionscopy.get(i).toString());
        }
        value.add("missions", array);
        sessionshandler.sendToSession(from, value);
    }

}
