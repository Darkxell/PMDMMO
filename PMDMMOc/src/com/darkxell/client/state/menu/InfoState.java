package com.darkxell.client.state.menu;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.dungeon.DungeonState;
import com.darkxell.client.state.menu.components.TextWindow;
import com.darkxell.client.ui.Keys;
import com.darkxell.common.util.Message;

public class InfoState extends AbstractMenuState
{

	public final Message[] infos, titles;
	public final AbstractState parent;
	private int tab;
	private TextWindow window;

	public InfoState(DungeonState background, AbstractState parent, Message[] titles, Message[] infos)
	{
		super(background);
		this.parent = parent;
		this.titles = titles;
		this.infos = infos;
	}

	@Override
	protected void createOptions()
	{}

	@Override
	protected void onExit()
	{
		Launcher.stateManager.setState(this.parent, 0);
	}

	@Override
	public void onKeyPressed(short key)
	{
		super.onKeyPressed(key);
		if (key == Keys.KEY_ATTACK)
		; // Go to next tab

		if (key == Keys.KEY_LEFT)
		{
			if (this.tab == 0) this.tab = this.infos.length;
			--this.tab;
			this.window = null;
		}
		if (key == Keys.KEY_RIGHT)
		{
			++this.tab;
			if (this.tab == this.infos.length) this.tab = 0;
			this.window = null;
		}
	}

	@Override
	protected void onOptionSelected(MenuOption option)
	{}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		super.render(g, width, height);
		if (this.window == null)
		{
			this.window = new TextWindow(new Rectangle(16, 32, width - 16 * 2, height - 32 * 4), this.infos[this.tab], true);
			this.window.leftTab = this.tab > 0;
			this.window.rightTab = this.tab < this.infos.length - 1;
		}
		this.window.render(g, this.titles[this.tab], width, height);
	}
}
