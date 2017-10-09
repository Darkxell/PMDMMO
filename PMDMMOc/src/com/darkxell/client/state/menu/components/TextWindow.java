package com.darkxell.client.state.menu.components;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.resources.images.MenuHudSpriteset;
import com.darkxell.common.util.language.Message;

public class TextWindow extends MenuWindow
{
	private static final BufferedImage nextArrow = MenuHudSpriteset.instance.nextWindowArrow();

	private int cursor = 0;
	/** True if this Window leads to another when accepted. (will draw an arrow at bottom.) */
	public final boolean hasNext;
	public boolean leftTab = false, rightTab = false;
	private ArrayList<String> lines;
	/** The message to display. */
	private Message message;

	public TextWindow(Rectangle dimensions, Message message, boolean hasNext)
	{
		super(dimensions);
		this.message = message;
		this.hasNext = hasNext;
	}

	@Override
	public void render(Graphics2D g, Message name, int width, int height)
	{
		super.render(g, name, width, height);

		if (this.lines == null) this.lines = TextRenderer.instance.splitLines(this.message.toString(), this.inside().width - 10);

		int x = this.inside().x + 5, y = this.inside().y + 5;
		for (String line : this.lines)
		{
			TextRenderer.instance.render(g, line, x, y);
			y += TextRenderer.CHAR_HEIGHT + TextRenderer.LINE_SPACING;
		}

		if (this.rightTab) g.drawImage(MenuHudSpriteset.instance.tabArrowRight(), (int) this.inside.getMaxX()
				- MenuHudSpriteset.instance.tabArrowRight().getWidth(), this.dimensions.y - MenuHudSpriteset.instance.tabArrowRight().getHeight() / 3, null);
		if (this.leftTab) g.drawImage(MenuHudSpriteset.instance.tabArrowLeft(), (int) this.inside.getMaxX()
				- MenuHudSpriteset.instance.tabArrowLeft().getWidth() - MenuHudSpriteset.instance.tabArrowRight().getWidth() - 5, this.dimensions.y
				- MenuHudSpriteset.instance.tabArrowLeft().getHeight() / 3, null);

		if (this.hasNext)
		{
			x = this.dimensions.x - this.dimensions.width / 2 - nextArrow.getWidth() / 2;
			y = (int) (this.dimensions.getMaxY() - nextArrow.getHeight() / 2);
			g.drawImage(nextArrow, x, y, null);
		}
	}

	public void setMessage(Message message)
	{
		this.message = message;
		this.lines = null;
	}

	public void update()
	{
		++this.cursor;
		if (this.cursor > 20) this.cursor = 0;
	}

}
