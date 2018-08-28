/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.gameserver.servermechanics;

import com.darkxell.common.dbobject.DBPlayer;
import com.darkxell.common.mission.Mission;
import com.darkxell.gameserver.GameServer;
import com.darkxell.gameserver.GameSessionInfo;

/**
 *
 * @author Darkxell
 */
public class MissionEndManager {

    public static void manageMissionCompletion(GameSessionInfo si, GameServer endpoint, String missioncode) {
        Mission m = null;
        try {
            m = new Mission(missioncode);
        } catch (Exception e) {
            System.err.println("Invalid mission completion for " + si.name + " : " + missioncode);
            return;
        }
        //Give the money and the rescue points
        DBPlayer player = endpoint.getPlayerDAO().find(si.serverid);
        player.moneyinbag += m.getRewards().getMoney();
        player.points += m.getRewards().getPoints();
        endpoint.getPlayerDAO().update(player);
        //Gives the items to the player
        for (int i = 0; i < m.getRewards().getItems().length; i++) {
            GiveManager.giveItem(m.getRewards().getItems()[i], m.getRewards().getQuantities()[i], si, endpoint, false);
        }
        //Iterates on the triggers
        try {
            if (m.getRewards().getTriggers() != null) {
                for (int i = 0; i < m.getRewards().getTriggers().length; i++) {
                    //TODO : manage the triggers somewhere sometime.
                }
            }
        } catch (Exception e) {
        }
        endpoint.getMissions_DAO().delete(si.serverid, missioncode);
    }

}
