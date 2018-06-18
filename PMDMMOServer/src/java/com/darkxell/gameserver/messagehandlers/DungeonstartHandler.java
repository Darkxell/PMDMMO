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
import java.util.Random;
import javax.json.JsonObject;
import javax.websocket.Session;

/**
 *
 * @author Darkxell
 */
public class DungeonstartHandler extends MessageHandler {

    public DungeonstartHandler(GameServer endpoint) {
        super(endpoint);
    }

    @Override
    public void handleMessage(JsonObject json, Session from, GameSessionHandler sessionshandler) {
        GameSessionInfo si = SessionsInfoHolder.getInfo(from.getId());
        if (si.currentdoing != GameSessionInfo.current_freezone) {
            return;
        }
        Long dungeonseed = new Random().nextLong();
        int dungeonid = json.getInt("dungeon", -1);
        if (dungeonid == -1) {
            return;
        }
        //Prepares thez answer
        com.eclipsesource.json.JsonObject answer = Json.object();
        answer.add("action", "dungeonstartconfirm");
        answer.add("seed", dungeonseed);
        answer.add("dungeon", dungeonid);
        //sets the gamesessioninfo
        si.currentdoing = GameSessionInfo.current_dungeon;
        si.freezoneID = "undefined";
        si.currentdungeon = dungeonid;
        si.currentdungeonseed = dungeonseed;
        sessionshandler.sendToSession(from, answer);
    }

}
