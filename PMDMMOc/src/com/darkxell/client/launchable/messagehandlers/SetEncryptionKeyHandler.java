package com.darkxell.client.launchable.messagehandlers;

import com.darkxell.client.launchable.crypto.Safe;
import com.darkxell.common.util.Logger;
import com.eclipsesource.json.JsonObject;

public class SetEncryptionKeyHandler extends MessageHandler {

	@Override
	public void handleMessage(JsonObject message) {
		if (message.getString("ack", "").equals("ok")) {
			Safe.serverhaskey = true;
			Logger.i("Key has been acknowledged. Messages to the server will now be encrypted.");
		} else {
			Logger.e("Server refused sync key. Not using encryption.");
		}
	}

}
