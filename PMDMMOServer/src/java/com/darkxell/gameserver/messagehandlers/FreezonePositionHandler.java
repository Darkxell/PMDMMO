/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.gameserver.messagehandlers;

import com.darkxell.gameserver.GameServer;
import com.darkxell.gameserver.GameSessionInfo;
import com.darkxell.gameserver.GameSessionHandler;
import com.darkxell.gameserver.MessageHandler;
import com.darkxell.gameserver.SessionsInfoHolder;
import java.util.Iterator;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.Session;

/**
 *
 * @author Darkxell
 */
public class FreezonePositionHandler extends MessageHandler {

    public FreezonePositionHandler(GameServer gs) {
        super(gs);
    }

    @Override
    public void handleMessage(JsonObject json, Session from, GameSessionHandler sessionshandler) {
        try {
            if (!SessionsInfoHolder.infoExists(from.getId())) {
                System.err.println("Error at\ncom.darkxell.gameserver.messagehandlers.FreezonePositionHandler.handleMessage()\n" + from + " is not in the session info handler.");
                return;
            }

            GameSessionInfo si = SessionsInfoHolder.getInfo(from.getId());
            si.posFX = json.getJsonNumber("posfx").doubleValue();
            si.posFY = json.getJsonNumber("posfy").doubleValue();
            si.currentPokemon = json.getString("currentpokemon", "1");
            si.freezoneID = json.getString("freezoneid", "base");
            Iterator it = SessionsInfoHolder.getMap().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                GameSessionInfo gsi = (GameSessionInfo) pair.getValue();
                if (gsi.freezoneID.equals(si.freezoneID) && gsi.isconnected && si.isconnected) {
                    JsonObject value = Json.createObjectBuilder()
                            .add("action", "freezoneposition")
                            .add("name", gsi.name)
                            .add("currentpokemon", gsi.currentPokemon)
                            .add("freezoneid", gsi.freezoneID)
                            .add("posfx", gsi.posFX)
                            .add("posfy", gsi.posFY)
                            .build();
                    sessionshandler.sendToSession(from, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
