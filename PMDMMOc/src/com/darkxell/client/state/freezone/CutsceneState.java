package com.darkxell.client.state.freezone;

import java.awt.Color;
import java.awt.Graphics2D;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.cutscene.Cutscene;

public class CutsceneState extends AbstractFreezoneState
{

	public final Cutscene cutscene;

	public CutsceneState(Cutscene cutscene)
	{
		this.cutscene = cutscene;
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		super.render(g, width, height);

		if (this.cutscene.player.mapAlpha != 1)
		{
			g.setColor(new Color(0, 0, 0, (int) ((1 - this.cutscene.player.mapAlpha) * 255)));
			g.fillRect(0, 0, width, height);
		}
	}

	@Override
	public void update()
	{
		super.update();
		if (this.isMain()) this.cutscene.player.update();
		Persistance.currentmap.cutsceneEntityRenderers.update();
	}

}
