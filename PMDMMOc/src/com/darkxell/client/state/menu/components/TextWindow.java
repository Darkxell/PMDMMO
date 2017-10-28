package com.darkxell.client.state.menu.components;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.resources.images.MenuHudSpriteset;
import com.darkxell.common.util.language.Message;

public class TextWindow extends MenuWindow {

	private int cursor = 0;
	/**
	 * True if this Window leads to another when accepted. (will draw an arrow
	 * at bottom.)
	 */
	public final boolean hasNext;
	public boolean leftTab = false, rightTab = false;
	private ArrayList<String> lines;
	/** The message to display. */
	private Message message;

	public TextWindow(Rectangle dimensions, Message message, boolean hasNext) {
		super(dimensions);
		this.message = message;
		this.hasNext = hasNext;
	}

	@Override
	public void render(Graphics2D g, Message name, int width, int height) {
		super.render(g, name, width, height);

		if (this.lines == null)
			this.lines = TextRenderer.splitLines(this.message.toString(), this.inside().width - 10);

		int x = this.inside().x + 5, y = this.inside().y + 5;
		for (String line : this.lines) {
			TextRenderer.render(g, line, x, y);
			y += TextRenderer.height() + TextRenderer.lineSpacing();
		}

		if (this.rightTab)
			g.drawImage(MenuHudSpriteset.TAB_ARROW_RIGHT,
					(int) this.inside.getMaxX() - MenuHudSpriteset.TAB_ARROW_RIGHT.getWidth(),
					this.dimensions.y - MenuHudSpriteset.TAB_ARROW_RIGHT.getHeight() / 3, null);
		if (this.leftTab)
			g.drawImage(MenuHudSpriteset.TAB_ARROW_LEFT,
					(int) this.inside.getMaxX() - MenuHudSpriteset.TAB_ARROW_LEFT.getWidth()
							- MenuHudSpriteset.TAB_ARROW_RIGHT.getWidth() - 5,
					this.dimensions.y - MenuHudSpriteset.TAB_ARROW_LEFT.getHeight() / 3, null);

		if (this.hasNext) {
			x = this.dimensions.x - this.dimensions.width / 2 - MenuHudSpriteset.SELECTION_ARROW.getWidth() / 2;
			y = (int) (this.dimensions.getMaxY() - MenuHudSpriteset.SELECTION_ARROW.getHeight() / 2);
			g.drawImage(MenuHudSpriteset.SELECTION_ARROW, x, y, null);
		}
	}

	public void setMessage(Message message) {
		this.message = message;
		this.lines = null;
	}

	public void update() {
		++this.cursor;
		if (this.cursor > 20)
			this.cursor = 0;
	}

}
