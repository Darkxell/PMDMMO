/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.gameserver;

import com.darkxell.common.dbobject.DBDeployKey;
import com.darkxell.utility.RandomString;

/**
 *
 * @author Darkxell
 */
public class DeployKeyHandler {

    /**
     * Global deploykey mode switch. Might wanna put this elsewhere
     * later.<br><br>When deploykeymode is true, account creation payload will
     * require a deploykey to be included.
     */
    public static final boolean DEPLOYKEYMODE = false;

    /**
     * Generates a deployment key and returns it.
     */
    public static String generateKey(GameServer server) {
        DBDeployKey key = new DBDeployKey();
        key.keyvalue = getNewKey();
        key.timestamp = System.currentTimeMillis();
        if (server.getDeployKeyDAO().create(key)) {
            return key.keyvalue;
        } else {
            return "ERROR";
        }
    }

    /**
     * Generates a new random 20 chars string with a simple checksum suffix.
     */
    private static String getNewKey() {
        String k = new RandomString(20).nextString();
        int hash = 7;
        for (int i = 0; i < k.length(); i++) {
            hash = hash * 31 + k.charAt(i);
        }
        k += hash;
        return k;
    }

    public static boolean keyExists(String key, GameServer server) {
        DBDeployKey k = server.getDeployKeyDAO().find(key);
        return k != null && !k.isused;
    }

    public static boolean useKey(String key, long playerid, GameServer server) {
        DBDeployKey ke = server.getDeployKeyDAO().find(key);
        if (ke == null) {
            return false;
        }
        ke.timestamp = System.currentTimeMillis();
        ke.playerid = playerid;
        ke.isused = true;
        server.getDeployKeyDAO().update(ke);
        return true;
    }

}
