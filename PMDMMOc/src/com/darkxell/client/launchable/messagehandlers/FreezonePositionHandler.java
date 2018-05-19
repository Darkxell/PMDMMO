package com.darkxell.client.launchable.messagehandlers;

import com.darkxell.client.launchable.Persistance;
import com.eclipsesource.json.JsonObject;

public class FreezonePositionHandler extends MessageHandler {

	@Override
	public void handleMessage(JsonObject message) {
		if(Persistance.currentmap != null)
			Persistance.currentmap.updateOtherPlayers(message);
	}

}
