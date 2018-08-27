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
import com.darkxell.gameserver.RamMissionHolder;
import com.darkxell.gameserver.SessionsInfoHolder;
import com.eclipsesource.json.Json;
import javax.json.JsonObject;
import javax.websocket.Session;

/**
 *
 * @author Darkxell
 */
public class AcceptMissionHandler extends MessageHandler {

    public AcceptMissionHandler(GameServer endpoint) {
        super(endpoint);
    }

    @Override
    public void handleMessage(JsonObject json, Session from, GameSessionHandler sessionshandler) {
        GameSessionInfo si = SessionsInfoHolder.getInfo(from.getId());
        String wantedmissioncode = json.getString("mission", "na");
        com.eclipsesource.json.JsonObject value = Json.object();
        value.add("action", "acceptmission");
        value.add("mission", wantedmissioncode);
        boolean contains = false;
        for (int i = 0; i < RamMissionHolder.getMissions().size(); i++) {
            if (RamMissionHolder.getMissions().get(i).toString().equals(wantedmissioncode)) {
                contains = true;
                break;
            }
        }
        if (!contains || RamMissionHolder.acceptedToday(si.serverid, wantedmissioncode)) {
            value.add("accepted", 0);
            sessionshandler.sendToSession(from, value);
            return;
        }
        RamMissionHolder.addDailyAccept(si.serverid, wantedmissioncode);
        endpoint.getMissions_DAO().create(si.serverid, wantedmissioncode);
        value.add("accepted", 1);
        sessionshandler.sendToSession(from, value);
    }

}
