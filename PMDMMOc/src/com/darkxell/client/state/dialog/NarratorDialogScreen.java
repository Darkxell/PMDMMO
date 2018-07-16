package com.darkxell.client.state.dialog;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.renderers.TextRenderer.PMDChar;
import com.darkxell.client.ui.Keys.Key;
import com.darkxell.common.util.language.Message;

public class NarratorDialogScreen extends DialogScreen
{
	public static final int FADETIME = 30;

	private int fadeTick = 0;
	private boolean fadingOut = false;

	public NarratorDialogScreen(Message message)
	{
		super(message);
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		if (this.lines.isEmpty()) this.reformLines(width * 3 / 4);

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);

		int y = height / 2 - TextRenderer.height() * this.lines.size() - TextRenderer.lineSpacing() * (this.lines.size() - 1);
		for (int i = 0; i < this.lines.size(); ++i)
		{
			List<PMDChar> line = this.lines.get(i);
			int x = width / 2 - TextRenderer.width(line) / 2;
			TextRenderer.render(g, line, x, y, false);
			y += TextRenderer.height() + TextRenderer.lineSpacing();
		}

		if (this.state == DialogScreenState.PAUSED && this.arrowtick > 9 && this.parentState.isMain())
			g.drawImage(arrow, width / 2 - arrow.getWidth() / 2, y + TextRenderer.lineSpacing(), null);

		if (this.fadeTick < FADETIME)
		{
			double alpha = 255 - (this.fadeTick * 1. / FADETIME) * 255;
			g.setColor(new Color(0, 0, 0, (int) alpha));
			g.fillRect(0, 0, width, height);
		}
	}

	@Override
	protected void requestNextLine()
	{
		this.state = DialogScreenState.PRINTING;
		this.fadingOut = true;
	}

	@Override
	public boolean shouldRenderBackground()
	{
		return false;
	}

	@Override
	public void update()
	{
		if (this.state == DialogScreenState.PRINTING && !this.lines.isEmpty())
		{
			this.fadeTick += this.fadingOut ? -1 : 1;
			if (!this.fadingOut && this.fadeTick >= FADETIME)
			{
				this.state = DialogScreenState.PAUSED;
				this.fadeTick = FADETIME;
			} else if (this.fadingOut && this.fadeTick <= 0)
			{
				this.fadingOut = false;
				this.parentState.nextMessage();
			}
			if (this.state == DialogScreenState.PAUSED && Key.RUN.isPressed() && this.parentState.isMain()) this.requestNextLine();
		}

		++this.arrowtick;
		if (this.arrowtick >= ARROW_TICK_LENGTH) this.arrowtick = 0;
	}

}
