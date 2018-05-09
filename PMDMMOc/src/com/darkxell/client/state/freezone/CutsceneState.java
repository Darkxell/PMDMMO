package com.darkxell.client.state.freezone;

import com.darkxell.client.mechanics.cutscene.Cutscene;

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
		this.cutscene.creation.create();
	}
	
	@Override
	public void update()
	{
		super.update();
		this.cutscene.player.update();
	}

}
