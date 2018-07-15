/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.gameserver;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

public class GameServerSafe {

    /**
     * Pointer the this instance of the server keypair. This keypair is
     * regenerated on server boot.<br>For obvious security reasons, the Private
     * key inside this object should NEVER leave the server.
     */
    private static KeyPair keypair = null;

    public static boolean iskeipairset = false;

    public static void setkeypair() {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);// keysize=2048
            keypair = kpg.generateKeyPair();
            iskeipairset = true;
            System.out.println("Server keypair was set. Public key is:\n" + keypair.getPublic());
        } catch (Exception e) {
            System.err.println("Could not generate main server keypair correctly.");
            e.printStackTrace();
        }
    }

}
