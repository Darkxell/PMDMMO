package com.darkxell.client.state.menu.components;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.darkxell.client.resources.images.MenuHudSpriteset;
import com.darkxell.client.state.menu.AbstractMenuState;
import com.darkxell.client.state.menu.AbstractMenuState.MenuOption;
import com.darkxell.client.state.menu.AbstractMenuState.MenuTab;
import com.darkxell.common.util.Message;

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
	public void render(Graphics2D g, Message name, int width, int height)
	{
		super.render(g, name, width, height);

		// Tabs
		MenuTab[] tabs = this.menu.tabs();
		if (tabs.length != 0)
		{
			boolean left = tabs[0] != this.menu.currentTab();
			boolean right = tabs[tabs.length - 1] != this.menu.currentTab();
			if (right) g
					.drawImage(MenuHudSpriteset.instance.tabArrowRight(), (int) this.inside.getMaxX() - MenuHudSpriteset.instance.tabArrowRight().getWidth(),
							this.dimensions.y - MenuHudSpriteset.instance.tabArrowRight().getHeight() / 3, null);
			if (left) g.drawImage(MenuHudSpriteset.instance.tabArrowLeft(), (int) this.inside.getMaxX() - MenuHudSpriteset.instance.tabArrowLeft().getWidth()
					- MenuHudSpriteset.instance.tabArrowRight().getWidth() - 5, this.dimensions.y - MenuHudSpriteset.instance.tabArrowLeft().getHeight() / 3,
					null);
		}

		// Text
		g.setColor(Color.BLACK);
		int x = MARGIN_X + this.dimensions.x;
		int y = MARGIN_Y + this.dimensions.y + MenuTab.OPTION_SPACE / 2;
		for (MenuOption option : this.menu.currentTab().options())
		{
			g.drawString(option.name.toString(), x, y + g.getFont().getSize());
			if (this.cursor > 9 && this.menu.currentOption() == option) g.drawImage(
					this.menu.isMain() ? selectionArrow : MenuHudSpriteset.instance.selectedArrow(), x - selectionArrow.getWidth() - 4, y
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
