package com.darkxell.client.state.menu.components;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.resources.Palette;
import com.darkxell.client.state.menu.OptionSelectionMenuState;
import com.darkxell.client.state.menu.AbstractMenuState.MenuOption;
import com.darkxell.client.state.menu.menus.ControlsMenuState.ControlMenuOption;

public class ControlsWindow extends OptionSelectionWindow
{

	public ControlsWindow(OptionSelectionMenuState menu, Rectangle dimensions)
	{
		super(menu, dimensions);
	}

	@Override
	protected void drawOption(Graphics2D g, MenuOption option, int x, int y)
	{
		super.drawOption(g, option, x, y);
		if (y - 3 != this.inside().y)
		{
			g.setColor(Palette.TRANSPARENT_GRAY);
			g.drawLine(x, y - 2, x + this.dimensions.width - MARGIN_X * 2, y - 2);
		}
		x += this.dimensions.width - MARGIN_X * 2;

		int key = ((ControlMenuOption) option).newValue();
		String name = KeyEvent.getKeyText(key);
		x -= TextRenderer.width(name) + 2;
		TextRenderer.render(g, name, x, y);

	}

}
