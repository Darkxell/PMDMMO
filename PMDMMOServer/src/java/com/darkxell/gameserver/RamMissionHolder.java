/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.gameserver;

import com.darkxell.common.mission.Mission;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Holds the different missions currently available in the server's ram.
 *
 * @author Darkxell
 */
public class RamMissionHolder {

    /**
     * The array of missions available today
     */
    private static ArrayList<Mission> missions = null;
    /**
     * A map of all the missions done today by each player.
     */
    private static HashMap<Long, ArrayList<String>> todayscompletions = new HashMap<>();
    private static long lastregeneration = 0l;
    private static final long REGENERATIONRATE = 1000 * 60 * 60 * 24;

    public static ArrayList<Mission> getMissions() {
        if (missions == null || System.currentTimeMillis() - lastregeneration > REGENERATIONRATE) {
            regenerateDailyMissions();
            lastregeneration = System.currentTimeMillis();
        }
        return missions;
    }

    private static void regenerateDailyMissions() {
        missions = Mission.getDailyMissions();
        todayscompletions.clear();
    }

    public static void addDailyCompletion(Long playerid, String missioncode) {
        if (todayscompletions.containsKey(playerid)) {
            todayscompletions.get(playerid).add(missioncode);
        } else {
            ArrayList<String> tp = new ArrayList<>();
            tp.add(missioncode);
            todayscompletions.put(playerid, tp);
        }
    }

    /**
     * Predicate that returns true if the parsed player has completed the parsed
     * mission today.
     */
    public static boolean completedToday(Long playerid, String missioncode) {
        if (todayscompletions.containsKey(playerid)) {
            ArrayList l = todayscompletions.get(playerid);
            return l.contains(missioncode);
        }
        return false;
    }

}
