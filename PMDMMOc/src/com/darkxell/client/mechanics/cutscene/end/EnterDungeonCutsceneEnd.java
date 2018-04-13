package com.darkxell.client.mechanics.cutscene.end;

import org.jdom2.Element;

import com.darkxell.client.mechanics.cutscene.Cutscene.CutsceneEnd;
import com.darkxell.common.util.XMLUtils;

public class EnterDungeonCutsceneEnd extends CutsceneEnd
{

	public final int dungeonID;

	public EnterDungeonCutsceneEnd(Element xml)
	{
		this.dungeonID = XMLUtils.getAttribute(xml, "id", 1);
	}

	@Override
	public void onCutsceneEnd()
	{
		// TODO Auto-generated method stub
	}

}
