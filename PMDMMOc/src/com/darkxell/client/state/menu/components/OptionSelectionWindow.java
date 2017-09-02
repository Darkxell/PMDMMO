package com.darkxell.client.state.menu.components;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.darkxell.client.resources.images.MenuHudSpriteset;
import com.darkxell.client.state.menu.AbstractMenuState;
import com.darkxell.client.state.menu.AbstractMenuState.MenuOption;
import com.darkxell.client.state.menu.AbstractMenuState.MenuTab;

public class OptionSelectionWindow extends MenuWindow
{
	private static final BufferedImage selectionArrow = MenuHudSpriteset.instance.selectionArrow();

	private int cursor = 0;
	private final AbstractMenuState menu;

	public OptionSelectionWindow(AbstractMenuState menu, Rectangle dimensions)
	{
		super(dimensions);
		this.menu = menu;
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		super.render(g, width, height);

		// Text
		g.setColor(Color.BLACK);
		int x = MARGIN_X + this.dimensions.x;
		int y = MARGIN_Y + this.dimensions.y + MenuTab.OPTION_SPACE / 2;
		for (MenuOption option : this.menu.currentTab().options())
		{
			g.drawString(option.message.toString(), x, y + g.getFont().getSize());
			if (this.cursor > 9 && this.menu.currentOption() == option) g.drawImage(selectionArrow, x - selectionArrow.getWidth() - 4, y
					+ g.getFont().getSize() / 2 - selectionArrow.getHeight() / 2, null);
			y += g.getFont().getSize() + MenuTab.OPTION_SPACE;
		}
	}

	@Override
	public void update()
	{
		super.update();

		++this.cursor;
		if (this.cursor > 20) this.cursor = 0;
	}

}
