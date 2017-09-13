package com.darkxell.client.mechanics.chat;

import java.awt.Color;

/** Represents a message in thge chat. */
public class ChatMessage {

	public String tag = "";
	public String sender;
	public String message;
	public Color tagColor = Color.WHITE;
	public Color lineColor;

	public ChatMessage(String sender, String message, Color color) {
		this.sender = sender;
		this.message = message;
		this.lineColor = color;
	}

	public ChatMessage(String sender, String message, Color color, String tag, Color tagcolor) {
		this(sender, message, color);
		this.tag = tag;
		this.tagColor = tagcolor;
	}

}
