package com.darkxell.client.state.menu;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Stack;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.renderers.layers.AbstractGraphiclayer;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.menu.components.TextWindow;
import com.darkxell.client.ui.Keys.Key;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.language.Keywords;
import com.darkxell.common.util.language.Message;

public class InfoState extends AbstractMenuState
{

	private static ArrayList<String> getKeywords(Message[] infos)
	{
		ArrayList<String> keywords = new ArrayList<String>();
		Stack<Message> toProcess = new Stack<Message>();

		for (Message info : infos)
			toProcess.add(info);

		while (!toProcess.isEmpty())
		{
			Message m = toProcess.pop();
			m.findKeywords();
			String[] newKeywords = m.getKeywords();
			for (String keyword : newKeywords)
				if (!keywords.contains(keyword))
				{
					keywords.add(keyword);
					toProcess.add(new Message(Keywords.getKeyword(keyword)));
				}
		}

		return keywords;
	}

	public final Message[] infos, titles;
	private boolean isOpaque = false;
	public final AbstractState parent;
	private int tab;
	protected TextWindow window;

	public InfoState(AbstractGraphiclayer background, AbstractState parent, Message[] titles, Message[] infos)
	{
		super(background);
		this.parent = parent;
		if (titles.length != infos.length) Logger.e("InfoState(): titles and infos have different sizes!");

		ArrayList<Message> t = new ArrayList<Message>();
		ArrayList<Message> i = new ArrayList<Message>();

		for (int j = 0; j < infos.length; ++j)
		{
			t.add(titles[j]);
			i.add(infos[j]);
		}

		ArrayList<String> keywords = getKeywords(infos);
		for (String keyword : keywords)
		{
			t.add(new Message(keyword, false));
			i.add(new Message(Keywords.getKeyword(keyword)).findKeywords());
		}

		this.titles = t.toArray(new Message[t.size()]);
		this.infos = i.toArray(new Message[i.size()]);
	}

	@Override
	protected void createOptions()
	{}

	@Override
	protected void onExit()
	{
		Persistance.stateManager.setState(this.parent);
	}

	@Override
	public void onKeyPressed(Key key)
	{
		super.onKeyPressed(key);

		if (key == Key.LEFT)
		{
			if (this.tab == 0) this.tab = this.infos.length;
			--this.tab;
			this.window = null;
		}
		if (key == Key.RIGHT)
		{
			++this.tab;
			if (this.tab == this.infos.length) this.tab = 0;
			this.window = null;
		}
	}

	@Override
	public void onMouseClick(int x, int y)
	{
		super.onMouseClick(x, y);
		if (this.window.inside().contains(x, y))
		{
			++this.tab;
			if (this.tab == this.infos.length) this.tab = 0;
			this.window = null;
		} else this.onExit();
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
			this.window.isOpaque = this.isOpaque;
		}
		this.window.render(g, this.titles[this.tab], width, height);
	}

	public InfoState setOpaque(boolean isOpaque)
	{
		this.isOpaque = isOpaque;
		return this;
	}
}
