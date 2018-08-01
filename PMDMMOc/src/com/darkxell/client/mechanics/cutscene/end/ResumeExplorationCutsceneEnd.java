package com.darkxell.client.mechanics.cutscene.end;

import org.jdom2.Element;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.Cutscene.CutsceneEnd;
import com.darkxell.client.state.dungeon.NextFloorState;

public class ResumeExplorationCutsceneEnd extends CutsceneEnd
{

	public ResumeExplorationCutsceneEnd()
	{
		this(null);
	}

	public ResumeExplorationCutsceneEnd(Cutscene cutscene)
	{
		super(cutscene);
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
	public Element toXML()
	{
		return new Element("resumeexploration");
	}

}
