package com.darkxell.client.state;

import java.awt.Graphics2D;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.launchable.StoryPositionSetup;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.renderers.layers.BackgroundSeaLayer;
import com.darkxell.client.resources.images.Sprites;
import com.darkxell.client.resources.music.SoundsHolder;
import com.darkxell.client.ui.Keys.Key;

public class OpenningState extends AbstractState
{

	private BackgroundSeaLayer background = new BackgroundSeaLayer(true);
	private boolean ismusicset = false;
	private int textblink = 0;

	@Override
	public void onKeyPressed(Key key)
	{
		if (key == Key.ATTACK) StoryPositionSetup.trigger(Persistance.player.getData().storyposition, true);
	}

	@Override
	public void onKeyReleased(Key key)
	{}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		background.render(g, width, height);
		g.drawImage(Sprites.Res_Hud.gametitle.image(), width / 2 - Sprites.Res_Hud.gametitle.image().getWidth() / 2,
				height / 2 - Sprites.Res_Hud.gametitle.image().getHeight() / 2, null);
		if (textblink >= 50)
		{
			String press = "Press attack (default D) to continue.";
			TextRenderer.render(g, press, width / 2 - TextRenderer.width(press) / 2, height / 4 * 3);
		}
	}

	@Override
	public void update()
	{
		if (!ismusicset)
		{
			ismusicset = true;
			Persistance.soundmanager.setBackgroundMusic(SoundsHolder.getSong("intro.mp3"));
		}

		background.update();
		++textblink;
		if (textblink >= 100) textblink = 0;
	}

}
