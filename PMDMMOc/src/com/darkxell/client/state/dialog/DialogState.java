package com.darkxell.client.state.dialog;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.Arrays;
import java.util.List;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.renderers.TextRenderer.PMDChar;
import com.darkxell.client.resources.images.others.Hud;
import com.darkxell.client.resources.images.pokemon.PokemonPortrait;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.ui.Keys;

public class DialogState extends AbstractDialogState
{
	/** The State to draw in this State's background. */
	public final AbstractState backgroundState;
	/** The current line to display. When displayed, paused until the player skips. */
	private int currentLine;
	/** The current maximum character to print. */
	private int cursor;
	private Rectangle dialogBox;
	/** True if this Dialog's window is opaque. */
	public boolean isOpaque;
	/** Text offset. */
	private int offset;
	/** The offset to reach. */
	private int targetOffset;

	public DialogState(AbstractState backgroundState, DialogEndListener listener, boolean isOpaque, DialogScreen screen)
	{
		this(backgroundState, listener, isOpaque, Arrays.asList(screen));
	}

	public DialogState(AbstractState backgroundState, DialogEndListener listener, boolean isOpaque, List<DialogScreen> screens)
	{
		super(listener, screens);

		this.backgroundState = backgroundState;
		this.isOpaque = isOpaque;
		this.cursor = this.offset = this.targetOffset = 0;
		this.currentLine = 2;
	}

	public DialogState(AbstractState backgroundState, DialogEndListener listener, DialogScreen screen)
	{
		this(backgroundState, listener, true, Arrays.asList(screen));
	}

	public DialogState(AbstractState backgroundState, DialogEndListener listener, List<DialogScreen> elements)
	{
		this(backgroundState, listener, true, elements);
	}

	public DialogState(AbstractState backgroundState, DialogScreen screen)
	{
		this(backgroundState, Arrays.asList(screen));
	}

	public DialogState(AbstractState backgroundState, List<DialogScreen> elements)
	{
		this(backgroundState, null, elements);
	}

	@Override
	int currentLength()
	{
		if (this.lines.isEmpty()) return 0;
		int length = 0;
		for (int line = 0; line <= this.currentLine && line < this.lines.size(); ++line)
			length += this.lines.get(line).size();
		return length;
	}

	public Rectangle dialogBox()
	{
		return this.dialogBox;
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
			if (this.listener == null && this.backgroundState != null) Persistance.stateManager.setState(this.backgroundState);
			else if (this.listener != null) this.listener.onDialogEnd(this);
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
		this.dialogBox = new Rectangle(20, height - temp_height - 5, temp_width, temp_height);
		int marginx = this.dialogBox.width / 20, marginy = this.dialogBox.height / 5;
		Rectangle inside = new Rectangle(this.dialogBox.x + marginx, this.dialogBox.y + marginy, this.dialogBox.width - marginx * 2,
				this.dialogBox.height - marginy * 2);

		if (this.lines.isEmpty()) this.reformLines(inside.width);

		g.drawImage(this.isOpaque ? Hud.textwindow : Hud.textwindow_transparent, this.dialogBox.x, this.dialogBox.y, this.dialogBox.width,
				this.dialogBox.height, null);
		Shape c = g.getClip();
		g.setClip(inside);
		int length = 0;
		for (int i = 0; i < this.lines.size() && length < this.cursor; ++i)
		{
			int count = Math.min(this.cursor - length, this.lines.get(i).size());
			List<PMDChar> line = this.lines.get(i).subList(0, count);
			int x = inside.x;
			if (this.currentScreen().isCentered) x += inside.getWidth() / 2 - TextRenderer.width(line) / 2;

			TextRenderer.render(g, line, x, inside.y - this.offset + i * (TextRenderer.height() + TextRenderer.lineSpacing()), false);
			length += count;
		}
		g.setClip(c);

		if (this.state == PAUSED && this.arrowtick > 9 && this.isMain()) g.drawImage(arrow, this.dialogBox.x + this.dialogBox.width / 2 - arrow.getWidth() / 2,
				(int) (this.dialogBox.getMaxY() - arrow.getHeight() * 3 / 4), null);

		if (this.currentScreen < this.screens.size() && this.currentScreen().pokemon != null) PokemonPortrait.drawPortrait(g, this.currentScreen().pokemon,
				this.currentScreen().shiny, this.dialogBox.x + 5, this.dialogBox.y - Hud.portrait.getHeight() - 5);

	}

	private void requestNextLine()
	{
		if (this.currentLine < this.lines.size() - 1 || this.switchAnimation())
		{
			this.state = SWITCHING;
			this.targetOffset = this.offset + TextRenderer.height() + TextRenderer.lineSpacing();
			if (this.currentLine >= this.lines.size() - 1) this.targetOffset += (TextRenderer.height() + TextRenderer.lineSpacing()) * 2;
		} else this.nextLine();
	}

	/** @return True if the current message should be followed with a switching animation. */
	private boolean switchAnimation()
	{
		if (this.currentScreen >= this.screens.size() - 1) return false;
		return this.currentScreen().emotion != this.screens.get(this.currentScreen + 1).emotion
				|| (this.currentScreen().pokemon != null && this.currentScreen().pokemon.equals(this.screens.get(this.currentScreen + 1).pokemon));
	}

	@Override
	public void update()
	{
		if (this.state == PRINTING && !this.lines.isEmpty())
		{
			if (this.currentScreen().isInstant) this.cursor = this.currentLength();
			else++this.cursor;
			if (this.cursor >= this.currentLength()) this.state = PAUSED;
			if (this.state == PAUSED && Keys.isPressed(Keys.KEY_RUN) && this.isMain()) this.requestNextLine();
		} else if (this.state == SWITCHING)
		{
			this.offset += 3;
			if (this.offset >= this.targetOffset + 10) // +10 to add some delay
			{
				this.offset = this.targetOffset;
				this.nextLine();
			}
		}

		super.update();

		if (this.backgroundState != null) this.backgroundState.update();
	}
}
