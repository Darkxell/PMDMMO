package com.darkxell.client.mechanics.cutscene.end;

import org.jdom2.Element;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.Cutscene.CutsceneEnd;
import com.darkxell.client.state.StateManager;
import com.darkxell.common.util.XMLUtils;

public class EnterDungeonCutsceneEnd extends CutsceneEnd
{

	public final int dungeonID;

	public EnterDungeonCutsceneEnd(Cutscene cutscene, Element xml)
	{
		super(cutscene);
		this.dungeonID = XMLUtils.getAttribute(xml, "id", 1);
	}

	@Override
	public void onCutsceneEnd()
	{
		super.onCutsceneEnd();
		StateManager.setDungeonState(Persistance.cutsceneState, this.dungeonID);
	}

}
