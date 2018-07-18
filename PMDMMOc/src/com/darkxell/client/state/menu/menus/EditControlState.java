package com.darkxell.client.state.menu.menus;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.launchable.UpdaterAndRenderer;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.renderers.layers.AbstractGraphiclayer;
import com.darkxell.client.resources.Palette;
import com.darkxell.client.resources.music.SoundManager;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.mainstates.PrincipalMainState;
import com.darkxell.client.state.menu.components.MenuWindow;
import com.darkxell.client.state.menu.menus.ControlsMenuState.ControlMenuOption;
import com.darkxell.client.ui.Keys.Key;
import com.darkxell.common.util.language.Message;

public class EditControlState extends AbstractState
{

	public static final int SECONDS = 5;
	public static final int TICKMAX = UpdaterAndRenderer.targetUPS * SECONDS;

	private AbstractGraphiclayer background;
	public final Key key;
	private ControlMenuOption option;
	private ControlsMenuState parent;
	private Rectangle progressBar;
	private int tick = 0;
	private MenuWindow window;

	public EditControlState(AbstractGraphiclayer background, ControlsMenuState parent, ControlMenuOption option)
	{
		this.background = background;
		this.parent = parent;
		this.option = option;
		this.key = this.option.key;

		this.window = new MenuWindow(new Rectangle(16, 32, PrincipalMainState.displayWidth - 16 * 2, PrincipalMainState.displayHeight - 32 * 3 / 2));
		Rectangle r = this.window.inside();
		this.progressBar = new Rectangle(r.x + r.width / 8, r.y + r.height / 2 + TextRenderer.height() + 5, r.width * 3 / 4, 15);
	}

	@Override
	public void onKeyPressed(Key key)
	{
		this.parent.onKeyValueSelected(this.option, key.keyValue());
	}

	@Override
	public void onKeyReleased(Key key)
	{}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		if (this.background != null) this.background.render(g, width, height);
		this.window.render(g, null, width, height);

		int secondsLeft = (TICKMAX - this.tick) / UpdaterAndRenderer.targetUPS;
		Message main = new Message("key.edit.main").addReplacement("<key>", this.key.getName());
		Message left = new Message("key.edit.timer").addReplacement("<seconds>", String.valueOf(secondsLeft));

		int y = (int) (this.window.inside().y + this.window.inside().getHeight() / 2);
		TextRenderer.render(g, main, width / 2 - TextRenderer.width(main) / 2, y - TextRenderer.height() - 2);
		TextRenderer.render(g, left, width / 2 - TextRenderer.width(left) / 2, y + 2);

		int w = this.progressBar.width * this.tick / TICKMAX;
		g.setColor(Palette.TRANSPARENT_GRAY);
		g.fillRect(this.progressBar.x, this.progressBar.y, w, this.progressBar.height);

		g.setColor(Palette.TEAM);
		g.draw(this.progressBar);
	}

	@Override
	public void update()
	{
		++this.tick;
		if (this.tick >= TICKMAX)
		{
			SoundManager.playSound("ui-back");
			Persistance.stateManager.setState(this.parent);
		}
	}

}
