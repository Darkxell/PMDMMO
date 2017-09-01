package com.darkxell.client.state.menu;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.darkxell.client.resources.images.MenuHudSpriteset;
import com.darkxell.client.state.menu.AbstractMenuState.MenuOption;
import com.darkxell.client.state.menu.AbstractMenuState.MenuTab;

public class MenuWindow
{
	public static final int MARGIN = 10, SELECTION_ARROW_SPACE = 8;
	private static final BufferedImage selectionArrow = MenuHudSpriteset.instance.selectionArrow();

	private int cursor = 0;
	private final Rectangle dimensions;
	private final AbstractMenuState menu;

	public MenuWindow(AbstractMenuState menu, Rectangle dimensions)
	{
		this.menu = menu;
		this.dimensions = dimensions;
	}

	public void render(Graphics2D g, int width, int height)
	{
		// TODO MenuWindow.render()
		g.setColor(Color.BLACK);
		g.drawRect(this.dimensions.x, this.dimensions.y, this.dimensions.width, this.dimensions.height);

		int x = MARGIN + SELECTION_ARROW_SPACE + this.dimensions.x, y = MARGIN + this.dimensions.y;
		for (MenuOption option : this.menu.currentTab().options)
		{
			g.drawString(option.message.toString(), x, y + g.getFont().getSize());
			if (this.cursor > 9 && this.menu.currentOption() == option) g.drawImage(selectionArrow, x - SELECTION_ARROW_SPACE, y + g.getFont().getSize() / 2
					- selectionArrow.getHeight() / 2, null);
			y += g.getFont().getSize() + MenuTab.OPTION_SPACE;
		}
	}

	public void update()
	{
		++this.cursor;
		if (this.cursor > 20) this.cursor = 0;
	}

}
