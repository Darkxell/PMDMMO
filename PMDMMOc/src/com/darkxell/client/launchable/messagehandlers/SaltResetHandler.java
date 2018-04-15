package com.darkxell.client.launchable.messagehandlers;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.state.mainstates.LoginMainState;
import com.eclipsesource.json.JsonObject;

public class SaltResetHandler extends MessageHandler{

	@Override
	public void handleMessage(JsonObject message) {
		if(Persistance.stateManager instanceof LoginMainState)
			((LoginMainState) Persistance.stateManager).setSalt(message.asObject().getString("value", ""));
	}

}
