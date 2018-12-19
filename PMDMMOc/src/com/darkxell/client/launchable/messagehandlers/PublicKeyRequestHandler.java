package com.darkxell.client.launchable.messagehandlers;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.darkxell.client.launchable.GameSocketEndpoint;
import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.launchable.crypto.Encryption;
import com.darkxell.client.launchable.crypto.RandomString;
import com.darkxell.client.launchable.crypto.Safe;
import com.darkxell.common.util.Logger;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

public class PublicKeyRequestHandler extends MessageHandler {

	@Override
	public void handleMessage(JsonObject message) {
		GameSocketEndpoint.SERVERVERSION = message.getString("version", "UNKNOWN");
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
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			byte[] salt = new byte[256];
			SecureRandom.getInstanceStrong().nextBytes(salt);
			KeySpec spec = new PBEKeySpec((new RandomString(256)).nextString().toCharArray(), salt, 65536, 256);
			byte[] tmp = factory.generateSecret(spec).getEncoded();
			Safe.symmetricKey = new SecretKeySpec(tmp, "AES");
			
			Logger.d("Secure sync key has been generated.");
			JsonObject jsontosend = Json.object().add("action", "setencryptionkey");
			JsonObject internal = Json.object().add("key", Encryption.bytesToHexString(tmp));
			String encryptedinternal = Encryption
					.bytesToHexString(Encryption.encryptRSAPK1(internal.toString().getBytes(), Safe.serverPublicKey));
			jsontosend.add("value", encryptedinternal);
			Persistence.socketendpoint.sendMessage(jsontosend.toString());
			Logger.i("Sent secure sync key to server, waiting server response to use encrypted payloads.");
		} catch (NoSuchAlgorithmException e) {
			Logger.e("Wrong encryption algorithm, could not load server public key for encryption.");
		} catch (InvalidKeySpecException e) {
			Logger.e("Invalid key spec!");
			e.printStackTrace();
		} catch (Exception e) {
			Logger.e("Unknown encryption protocol error at:");
			e.printStackTrace();
		}
	}

}
