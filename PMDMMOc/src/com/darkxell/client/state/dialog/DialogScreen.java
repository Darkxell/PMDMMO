package com.darkxell.client.state.dialog;

import com.darkxell.common.util.language.Message;

public class DialogScreen
{
	/** Customizable ID, usable for complex Dialog states. 0 by default. */
	public byte id = 0;
	/** True if this DialogScreen prints text centered horizontally. */
	public boolean isCentered = false;
	/** True if this DialogScreen prints instantaneously. */
	public boolean isInstant = false;
	/** True if this Dialog's window is opaque. */
	public boolean isOpaque = false;
	/** The Message to show in this Screen. */
	public final Message message;
	AbstractDialogState parentState;

	public DialogScreen(Message message)
	{
		this.message = message;
	}

	public AbstractDialogState parentState()
	{
		return this.parentState;
	}

	/** @return True if this message can be finished. */
	public boolean requestNextMessage()
	{
		return true;
	}

	public DialogScreen setCentered()
	{
		this.isCentered = true;
		return this;
	}

	public DialogScreen setInstant()
	{
		this.isInstant = true;
		return this;
	}

}