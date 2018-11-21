package com.darkxell.client.mechanics.cutscene;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.resources.Res;
import com.darkxell.client.state.TransitionState;
import com.darkxell.client.state.freezone.CutsceneState;
import com.darkxell.common.util.XMLUtils;

public class CutsceneManager
{

	public static Cutscene loadCutscene(String id)
	{
		return new Cutscene(id, XMLUtils.read(Res.get("/cutscenes/" + id + ".xml")));
	}

	public static void playCutscene(String id, boolean fading)
	{
		Cutscene c = loadCutscene(id);
		Persistance.cutsceneState = new CutsceneState(c);

		if (fading) Persistance.stateManager.setState(new TransitionState(Persistance.stateManager.getCurrentState(), Persistance.cutsceneState) {
			@Override
			public void onTransitionHalf()
			{
				super.onTransitionHalf();
				c.creation.create();
			}
		});
		else
		{
			c.creation.create();
			Persistance.stateManager.setState(Persistance.cutsceneState);
		}
	}

}
