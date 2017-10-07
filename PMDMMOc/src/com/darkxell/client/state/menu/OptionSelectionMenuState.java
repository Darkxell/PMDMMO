package com.darkxell.client.state.menu;

import java.awt.Graphics2D;

import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.menu.components.MenuWindow;
import com.darkxell.client.state.menu.components.OptionSelectionWindow;

public abstract class OptionSelectionMenuState extends AbstractMenuState
{

	/** The main window to display the options in. */
	private OptionSelectionWindow mainWindow;
	protected boolean isOpaque = false;

	public OptionSelectionMenuState(AbstractState backgroundState)
	{
		super(backgroundState);
	}

	public MenuWindow getMainWindow()
	{
		return this.mainWindow;
	}

	@Override
	protected void onTabChanged(MenuTab tab)
	{
		super.onTabChanged(tab);
		this.mainWindow = new OptionSelectionWindow(this, this.mainWindowDimensions());
		this.mainWindow.isOpaque = this.isOpaque;
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		super.render(g, width, height);

		if (this.mainWindow == null)
		{
			this.mainWindow = new OptionSelectionWindow(this, this.mainWindowDimensions());
			this.mainWindow.isOpaque = this.isOpaque;
		}
		if (this.tabs.size() != 0) this.mainWindow.render(g, this.currentTab().name, width, height);
	}

	@Override
	public void update()
	{
		super.update();
		if (this.mainWindow != null) this.mainWindow.update();
	}

}
