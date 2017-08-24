package com.darkxell.client.util;

import java.util.HashMap;

/** Represents a message to be translated. Contains an ID of message, and may contain replacements to make after translating. */
public class Message
{

	/** ID of the message. */
	public final String id;
	/** ID of the last language this message was translated with. Used to update the translation when the user changes the Language. */
	private String lastLang = null;
	private HashMap<String, Message> replacements = new HashMap<String, Message>();
	/** False if this Message's ID shouldn't be translated. */
	public final boolean shouldTranslate;
	/** Translated value of this Message. Used to avoid translating every time this Message is used. */
	private String value = "";

	public Message(String id, boolean shouldTranslate)
	{
		this.id = id;
		this.shouldTranslate = shouldTranslate;
	}

	/** Adds a replacement to this Message.
	 * 
	 * @param pattern - The pattern to replace.
	 * @param message - The message to replace with. */
	public Message addReplacement(String pattern, Message message)
	{
		this.replacements.put(pattern, message);
		return this;
	}

	/** Adds a replacement to this Message.
	 * 
	 * @param pattern - The pattern to replace.
	 * @param message - The message to replace with. */
	public Message addReplacement(String pattern, String message)
	{
		return this.addReplacement(pattern, new Message(message, false));
	}

	private void update()
	{
		this.value = this.shouldTranslate ? Lang.translate(this.id) : this.id;
		for (String pattern : this.replacements.keySet())
			this.value.replaceAll(pattern, this.replacements.get(pattern).value());
	}

	/** @return The translated value of this Message. */
	public String value()
	{
		if (!Lang.getLanguage().id.equals(this.lastLang)) this.update();
		return this.value;
	}

}
