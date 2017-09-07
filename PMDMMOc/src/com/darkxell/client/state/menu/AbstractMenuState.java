package com.darkxell.client.state.menu;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.resources.images.MenuHudSpriteset;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.menu.components.MenuWindow;
import com.darkxell.client.state.menu.components.OptionSelectionWindow;
import com.darkxell.client.ui.Keys;
import com.darkxell.common.util.Message;

public abstract class AbstractMenuState extends AbstractState
{

	public static class MenuOption
	{
		/** True if this Option can be selected. */
		public boolean isEnabled;
		/** The name of this Option. */
		public final Message name;

		public MenuOption(Message name)
		{
			this(name, true);
		}

		public MenuOption(Message name, boolean isEnabled)
		{
			this.name = name;
			this.isEnabled = isEnabled;
		}

		public MenuOption(String nameID)
		{
			this(new Message(nameID));
		}
	}

	public static class MenuTab
	{
		public static final int OPTION_SPACE = 4;

		public final Message name;
		private ArrayList<MenuOption> options;

		public MenuTab()
		{
			this((Message) null);
		}

		public MenuTab(Message name)
		{
			this.name = name;
			this.options = new ArrayList<AbstractMenuState.MenuOption>();
		}

		public MenuTab(String nameID)
		{
			this(new Message(nameID));
		}

		public MenuTab addOption(MenuOption option)
		{
			this.options.add(option);
			return this;
		}

		public int height(Graphics2D g)
		{
			return this.options.size() * (TextRenderer.CHAR_HEIGHT + OPTION_SPACE);
		}

		public MenuOption[] options()
		{
			return this.options.toArray(new MenuOption[this.options.size()]);
		}

		public int width(Graphics2D g)
		{
			int width = 0;
			for (MenuOption option : this.options)
				width = Math.max(width, TextRenderer.instance.width(option.name.toString()));
			width = Math.max(width, TextRenderer.instance.width(this.name) + MenuHudSpriteset.instance.cornerSize.width * 2);
			return width;
		}

	}

	/** The state to draw behind this menu State. */
	public final AbstractState backgroundState;
	/** The main window to display the options in. */
	private OptionSelectionWindow mainWindow;
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

	public MenuOption currentOption()
	{
		return this.currentTab().options.get(this.selection);
	}

	public MenuTab currentTab()
	{
		return this.tabs.get(this.tab);
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

	/** Called when the player presses the "back" button. */
	protected abstract void onExit();

	@Override
	public void onKeyPressed(short key)
	{
		if (key == Keys.KEY_LEFT && this.tab > 0) --this.tab;
		else if (key == Keys.KEY_RIGHT && this.tab < this.tabs.size() - 1) ++this.tab;
		else if (key == Keys.KEY_UP) --this.selection;
		else if (key == Keys.KEY_DOWN) ++this.selection;
		else if (key == Keys.KEY_ATTACK) this.onOptionSelected(this.currentOption());
		else if (key == Keys.KEY_MENU || key == Keys.KEY_RUN) this.onExit();

		if (key == Keys.KEY_LEFT || key == Keys.KEY_RIGHT)
		{
			if (this.selection >= this.currentTab().options.size()) this.selection = this.currentTab().options.size() - 1;
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
		if (this.mainWindow == null) this.mainWindow = new OptionSelectionWindow(this, this.mainWindowDimensions(g));

		if (this.backgroundState != null) this.backgroundState.render(g, width, height);
		this.mainWindow.render(g, this.currentTab().name, width, height);
	}

	public MenuTab[] tabs()
	{
		return this.tabs.toArray(new MenuTab[this.tabs.size()]);
	}

	@Override
	public void update()
	{
		if (this.backgroundState != null) this.backgroundState.update();
		if (this.mainWindow != null) this.mainWindow.update();
	}

}
