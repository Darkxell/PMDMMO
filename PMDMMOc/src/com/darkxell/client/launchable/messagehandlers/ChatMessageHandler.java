package com.darkxell.client.launchable.messagehandlers;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.chat.ChatMessage;
import com.darkxell.common.util.Logger;
import com.eclipsesource.json.JsonObject;

public class ChatMessageHandler extends MessageHandler {

	@Override
	public void handleMessage(JsonObject message) {
		try {
			if (Persistence.chatbox != null && Persistence.chatbox.messages != null)
				Persistence.chatbox.messages.add(new ChatMessage(message));
			else {
				Logger.w("Couldn't write the following message in chat (not yet loaded) : " + message);
			}
		} catch (Exception e) {
			Logger.e("Could not add the recieved message to messages list : " + message + " / " + e);
		}
	}

}
