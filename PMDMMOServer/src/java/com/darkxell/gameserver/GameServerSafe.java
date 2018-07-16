/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.gameserver;

import com.eclipsesource.json.Json;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
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

    /**
     * Encrypts a string using the AES key in the safe and generates a
     * JsonString containing the encrypted data, ready to be sent to the server.
     */
    public static String syncEncrypt(String data, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return Json.object().add("encrypted", 1).add("value", DatatypeConverter.printHexBinary(cipher.doFinal(data.getBytes())))
                .toString();
    }

    /**
     * Decrypts an encrypted json payload using the AES key in the safe. The
     * result is usually a json payload.
     */
    public static String syncDecrypt(String data, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            
        cipher.init(Cipher.DECRYPT_MODE, key);
        return new String(cipher.doFinal(DatatypeConverter.parseHexBinary(Json.parse(data).asObject().getString("value", ""))));
    }

}
