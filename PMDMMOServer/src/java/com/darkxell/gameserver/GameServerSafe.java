/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.gameserver;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import javax.crypto.Cipher;
import javax.xml.bind.DatatypeConverter;

public class GameServerSafe {

    /**
     * Pointer the this instance of the server keypair. This keypair is
     * regenerated on server boot.<br>For obvious security reasons, the Private
     * key inside this object should NEVER leave the server.
     */
    private static KeyPair keypair = null;

    public static boolean iskeypairset = false;

    public static void setkeypair() {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);// keysize=2048
            keypair = kpg.generateKeyPair();
            iskeypairset = true;
            System.out.println("Server keypair was set. Public key is:\n" + keypair.getPublic());
        } catch (Exception e) {
            System.err.println("Could not generate main server keypair correctly.");
            e.printStackTrace();
        }
    }

    public static String getPublicKeyHexString() {
        if (!iskeypairset) {
            setkeypair();
        }
        return DatatypeConverter.printHexBinary(keypair.getPublic().getEncoded());
    }

    /**
     * Decrypts an array of bytes with the parsed private key.
     */
    public static byte[] decryptRSAPK1(byte[] inpBytes) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
        cipher.init(Cipher.DECRYPT_MODE, keypair.getPrivate());
        return cipher.doFinal(inpBytes);
    }

}
