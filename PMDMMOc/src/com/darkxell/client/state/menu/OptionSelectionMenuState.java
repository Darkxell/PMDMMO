package com.darkxell.client.state.menu;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.menu.components.MenuWindow;
import com.darkxell.client.state.menu.components.OptionSelectionWindow;

public abstract class OptionSelectionMenuState extends AbstractMenuState
{

	/** The main window to display the options in. */
	private OptionSelectionWindow mainWindow;

	public OptionSelectionMenuState(AbstractState backgroundState)
	{
		super(backgroundState);
	}

	public MenuWindow getMainWindow()
	{
		return this.mainWindow;
	}

	/** @return This Window's dimensions. */
	protected Rectangle mainWindowDimensions(Graphics2D g)
	{
		int width = 0, height = 0;
		for (MenuTab tab : this.tabs)
		{
			width = Math.max(width, tab.width(g));
			height = Math.max(height, tab.height(g));
		}
		width += OptionSelectionWindow.MARGIN_X * 2;
		height += OptionSelectionWindow.MARGIN_Y * 2;

		return new Rectangle(16, 32, width, height);
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		super.render(g, width, height);

		if (this.mainWindow == null) this.mainWindow = new OptionSelectionWindow(this, this.mainWindowDimensions(g));
		if (this.tabs.size() != 0) this.mainWindow.render(g, this.currentTab().name, width, height);
	}

	@Override
	public void update()
	{
		super.update();
		if (this.mainWindow != null) this.mainWindow.update();
	}

}
