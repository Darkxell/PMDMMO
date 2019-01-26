package com.darkxell.client.state.menu;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.renderers.layers.AbstractGraphicLayer;
import com.darkxell.client.resources.images.hud.MenuStateHudSpriteset;
import com.darkxell.client.resources.music.SoundManager;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.menu.components.OptionSelectionWindow;
import com.darkxell.client.ui.Keys.Key;
import com.darkxell.common.util.language.Message;

public abstract class AbstractMenuState extends AbstractState
{

	public static class MenuOption
	{
		/** True if this Option can be selected. */
		public boolean isEnabled;
		/** The name of this Option. */
		public Message name;

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

		public int height()
		{
			return this.options.size() * (TextRenderer.height() + TextRenderer.lineSpacing());
		}

		public MenuOption[] options()
		{
			return this.options.toArray(new MenuOption[this.options.size()]);
		}

		public int width()
		{
			int width = 0;
			for (MenuOption option : this.options)
				width = Math.max(width, TextRenderer.width(option.name));
			width = Math.max(width, TextRenderer.width(this.name) + MenuStateHudSpriteset.cornerSize.width * 2);
			return width;
		}

	}

	/** The state to draw behind this menu State. */
	public final AbstractGraphicLayer background;
	protected int selection = 0;
	/** The currently selected option. */
	protected int tab = 0;
	/** The tabs of this Menu. */
	protected ArrayList<MenuTab> tabs;

	public AbstractMenuState(AbstractGraphicLayer background)
	{
		this.background = background;
		this.tabs = new ArrayList<MenuTab>();
	}

	/** Creates this Menu's options. */
	protected abstract void createOptions();

	public MenuOption currentOption()
	{
		if (this.tabs.size() == 0) return null;
		return this.currentTab().options.get(this.selection);
	}

	public MenuTab currentTab()
	{
		if (this.tabs.size() == 0) return null;
		return this.tabs.get(this.tab);
	}

	/** @return This Window's dimensions. */
	protected Rectangle mainWindowDimensions()
	{
		int width = this.currentTab().width(), height = this.currentTab().height();
		width += OptionSelectionWindow.MARGIN_X * 2;
		height += OptionSelectionWindow.MARGIN_Y * 2;

		return new Rectangle(16, 32, width, height);
	}

	/* protected Rectangle mainWindowDimensions() { int width = 0, height = 0; for (MenuTab tab : this.tabs) { width = Math.max(width, tab.width()); height = Math.max(height, tab.height()); } width += OptionSelectionWindow.MARGIN_X * 2; height += OptionSelectionWindow.MARGIN_Y * 2;
	 * 
	 * return new Rectangle(16, 32, width, height); } */

	/** Called when the player presses the "back" button. */
	protected abstract void onExit();

	@Override
	public void onKeyPressed(Key key)
	{
		if (this.tabs.size() != 0)
		{
			if ((key == Key.LEFT || key == Key.PAGE_LEFT) && this.tab > 0) --this.tab;
			else if ((key == Key.RIGHT || key == Key.PAGE_RIGHT) && this.tab < this.tabs.size() - 1) ++this.tab;
			else if (key == Key.UP) --this.selection;
			else if (key == Key.DOWN) ++this.selection;
			else if (key == Key.ATTACK)
			{
				this.onOptionSelected(this.currentOption());
				SoundManager.playSound("ui-select");
			}

			if (key == Key.LEFT || key == Key.RIGHT || key == Key.PAGE_RIGHT || key == Key.PAGE_RIGHT)
			{
				if (this.selection >= this.currentTab().options.size()) this.selection = this.currentTab().options.size() - 1;
				this.onTabChanged(this.currentTab());
				SoundManager.playSound("ui-move");
			} else if (key == Key.UP || key == Key.DOWN)
			{
				if (this.selection == -1) this.selection = this.currentTab().options.size() - 1;
				else if (this.selection == this.currentTab().options.size()) this.selection = 0;
				this.onOptionChanged(this.currentOption());
				SoundManager.playSound("ui-move");
			}
		}
		if (key == Key.MENU || key == Key.RUN)
		{
			SoundManager.playSound("ui-back");
			this.onExit();
		}
	}

	@Override
	public void onKeyReleased(Key key)
	{}

	protected void onOptionChanged(MenuOption option)
	{}

	/** Called when the player chooses the input Option. */
	protected abstract void onOptionSelected(MenuOption option);

	protected void onTabChanged(MenuTab tab)
	{}

	public int optionIndex()
	{
		return this.selection;
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		if (this.background != null) this.background.render(g, width, height);
	}

	public int tabIndex()
	{
		return this.tab;
	}

	public MenuTab[] tabs()
	{
		return this.tabs.toArray(new MenuTab[this.tabs.size()]);
	}

	@Override
	public void update()
	{
		if (this.background != null) this.background.update();
	}

}
