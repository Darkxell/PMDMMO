package com.darkxell.client.state.freezone;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.freezones.entities.FreezoneCamera;

public class CutsceneState extends AbstractFreezoneState
{

	public final Cutscene cutscene;

	public CutsceneState(Cutscene cutscene)
	{
		this.cutscene = cutscene;
	}

	@Override
	public void onStart()
	{
		super.onStart();
		Persistance.freezoneCamera = new FreezoneCamera(null);
		this.cutscene.creation.create();
	}

}
