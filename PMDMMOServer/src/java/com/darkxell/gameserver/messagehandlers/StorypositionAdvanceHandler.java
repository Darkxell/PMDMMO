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
import javax.json.JsonObject;
import javax.websocket.Session;

/**
 *
 * @author Darkxell
 */
public class StorypositionAdvanceHandler extends MessageHandler {

    public StorypositionAdvanceHandler(GameServer endpoint) {
        super(endpoint);
    }

    @Override
    public void handleMessage(JsonObject json, Session from, GameSessionHandler sessionshandler) {
        GameSessionInfo si = SessionsInfoHolder.getInfo(from.getId());
        DBPlayer player = endpoint.getPlayerDAO().find(si.serverid);
        int targetposition = json.getInt("target", 0);
        // Checks if this modification is allowed.
        boolean acceptcommit = false;
        if (targetposition != 0) {
            switch (targetposition) {
                case 2:
                    if (player.storyposition == 1) {
                        acceptcommit = true;
                    }
                    break;
                    //TODO: add the possible modifications here
            }
        }
        // Commits the modification if it was allowed.
        if(acceptcommit){
            player.storyposition = targetposition;
            endpoint.getPlayerDAO().update(player);
        }
    }

}