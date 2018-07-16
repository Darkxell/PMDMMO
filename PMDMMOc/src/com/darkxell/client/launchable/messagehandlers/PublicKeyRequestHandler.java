package com.darkxell.client.launchable.messagehandlers;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import com.darkxell.client.launchable.crypto.Encryption;
import com.darkxell.client.launchable.crypto.Safe;
import com.darkxell.common.util.Logger;
import com.eclipsesource.json.JsonObject;

public class PublicKeyRequestHandler extends MessageHandler {

	@Override
	public void handleMessage(JsonObject message) {
		String hexkey = message.getString("keybytes", "");
		if (hexkey.equals("")) {
			Logger.e("Could not load the server's public key to set up encryption.");
			return;
		}
		byte[] pkBytes = Encryption.hexStringToBytes(hexkey);
		KeyFactory keyFactory;
		try {
			keyFactory = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(pkBytes);
			PublicKey newpubKey = keyFactory.generatePublic(pubKeySpec);
			Safe.serverPublicKey = newpubKey;
			Logger.i("Server public key has been recieved sucessfully : " + hexkey);
		} catch (NoSuchAlgorithmException e) {
			Logger.e("Wrong encryption algorithm, could not load server public key for encryption.");
		} catch (InvalidKeySpecException e) {
			Logger.e("Invalid key spec!");
			e.printStackTrace();
		}
	}

}
