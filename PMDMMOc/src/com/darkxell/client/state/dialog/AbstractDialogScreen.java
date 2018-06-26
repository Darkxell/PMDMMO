package com.darkxell.client.state.dialog;

import com.darkxell.common.util.language.Message;

public class AbstractDialogScreen
{
	/** Customizable ID, usable for complex Dialog states. 0 by default. */
	public byte id = 0;
	/** True if this DialogScreen prints text centered horizontally. */
	public boolean isCentered;
	/** True if this DialogScreen prints instantaneously. */
	public boolean isInstant;
	/** The Message to show in this Screen. */
	public final Message message;

	public AbstractDialogScreen(Message message)
	{
		this.message = message;
		this.isInstant = false;
	}

	public AbstractDialogScreen setCentered()
	{
		this.isCentered = true;
		return this;
	}

	public AbstractDialogScreen setInstant()
	{
		this.isInstant = true;
		return this;
	}

}