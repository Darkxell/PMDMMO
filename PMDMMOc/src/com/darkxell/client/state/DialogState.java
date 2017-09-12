package com.darkxell.client.state;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.resources.images.Hud;
import com.darkxell.client.resources.images.MenuHudSpriteset;
import com.darkxell.client.resources.images.PokemonPortrait;
import com.darkxell.client.ui.Keys;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.Message;

public class DialogState extends AbstractState
{

	public static class DialogElement
	{
		@SuppressWarnings("unused")
		private short emotion;
		private Message[] messages;
		private Pokemon pokemon;

		public DialogElement(Pokemon pokemon, Message... messages)
		{
			this((short) 0, pokemon, messages);
		}

		public DialogElement(short emotion, Pokemon pokemon, Message... messages)
		{
			this.emotion = emotion;
			this.pokemon = pokemon;
			this.messages = messages;

			if (this.pokemon != null) for (Message m : this.messages)
				m.addPrefix(new Message(": ", false)).addPrefix(this.pokemon.getNickname());
		}
	}

	public static interface DialogEndListener
	{
		/** Called when the input dialog ends. */
		public void onDialogEnd(DialogState dialog);
	}

	private static final BufferedImage arrow = MenuHudSpriteset.instance.nextWindowArrow();
	private static final int SWITCH_TIME = 20;

	private int arrowtick;
	/** The State to draw in this State's background. */
	public final AbstractState backgroundState;
	/** The split lines of the current messages. */
	private ArrayList<String> currentLines;
	private int currentMessage;
	/** Matches each message to the element describing it. */
	private final ArrayList<DialogElement> elements;
	/** True if the end of the current message has been reached. */
	private boolean endReached;
	public boolean isOpaque;
	/** The listener called when this Dialog ends. If null, the Background State is used instead. */
	private final DialogEndListener listener;
	/** The messages to show. */
	private final ArrayList<Message> messages;
	/** The current maximum character to print. */
	private int position;
	/** Positive if the current message is switching. -1 else. */
	private int switching;

	public DialogState(AbstractState backgroundState, DialogElement... elements)
	{
		this(backgroundState, null, elements);
	}

	public DialogState(AbstractState backgroundState, DialogEndListener listener, boolean isOpaque, DialogElement... elements)
	{
		this.elements = new ArrayList<DialogState.DialogElement>();
		this.messages = new ArrayList<Message>();

		for (DialogElement element : elements)
			for (Message message : element.messages)
			{
				this.messages.add(message);
				this.elements.add(element);
			}

		this.backgroundState = backgroundState;
		this.listener = listener;
		this.isOpaque = isOpaque;
		this.currentLines = new ArrayList<String>();
		this.currentMessage = this.arrowtick = this.position = 0;
		this.switching = -1;
		this.endReached = false;
	}

	public DialogState(AbstractState backgroundState, DialogEndListener listener, DialogElement... elements)
	{
		this(backgroundState, listener, true, elements);
	}

	/** @return Length of the current Message. */
	private int currentLength()
	{
		if (this.currentLines.isEmpty()) return 0;
		int length = 0;
		for (String line : this.currentLines)
			length += line.length();
		return length;
	}

	public Message currentMessage()
	{
		return this.messages.get(this.currentMessage);
	}

	/** Skips to the next message. */
	public void nextMessage()
	{
		if (this.currentMessage == this.messages.size() - 1)
		{
			if (this.listener == null) Launcher.stateManager.setState(this.backgroundState, 0);
			else this.listener.onDialogEnd(this);
		} else
		{
			++this.currentMessage;
			this.currentLines.clear();
			this.position = 0;
			this.switching = -1;
			this.endReached = false;
		}
	}

	@Override
	public void onKeyPressed(short key)
	{
		if (this.endReached && (key == Keys.KEY_ATTACK || key == Keys.KEY_RUN)) this.requestNextMessage();
	}

	@Override
	public void onKeyReleased(short key)
	{}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		if (this.backgroundState != null) this.backgroundState.render(g, width, height);

		int temp_width = width - 40;
		int temp_height = temp_width * Hud.textwindow.getHeight() / Hud.textwindow.getWidth();
		Rectangle box = new Rectangle(20, height - temp_height - 20, temp_width, temp_height);
		int marginx = box.width / 20, marginy = box.height / 5;
		Rectangle inside = new Rectangle(box.x + marginx, box.y + marginy, box.width - marginx * 2, box.height - marginy * 2);

		if (this.currentLines.isEmpty()) this.currentLines.addAll(TextRenderer.instance.splitLines(this.currentMessage().toString(), inside.width));

		g.drawImage(this.isOpaque ? Hud.textwindow : Hud.textwindow_transparent, box.x, box.y, box.width, box.height, null);
		Shape c = g.getClip();
		g.setClip(inside);
		int length = 0;
		int offset = 0;
		if (this.endReached && this.switching != -1) offset = (int) (-(1f * this.switching / SWITCH_TIME) * inside.height);
		for (int i = 0; i < this.currentLines.size() && length < this.position; ++i)
		{
			int count = Math.min(this.position - length, this.currentLines.get(i).length());
			TextRenderer.instance.render(g, this.currentLines.get(i).substring(0, count), inside.x, inside.y + offset + i
					* (TextRenderer.CHAR_HEIGHT + TextRenderer.LINE_SPACING));
			length += count;
		}
		g.setClip(c);

		if (this.endReached && this.switching == -1 && this.arrowtick > 9) g.drawImage(arrow, box.x + box.width / 2 - arrow.getWidth() / 2,
				(int) (box.getMaxY() - arrow.getHeight() * 3 / 4), null);

		if (this.currentMessage < this.elements.size() && this.elements.get(this.currentMessage).pokemon != null)
		{
			int x = (int) (box.getMaxX() - Hud.portrait.getWidth());
			int y = (int) (box.y - Hud.portrait.getHeight() - 10);
			g.drawImage(Hud.portrait, x, y, null);
			g.drawImage(PokemonPortrait.portrait(this.elements.get(this.currentMessage).pokemon), x + 3, y + 3, null);
		}
	}

	private void requestNextMessage()
	{
		if (this.switchAnimation()) this.switching = 0;
		else this.nextMessage();
	}

	/** @return True if the current message should be followed with a switching animation. */
	private boolean switchAnimation()
	{
		if (this.currentMessage == this.messages.size() - 1) return false;
		return this.elements.get(this.currentMessage).pokemon != null
				&& this.elements.get(this.currentMessage).pokemon.equals(this.elements.get(this.currentMessage + 1).pokemon);
	}

	@Override
	public void update()
	{
		if (!this.endReached && !this.currentLines.isEmpty())
		{
			++this.position;
			this.endReached = this.position >= this.currentLength();
			if (this.endReached && Keys.isPressed(Keys.KEY_RUN)) this.requestNextMessage();
		} else if (this.endReached && this.switching >= 0)
		{
			++this.switching;
			if (this.switching >= SWITCH_TIME) this.nextMessage();
		}

		++this.arrowtick;
		if (this.arrowtick > 19) this.arrowtick = 0;

		if (this.backgroundState != null) this.backgroundState.update();
	}
}
