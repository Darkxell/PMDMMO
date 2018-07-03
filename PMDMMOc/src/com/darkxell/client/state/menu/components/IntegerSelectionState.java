package com.darkxell.client.state.menu.components;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.renderers.layers.AbstractGraphiclayer;
import com.darkxell.client.resources.images.MenuHudSpriteset;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.dialog.DialogState;
import com.darkxell.client.ui.Keys;
import com.darkxell.common.util.language.Message;

public class IntegerSelectionState extends AbstractState
{

	public static interface IntegerSelectionListener
	{
		/** Called when the user confirms the selection. -1 if the user cancelled. */
		public void onIntegerSelected(long selection);
	}

	public final AbstractGraphiclayer background;
	private long current;
	private int currentLogLocation, textx;
	private Message currentM;
	public final IntegerSelectionListener listener;
	private int logSelection, maxLog;
	public final long min, max;
	public final DialogState parent;
	private MenuWindow window;

	public IntegerSelectionState(AbstractGraphiclayer background, DialogState parent, IntegerSelectionListener listener, long min, long max, long start)
	{
		this.background = background;
		this.parent = parent;
		this.listener = listener;
		this.min = min;
		this.max = max;
		this.current = start;
		this.logSelection = (int) Math.floor(Math.log10(start));
		this.maxLog = (int) Math.floor(Math.log10(this.max));
		this.updateCurrentMessage();
	}

	@Override
	public void onKeyPressed(short key)
	{
		long newValue = this.current;
		int newLog = this.logSelection;
		switch (key)
		{
			case Keys.KEY_ATTACK:
				this.listener.onIntegerSelected(this.current);
				return;
			case Keys.KEY_RUN:
				this.listener.onIntegerSelected(-1);
				return;

			case Keys.KEY_UP:
				newValue += Math.pow(10, this.logSelection);
				break;
			case Keys.KEY_DOWN:
				newValue -= Math.pow(10, this.logSelection);
				break;

			case Keys.KEY_LEFT:
				++newLog;
				break;
			case Keys.KEY_RIGHT:
				--newLog;
				break;

			default:
				break;
		}

		if (newValue != this.current)
		{
			this.current = newValue;
			if (this.current < 0) this.current = 0;
			else if (this.current > this.max) this.current = this.max;
			this.updateCurrentMessage();
		}
		if (newLog != this.logSelection && newLog >= 0 && newLog <= this.maxLog)
		{
			this.logSelection = newLog;
			this.updateLogLocation();
		}
	}

	@Override
	public void onKeyReleased(short key)
	{}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		if (this.background != null) this.background.render(g, width, height);

		if (this.window == null)
		{
			String longestnumber = "";
			for (int i = 0; i < this.maxLog; ++i)
				longestnumber += "9";
			Message longest = new Message(longestnumber, false);

			Rectangle dialog = this.parent.dialogBox();
			int w = TextRenderer.width(longest) + OptionSelectionWindow.MARGIN_X * 2,
					h = TextRenderer.height() + OptionSelectionWindow.MARGIN_Y * 3 + MenuHudSpriteset.NEXT_WINDOW_ARROW.getHeight();
			this.window = new MenuWindow(new Rectangle((int) (dialog.getMaxX() - w), dialog.y - h - 5, w, h));
			this.window.isOpaque = this.parent.currentScreen().isOpaque;
			this.updateLogLocation();

			this.textx = (int) (this.window.dimensions.x + this.window.dimensions.getWidth() / 2 - TextRenderer.width(longest) / 2);
		}

		this.window.render(g, null, width, height);

		Rectangle inside = this.window.inside();
		int texty = (int) (inside.y + inside.getHeight() / 2 - TextRenderer.height() / 2);
		TextRenderer.render(g, this.currentM, this.textx, texty);

		int arrowx = this.textx + this.currentLogLocation - MenuHudSpriteset.NEXT_WINDOW_ARROW.getWidth();
		int uparrowy = texty - OptionSelectionWindow.MARGIN_Y / 3;
		int downarrowy = texty + TextRenderer.height() + OptionSelectionWindow.MARGIN_Y / 3;
		g.drawImage(MenuHudSpriteset.NEXT_WINDOW_ARROW, arrowx, uparrowy, MenuHudSpriteset.NEXT_WINDOW_ARROW.getWidth(),
				-MenuHudSpriteset.NEXT_WINDOW_ARROW.getHeight(), null);
		g.drawImage(MenuHudSpriteset.NEXT_WINDOW_ARROW, arrowx, downarrowy, null);
	}

	@Override
	public void update()
	{
		if (this.background != null) this.background.update();
	}

	private void updateCurrentMessage()
	{
		String base = String.valueOf(this.current);
		while (base.length() < this.maxLog + 1)
			base = "0" + base;
		this.currentM = new Message(base, false);
	}

	private void updateLogLocation()
	{
		String loglength = "";
		for (int i = this.maxLog; i >= this.logSelection; --i)
			loglength += "9";
		this.currentLogLocation = TextRenderer.width(new Message(loglength, false));
		this.currentLogLocation += TextRenderer.width(new Message("9", false)) / 2;
	}

}
