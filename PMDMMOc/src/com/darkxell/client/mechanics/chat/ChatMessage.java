package com.darkxell.client.mechanics.chat;

import java.awt.Color;

import com.darkxell.client.resources.Palette;
import com.eclipsesource.json.JsonValue;

/** Represents a message in thge chat. */
public class ChatMessage {

    public String tag = "";
    public String sender;
    public String message;
    public Color tagColor = Color.WHITE;
    public Color messageColor;
    public Color senderColor;

    public ChatMessage(String sender, String message, Color color, Color sendercolor) {
        this.sender = sender;
        this.message = message;
        this.messageColor = color;
        this.senderColor = sendercolor;
    }

    public ChatMessage(String sender, String message, Color color, Color sendercolor, String tag, Color tagcolor) {
        this(sender, message, color, sendercolor);
        this.tag = tag;
        this.tagColor = tagcolor;
    }

    public ChatMessage(JsonValue json) {
        this.tag = json.asObject().getString("tag", "");
        this.sender = json.asObject().getString("sender", "");
        this.message = json.asObject().getString("message", "");
        this.tagColor = Palette.getColorFromHexa(json.asObject().getString("tagcolor", "#000000"));
        this.messageColor = Palette.getColorFromHexa(json.asObject().getString("messagecolor", "#000000"));
        this.senderColor = Palette.getColorFromHexa(json.asObject().getString("sendercolor", "#000000"));
    }

}
