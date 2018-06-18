package com.darkxell.gameserver;

import java.util.HashMap;

/**
 * Perststance class that holds information to a session.
 *
 * @author Darkxell
 */
public class SessionsInfoHolder {

    private static HashMap<String, GameSessionInfo> infos = new HashMap<String, GameSessionInfo>();

    /**
     * Gets the infos for the wanted session.
     *
     * @return the sessionInfo for the session, null if the session doesn't exist.
     */
    public static GameSessionInfo getInfo(String sessionID) {
        return infos.get(sessionID);
    }

    /**
     * Removes all the stored informations for the provided sessionID
     */
    public static void removeInfo(String sessionID) {
        infos.remove(sessionID);
    }

    /**
     * predicate that returns true if there is data concerning the parsed
     * session available.
     */
    public static boolean infoExists(String sessionID) {
        return infos.containsKey(sessionID);
    }

    /**
     * Creates default values for a session in memory. Does nothing it the
     * session already has informations.
     */
    public static void createDefaultInfo(String sessionID) {
        if (!infoExists(sessionID)) {
            infos.put(sessionID, new GameSessionInfo());
        }
    }

    /**
     * Returns a pointer to the hashmap containing the sessions data.
     */
    public static HashMap getMap() {
        return infos;
    }

}
