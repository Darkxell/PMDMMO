package com.darkxell.client.state.menu;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.darkxell.client.state.menu.AbstractMenuState.MenuTab;

public class MenuWindow
{

	private final Rectangle dimensions;

	public MenuWindow(Rectangle dimensions)
	{
		this.dimensions = dimensions;
	}

	public void render(Graphics2D g, MenuTab tab, int width, int height)
	{
		// TODO MenuWindow.render()
		g.drawRect(this.dimensions.x, this.dimensions.y, this.dimensions.width, this.dimensions.height);
	}

}
