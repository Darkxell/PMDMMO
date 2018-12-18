package com.darkxell.client.launchable.messagehandlers;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.state.mainstates.AccountCreationState;
import com.darkxell.client.state.mainstates.LoginMainState;
import com.eclipsesource.json.JsonObject;

public class LogininfoHandler extends MessageHandler {

	@Override
	public void handleMessage(JsonObject message) {
		if(Persistence.stateManager instanceof LoginMainState)
			((LoginMainState) Persistence.stateManager).servercall(message.asObject().getString("value", "error"));
		if(Persistence.stateManager instanceof AccountCreationState)
			((AccountCreationState) Persistence.stateManager).servercall(message.asObject().getString("value", "error"));
	}

}
