package com.darkxell.client.launchable.messagehandlers;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.chat.ChatMessage;
import com.darkxell.common.util.Logger;
import com.eclipsesource.json.JsonObject;

public class ChatMessageHandler extends MessageHandler {

    @Override
    public void handleMessage(JsonObject message) {

        ChatMessage cm = new ChatMessage(message);
        if (cm.message.contains("A new deploy key has been generated:") && cm.sender.equals("Help")) {
            StringSelection selection = new StringSelection(cm.message);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
        }

        try {
            if (Persistence.chatbox != null && Persistence.chatbox.messages != null)
                Persistence.chatbox.messages.add(cm);
            else
                Logger.w("Couldn't write the following message in chat (not yet loaded) : " + message);
        } catch (Exception e) {
            Logger.e("Could not add the recieved message to messages list : " + message + " / " + e);
        }
    }

}
