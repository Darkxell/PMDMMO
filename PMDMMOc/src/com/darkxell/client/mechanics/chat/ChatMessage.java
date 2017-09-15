package com.darkxell.client.mechanics.chat;

import java.awt.Color;

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

}
