/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.gameserver.messagehandlers;

import com.darkxell.gameserver.GameServer;
import com.darkxell.gameserver.GameServerSafe;
import com.darkxell.gameserver.GameSessionHandler;
import com.darkxell.gameserver.GameSessionInfo;
import com.darkxell.gameserver.MessageHandler;
import com.darkxell.gameserver.SessionsInfoHolder;
import com.eclipsesource.json.Json;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.spec.SecretKeySpec;
import javax.json.JsonObject;
import javax.websocket.Session;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Darkxell
 */
public class SetEncryptionKeyHandler extends MessageHandler {

    public SetEncryptionKeyHandler(GameServer endpoint) {
        super(endpoint);
    }

    @Override
    public void handleMessage(JsonObject json, Session from, GameSessionHandler sessionshandler) {
        GameSessionInfo si = SessionsInfoHolder.getInfo(from.getId());
        try {
            String encryptedkey = json.getString("value");
            byte[] tmp = GameServerSafe.decryptRSAPK1(DatatypeConverter.parseHexBinary(encryptedkey));
            String hexsynckey = Json.parse(new String(tmp)).asObject().getString("key", "");
            if (hexsynckey.equals("")) {
                throw new Exception();
            }
            si.encryptionkey = new SecretKeySpec(DatatypeConverter.parseHexBinary(hexsynckey), "AES");

            System.out.println("Session " + from.getId() + "(" + si.name + ") is now using encryption with his own key!");
            JsonObject value = javax.json.Json.createObjectBuilder()
                    .add("action", "setencryptionkey")
                    .add("ack", "ok")
                    .build();
            sessionshandler.sendToSession(from, value);
        } catch (Exception ex) {
            Logger.getLogger(SetEncryptionKeyHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
