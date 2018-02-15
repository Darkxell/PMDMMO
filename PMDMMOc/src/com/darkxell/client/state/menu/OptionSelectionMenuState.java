package com.darkxell.client.state.menu;

import java.awt.Graphics2D;

import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.menu.components.OptionSelectionWindow;

public abstract class OptionSelectionMenuState extends AbstractMenuState
{

	private MenuOption hovered;
	protected boolean isOpaque = false;
	/** The main window to display the options in. */
	private OptionSelectionWindow mainWindow;

	public OptionSelectionMenuState(AbstractState backgroundState)
	{
		super(backgroundState);
	}

	public MenuOption getHoveredOption()
	{
		return this.hovered;
	}

	public OptionSelectionWindow getMainWindow()
	{
		return this.mainWindow;
	}

	@Override
	public void onMouseClick(int x, int y)
	{
		super.onMouseClick(x, y);
		boolean inside = this.getMainWindow().inside().contains(x, y);
		if (inside)
		{
			if (this.getHoveredOption() != null) this.onOptionSelected(this.getHoveredOption());
		} else this.onExit();
	}

	@Override
	public void onMouseMove(int x, int y)
	{
		super.onMouseMove(x, y);
		this.hovered = this.getMainWindow().optionAt(x, y);
		if (this.hovered != null)
		{
			MenuOption[] options = this.currentTab().options();
			for (int i = 0; i < options.length; i++)
				if (options[i] == this.hovered)
				{
					this.selection = i;
					this.onOptionChanged(this.hovered);
					break;
				}
		}
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
			System.out.println("asked dimensions");
			this.mainWindow.isOpaque = this.isOpaque;
		}
		if (this.tabs.size() != 0) this.getMainWindow().render(g, this.currentTab().name, width, height);
	}

	@Override
	public void update()
	{
		super.update();
		if (this.getMainWindow() != null) this.getMainWindow().update();
	}

}
