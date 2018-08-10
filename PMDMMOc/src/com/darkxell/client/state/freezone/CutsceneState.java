package com.darkxell.client.state.freezone;

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
	public void update()
	{
		super.update();
		if (this.isMain()) this.cutscene.player.update();
		Persistance.currentmap.cutsceneEntityRenderers.update();
	}

}
