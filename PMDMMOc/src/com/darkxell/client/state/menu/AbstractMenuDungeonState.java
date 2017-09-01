package com.darkxell.client.state.menu;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.darkxell.client.resources.images.MenuHudSpriteset;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.ui.Keys;
import com.darkxell.common.util.Message;

public abstract class AbstractMenuDungeonState extends AbstractState
{

	public static class MenuOption
	{
		/** True if this Option can be selected. */
		public boolean isEnabled;
		/** The name of this Option. */
		public final Message message;

		public MenuOption(Message message)
		{
			this(message, true);
		}

		public MenuOption(Message message, boolean isEnabled)
		{
			this.message = message;
			this.isEnabled = isEnabled;
		}
	}

	public static class MenuTab
	{

		public final Message name;
		ArrayList<MenuOption> options;

		public MenuTab(Message name)
		{
			this.name = name;
			this.options = new ArrayList<AbstractMenuDungeonState.MenuOption>();
		}

		public int height(Graphics2D g)
		{
			return this.options.size() * (g.getFont().getSize() + 5);
		}

		public int width(Graphics2D g)
		{
			int width = 0;
			for (MenuOption option : this.options)
				width = Math.max(width, g.getFontMetrics().stringWidth(option.message.toString()));
			return width;
		}

	}

	private static final BufferedImage selectionArrow = MenuHudSpriteset.instance.selectionArrow();

	/** The state to draw behind this menu State. */
	public final AbstractState backgroundState;
	/** The main window to display the options in. */
	private MenuWindow mainWindow;
	/** The currently selected option. */
	private int tab = 0, selection = 0;
	/** The tabs of this Menu. */
	protected ArrayList<MenuTab> tabs;

	public AbstractMenuDungeonState(AbstractState backgroundState)
	{
		this.backgroundState = backgroundState;
		this.tabs = new ArrayList<MenuTab>();
		this.createOptions();
	}

	protected Dimension autoDimensions(Graphics2D g)
	{
		int width = 0, height = 0;
		for (MenuTab tab : this.tabs)
		{
			width = Math.max(width, tab.width(g));
			height = Math.max(height, tab.height(g));
		}
		width += 15; // marges
		height += 15;

		return new Dimension(width, height);
	}

	/** Creates this Menu's options. */
	protected abstract void createOptions();

	protected MenuOption currentOption()
	{
		return this.currentTab().options.get(this.selection);
	}

	protected MenuTab currentTab()
	{
		return this.tabs.get(this.tab);
	}

	/** @return This Window's dimensions. Use {@link AbstractMenuDungeonState#autoDimensions} for automatic dimensions from the options. */
	protected abstract Dimension mainWindowDimensions(Graphics2D g);

	/** Called when the player presses the "back" button. */
	protected abstract void onExit();

	@Override
	public void onKeyPressed(short key)
	{
		if (key == Keys.KEY_LEFT) --this.tab;
		else if (key == Keys.KEY_RIGHT) ++this.tab;
		else if (key == Keys.KEY_UP) --this.selection;
		else if (key == Keys.KEY_DOWN) ++this.selection;
		else if (key == Keys.KEY_ATTACK) this.onOptionSelected(this.currentOption());
		else if (key == Keys.KEY_RUN) this.onExit();

		if (key == Keys.KEY_LEFT || key == Keys.KEY_RIGHT)
		{
			if (this.tab == -1) this.tab = this.tabs.size() - 1;
			else if (this.tab == this.tabs.size()) this.tab = 0;
			else if (this.selection >= this.currentTab().options.size()) this.selection = this.currentTab().options.size() - 1;
		} else if (key == Keys.KEY_UP || key == Keys.KEY_DOWN)
		{
			if (this.selection == -1) this.selection = this.currentTab().options.size() - 1;
			else if (this.selection == this.currentTab().options.size()) this.selection = 0;
		}
	}

	/** Called when the player chooses the input Option. */
	protected abstract void onOptionSelected(MenuOption option);

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		if (this.mainWindow == null) this.mainWindow = new MenuWindow(this.mainWindowDimensions(g));

		if (this.backgroundState != null) this.backgroundState.render(g, width, height);
		this.mainWindow.render(g, width, height);
	}

	@Override
	public void update()
	{
		if (this.backgroundState != null) this.backgroundState.update();
	}

}
