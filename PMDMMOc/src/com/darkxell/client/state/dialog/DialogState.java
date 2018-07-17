package com.darkxell.client.state.dialog;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.renderers.layers.AbstractGraphiclayer;
import com.darkxell.client.resources.images.others.Hud;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.ui.Keys.Key;
import com.darkxell.common.util.language.Message;

public class DialogState extends AbstractState
{

	public static interface DialogEndListener
	{
		/** Called when the input dialog ends. */
		public void onDialogEnd(DialogState dialog);
	}

	/** The State to draw in this State's background. */
	public final AbstractGraphiclayer backgroundState;
	/** The current screen. */
	protected int currentScreen;
	private Rectangle dialogBox;
	/** The listener called when this Dialog ends. If null, the Background State is used instead. */
	protected final DialogEndListener listener;
	/** The screens to show. */
	protected final DialogScreen[] screens;

	public DialogState(AbstractGraphiclayer backgroundState, DialogEndListener listener, DialogScreen... screens)
	{
		this.listener = listener;
		this.screens = screens;
		this.backgroundState = backgroundState;
		this.currentScreen = 0;

		for (DialogScreen screen : this.screens)
			screen.parentState = this;
	}

	public DialogState(AbstractGraphiclayer backgroundState, DialogScreen... elements)
	{
		this(backgroundState, null, elements);
	}

	public Message currentMessage()
	{
		return this.currentScreen().message;
	}

	public DialogScreen currentScreen()
	{
		return this.screens[this.currentScreen];
	}

	public Rectangle dialogBox()
	{
		return this.dialogBox;
	}

	public Rectangle dialogBoxInside()
	{
		int marginx = this.dialogBox.width / 20, marginy = this.dialogBox.height / 5;
		return new Rectangle(this.dialogBox.x + marginx, this.dialogBox.y + marginy, this.dialogBox.width - marginx * 2, this.dialogBox.height - marginy * 2);
	}

	/** @return The first screen with the input ID. */
	public DialogScreen getScreen(int id)
	{
		for (DialogScreen screen : this.screens)
			if (screen.id == id) return screen;
		return null;
	}

	/** @return Alls screens with the input ID. */
	public ArrayList<DialogScreen> getScreens(int id)
	{
		ArrayList<DialogScreen> screens = new ArrayList<>();
		for (DialogScreen screen : this.screens)
			if (screen.id == id) screens.add(screen);
		return screens;
	}

	/** Skips to the next message. */
	public void nextMessage()
	{
		if (this.currentScreen().requestNextMessage())
		{
			if (this.currentScreen == this.screens.length - 1)
			{
				if (this.listener == null && this.backgroundState != null && this.backgroundState instanceof AbstractState)
					Persistance.stateManager.setState((AbstractState) this.backgroundState);
				else if (this.listener != null) this.listener.onDialogEnd(this);
			} else
			{
				++this.currentScreen;
				this.currentScreen().onStart();
			}
		}
	}

	public DialogScreen nextScreen()
	{
		if (this.currentScreen >= this.screens.length - 1) return null;
		return this.screens[this.currentScreen + 1];
	}

	@Override
	public void onKeyPressed(Key key)
	{
		this.currentScreen().onKeyPressed(key);
	}

	@Override
	public void onKeyReleased(Key key)
	{}

	@Override
	public void onMouseClick(int x, int y)
	{
		super.onMouseClick(x, y);
		this.currentScreen().onMouseClick(x, y);
	}

	@Override
	public void onStart()
	{
		super.onStart();
		this.currentScreen().onStart();
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		if (this.backgroundState != null && this.currentScreen().shouldRenderBackground()) this.backgroundState.render(g, width, height);

		if (this.dialogBox == null)
		{
			int temp_width = width - 40;
			int temp_height = temp_width * Hud.textwindow.getHeight() / Hud.textwindow.getWidth();
			this.dialogBox = new Rectangle(20, height - temp_height - 5, temp_width, temp_height);
		}

		this.currentScreen().render(g, width, height);
	}

	public DialogState setOpaque(boolean opaque)
	{
		for (DialogScreen screen : this.screens)
			screen.isOpaque = opaque;
		return this;
	}

	@Override
	public void update()
	{
		this.currentScreen().update();
		if (this.backgroundState != null) this.backgroundState.update();
	}
}
