package com.darkxell.client.state.dialog;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.renderers.TextRenderer.PMDChar;
import com.darkxell.client.resources.images.MenuHudSpriteset;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.ui.Keys;
import com.darkxell.common.util.language.Message;

public abstract class AbstractDialogState extends AbstractState
{

	public static interface DialogEndListener
	{
		/** Called when the input dialog ends. */
		public void onDialogEnd(AbstractDialogState dialog);
	}

	public static final BufferedImage arrow = MenuHudSpriteset.NEXT_WINDOW_ARROW;
	public static final int ARROW_TICK_LENGTH = 20;
	static final byte PRINTING = 0, PAUSED = 1, SWITCHING = 2;

	int arrowtick;
	/** The current screen. */
	protected int currentScreen;
	/** The split lines of the current message. */
	ArrayList<ArrayList<PMDChar>> lines;
	/** The listener called when this Dialog ends. If null, the Background State is used instead. */
	protected final DialogEndListener listener;
	/** The screens to show. */
	protected final List<AbstractDialogScreen> screens;
	/** The current state of this dialog. */
	byte state;

	public AbstractDialogState(DialogEndListener listener, AbstractDialogScreen screen)
	{
		this(listener, Arrays.asList(screen));
	}

	public AbstractDialogState(DialogEndListener listener, List<AbstractDialogScreen> screens)
	{
		this.screens = screens;

		this.listener = listener;
		this.lines = new ArrayList<ArrayList<PMDChar>>();
		this.currentScreen = this.arrowtick = 0;
		this.state = PRINTING;
	}

	public AbstractDialogState(List<AbstractDialogScreen> elements)
	{
		this(null, elements);
	}

	/** @return Length of the current Message. */
	int currentLength()
	{
		if (this.lines.isEmpty()) return 0;
		int length = 0;
		for (int line = 0; line < this.lines.size(); ++line)
			length += this.lines.get(line).size();
		return length;
	}

	public Message currentMessage()
	{
		return this.currentScreen().message;
	}

	public AbstractDialogScreen currentScreen()
	{
		return this.screens.get(this.currentScreen);
	}

	/** Skips to the next message. */
	public void nextMessage()
	{
		if (this.currentScreen == this.screens.size() - 1)
		{
			if (this.listener != null) this.listener.onDialogEnd(this);
		} else
		{
			++this.currentScreen;
			this.lines.clear();
		}
	}

	@Override
	public void onKeyPressed(short key)
	{
		if (this.state == PAUSED && (key == Keys.KEY_ATTACK || key == Keys.KEY_RUN)) this.nextMessage();
	}

	@Override
	public void onKeyReleased(short key)
	{}

	protected void reformLines(int maxwidth)
	{
		ArrayList<String> l = TextRenderer.splitLines(this.currentMessage().toString(), maxwidth);
		for (String line : l)
			this.lines.add(TextRenderer.decode(line));
	}

	@Override
	public void update()
	{
		++this.arrowtick;
		if (this.arrowtick >= ARROW_TICK_LENGTH) this.arrowtick = 0;
	}
}
