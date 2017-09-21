package com.darkxell.client.state;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

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

	public static interface DialogEndListener
	{
		/** Called when the input dialog ends. */
		public void onDialogEnd(DialogState dialog);
	}

	public static class DialogScreen
	{
		/** The emotion of the Pokémon. Unused for now. */
		public final short emotion;
		/** The Message to show in this Screen. */
		public final Message message;
		/** The Pokémon talking. null if not a Pokémon. */
		public final Pokemon pokemon;

		public DialogScreen(Message message)
		{
			this(null, message);
		}

		public DialogScreen(Pokemon pokemon, Message message)
		{
			this((short) 0, pokemon, message);
		}

		public DialogScreen(short emotion, Pokemon pokemon, Message message)
		{
			this.emotion = emotion;
			this.pokemon = pokemon;
			this.message = message;

			if (this.pokemon != null) this.message.addPrefix(new Message(": ", false)).addPrefix(this.pokemon.getNickname());
		}
	}

	private static final BufferedImage arrow = MenuHudSpriteset.instance.nextWindowArrow();
	private static final byte PRINTING = 0, PAUSED = 1, SWITCHING = 2;

	private int arrowtick;
	/** The State to draw in this State's background. */
	public final AbstractState backgroundState;
	/** The current line to display. When displayed, paused until the player skips. */
	private int currentLine;
	/** The current screen. */
	private int currentScreen;
	/** The current maximum character to print. */
	private int cursor;
	/** True if this Dialog's window is opaque. */
	public boolean isOpaque;
	/** The split lines of the current messages. */
	private ArrayList<String> lines;
	/** The listener called when this Dialog ends. If null, the Background State is used instead. */
	private final DialogEndListener listener;
	/** Text offset. */
	private int offset;
	/** The screens to show. */
	private final List<DialogScreen> screens;
	/** True if the end of the current line has been reached. */
	private byte state;
	/** The offset to reach to switch back to printing. */
	private int targetOffset;

	public DialogState(AbstractState backgroundState, DialogEndListener listener, boolean isOpaque, List<DialogScreen> screens)
	{
		this.screens = screens;

		this.backgroundState = backgroundState;
		this.listener = listener;
		this.isOpaque = isOpaque;
		this.lines = new ArrayList<String>();
		this.currentScreen = this.arrowtick = this.cursor = this.offset = this.targetOffset = 0;
		this.currentLine = 2;
		this.state = PRINTING;
	}

	public DialogState(AbstractState backgroundState, DialogEndListener listener, List<DialogScreen> elements)
	{
		this(backgroundState, listener, true, elements);
	}

	public DialogState(AbstractState backgroundState, List<DialogScreen> elements)
	{
		this(backgroundState, null, elements);
	}

	/** @return Length of the current Message. */
	private int currentLength()
	{
		if (this.lines.isEmpty()) return 0;
		int length = 0;
		for (int line = 0; line <= this.currentLine && line < this.lines.size(); ++line)
			length += this.lines.get(line).length();
		return length;
	}

	public Message currentMessage()
	{
		return this.screens.get(this.currentScreen).message;
	}

	private void nextLine()
	{
		++this.currentLine;
		if (this.currentLine >= this.lines.size()) this.nextMessage();
		this.state = PRINTING;
	}

	/** Skips to the next message. */
	public void nextMessage()
	{
		if (this.currentScreen == this.screens.size() - 1)
		{
			if (this.listener == null) Launcher.stateManager.setState(this.backgroundState);
			else this.listener.onDialogEnd(this);
		} else
		{
			++this.currentScreen;
			this.lines.clear();
			this.cursor = 0;
			this.offset = 0;
			this.currentLine = 2;
			this.targetOffset = 0;
		}
	}

	@Override
	public void onKeyPressed(short key)
	{
		if (this.state == PAUSED && (key == Keys.KEY_ATTACK || key == Keys.KEY_RUN)) this.requestNextLine();
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

		if (this.lines.isEmpty()) this.lines.addAll(TextRenderer.instance.splitLines(this.currentMessage().toString(), inside.width));

		g.drawImage(this.isOpaque ? Hud.textwindow : Hud.textwindow_transparent, box.x, box.y, box.width, box.height, null);
		Shape c = g.getClip();
		g.setClip(inside);
		int length = 0;
		for (int i = 0; i < this.lines.size() && length < this.cursor; ++i)
		{
			int count = Math.min(this.cursor - length, this.lines.get(i).length());
			TextRenderer.instance.render(g, this.lines.get(i).substring(0, count), inside.x, inside.y - this.offset + i
					* (TextRenderer.CHAR_HEIGHT + TextRenderer.LINE_SPACING));
			length += count;
		}
		g.setClip(c);

		if (this.state == PAUSED && this.arrowtick > 9) g.drawImage(arrow, box.x + box.width / 2 - arrow.getWidth() / 2,
				(int) (box.getMaxY() - arrow.getHeight() * 3 / 4), null);

		if (this.currentScreen < this.screens.size() && this.screens.get(this.currentScreen).pokemon != null)
		{
			int x = (int) (box.getMaxX() - Hud.portrait.getWidth());
			int y = (int) (box.y - Hud.portrait.getHeight() - 5);
			g.drawImage(PokemonPortrait.portrait(this.screens.get(this.currentScreen).pokemon), x + 4, y + 4, null);
			g.drawImage(Hud.portrait, x, y, null);
		}
	}

	private void requestNextLine()
	{
		if (this.currentLine < this.lines.size() - 1 || this.switchAnimation())
		{
			this.state = SWITCHING;
			this.targetOffset = this.offset + TextRenderer.CHAR_HEIGHT + TextRenderer.LINE_SPACING;
			if (this.currentLine >= this.lines.size() - 1) this.targetOffset += (TextRenderer.CHAR_HEIGHT + TextRenderer.LINE_SPACING) * 2;
		} else this.nextLine();
	}

	/** @return True if the current message should be followed with a switching animation. */
	private boolean switchAnimation()
	{
		if (this.currentScreen >= this.screens.size() - 1) return false;
		return this.screens.get(this.currentScreen).emotion != this.screens.get(this.currentScreen + 1).emotion
				|| (this.screens.get(this.currentScreen).pokemon != null && this.screens.get(this.currentScreen).pokemon.equals(this.screens
						.get(this.currentScreen + 1).pokemon));
	}

	@Override
	public void update()
	{
		if (this.state == PRINTING && !this.lines.isEmpty())
		{
			++this.cursor;
			if (this.cursor >= this.currentLength()) this.state = PAUSED;
			if (this.state == PAUSED && Keys.isPressed(Keys.KEY_RUN)) this.requestNextLine();
		} else if (this.state == SWITCHING)
		{
			this.offset += 3;
			if (this.offset >= this.targetOffset + 10) // +10 to add some delay
			{
				this.offset = this.targetOffset;
				this.nextLine();
			}
		}

		++this.arrowtick;
		if (this.arrowtick > 19) this.arrowtick = 0;

		if (this.backgroundState != null) this.backgroundState.update();
	}
}
