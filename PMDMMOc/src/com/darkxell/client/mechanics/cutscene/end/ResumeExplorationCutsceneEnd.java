package com.darkxell.client.mechanics.cutscene.end;

import org.jdom2.Element;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.Cutscene.CutsceneEnd;
import com.darkxell.client.state.dungeon.NextFloorState;

public class ResumeExplorationCutsceneEnd extends CutsceneEnd
{

	public ResumeExplorationCutsceneEnd(Cutscene cutscene, Element xml)
	{
		super(cutscene, xml);
	}

	public ResumeExplorationCutsceneEnd(String function, boolean fadesOut)
	{
		super(null, function, fadesOut);
	}

	@Override
	public void onCutsceneEnd()
	{
		super.onCutsceneEnd();
		NextFloorState.resumeExploration();
		Persistance.stateManager.setState(Persistance.dungeonState);
		Persistance.eventProcessor().processPending();
	}

	@Override
	protected String xmlName()
	{
		return "resumeexploration";
	}

}
