package com.darkxell.client.launchable.messagehandlers;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.state.mainstates.LoginMainState;
import com.eclipsesource.json.JsonObject;

public class LoginPlayerHandler extends MessageHandler {

	@Override
	public void handleMessage(JsonObject message) {
		JsonObject pl = message.get("player").asObject();
		if (Persistance.stateManager instanceof LoginMainState)
			((LoginMainState) Persistance.stateManager).launchOnlineOnRecieve(pl);
	}

}
