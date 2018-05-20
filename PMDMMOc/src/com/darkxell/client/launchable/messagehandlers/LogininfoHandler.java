package com.darkxell.client.launchable.messagehandlers;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.state.mainstates.AccountCreationState;
import com.darkxell.client.state.mainstates.LoginMainState;
import com.eclipsesource.json.JsonObject;

public class LogininfoHandler extends MessageHandler {

	@Override
	public void handleMessage(JsonObject message) {
		if(Persistance.stateManager instanceof LoginMainState)
			((LoginMainState) Persistance.stateManager).servercall(message.asObject().getString("value", "error"));
		if(Persistance.stateManager instanceof AccountCreationState)
			((AccountCreationState) Persistance.stateManager).servercall(message.asObject().getString("value", "error"));
	}

}
