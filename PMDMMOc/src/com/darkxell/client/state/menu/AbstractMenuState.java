package com.darkxell.client.state.menu;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import com.darkxell.client.resources.images.MenuHudSpriteset;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.ui.Keys;
import com.darkxell.common.util.Message;

public abstract class AbstractMenuState extends AbstractState
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
		public static final int OPTION_SPACE = 5;

		public final Message name;
		ArrayList<MenuOption> options;

		public MenuTab(Message name)
		{
			this.name = name;
			this.options = new ArrayList<AbstractMenuState.MenuOption>();
		}

		public int height(Graphics2D g)
		{
			return this.options.size() * (g.getFont().getSize() + OPTION_SPACE);
		}

		public int width(Graphics2D g)
		{
			int width = 0;
			for (MenuOption option : this.options)
				width = Math.max(width, g.getFontMetrics().stringWidth(option.message.toString()));
			return width;
		}

	}

	/** The state to draw behind this menu State. */
	public final AbstractState backgroundState;
	/** The main window to display the options in. */
	private MenuWindow mainWindow;
	/** The currently selected option. */
	private int tab = 0, selection = 0;
	/** The tabs of this Menu. */
	protected ArrayList<MenuTab> tabs;

	public AbstractMenuState(AbstractState backgroundState)
	{
		this.backgroundState = backgroundState;
		this.tabs = new ArrayList<MenuTab>();
		this.createOptions();
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

	/** @return This Window's dimensions. */
	protected Rectangle mainWindowDimensions(Graphics2D g)
	{
		int width = 0, height = 0;
		for (MenuTab tab : this.tabs)
		{
			width = Math.max(width, tab.width(g));
			height = Math.max(height, tab.height(g));
		}
		width += MenuWindow.MARGIN * 2 + MenuWindow.SELECTION_ARROW_SPACE;
		height += MenuWindow.MARGIN * 2;

		return new Rectangle(16, 32, width, height);
	}

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
		else if (key == Keys.KEY_MENU) this.onExit();

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

	@Override
	public void onKeyReleased(short key)
	{}

	/** Called when the player chooses the input Option. */
	protected abstract void onOptionSelected(MenuOption option);

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		if (this.mainWindow == null) this.mainWindow = new MenuWindow(this, this.mainWindowDimensions(g));

		if (this.backgroundState != null) this.backgroundState.render(g, width, height);
		this.mainWindow.render(g, width, height);
	}

	@Override
	public void update()
	{
		if (this.backgroundState != null) this.backgroundState.update();
		if (this.mainWindow != null) this.mainWindow.update();
	}

}
