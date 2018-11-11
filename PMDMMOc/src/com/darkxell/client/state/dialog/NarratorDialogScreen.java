package com.darkxell.client.state.dialog;

import java.awt.AlphaComposite;
import java.awt.Composite;
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
	public boolean forceBlackBackground = true;

	public NarratorDialogScreen(Message message)
	{
		super(message);
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		if (this.lines.isEmpty()) this.reformLines(width * 3 / 4);

		Composite previousComp = g.getComposite();
		if (this.fadeTick < FADETIME)
		{
			double alpha = this.fadeTick == 0 ? 0 : this.fadeTick * 1. / FADETIME;
			AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha);
			g.setComposite(ac);
		}

		int y = height / 2 - TextRenderer.height() * this.lines.size() - TextRenderer.lineSpacing() * (this.lines.size() - 1);
		for (int i = 0; i < this.lines.size(); ++i)
		{
			List<PMDChar> line = this.lines.get(i);
			int x = width / 2 - TextRenderer.width(line) / 2;
			TextRenderer.render(g, line, x, y);
			y += TextRenderer.height() + TextRenderer.lineSpacing();
		}

		if (this.state == DialogScreenState.PAUSED && this.arrowtick > 9 && this.parentState.isMain())
			g.drawImage(arrow, width / 2 - arrow.getWidth() / 2, y + TextRenderer.lineSpacing(), null);

		if (this.fadeTick < FADETIME) g.setComposite(previousComp);

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
		return !this.forceBlackBackground;
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
				this.state = DialogScreenState.PAUSED;
				this.fadingOut = false;
				this.parentState.nextMessage();
			}
			if (this.state == DialogScreenState.PAUSED && Key.RUN.isPressed() && this.parentState.isMain()) this.requestNextLine();
		}

		++this.arrowtick;
		if (this.arrowtick >= ARROW_TICK_LENGTH) this.arrowtick = 0;
	}

}
