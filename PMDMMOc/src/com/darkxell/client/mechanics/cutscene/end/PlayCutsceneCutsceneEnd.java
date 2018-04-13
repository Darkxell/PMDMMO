package com.darkxell.client.mechanics.cutscene.end;

import org.jdom2.Element;

import com.darkxell.client.mechanics.cutscene.Cutscene.CutsceneEnd;
import com.darkxell.client.mechanics.cutscene.CutsceneManager;
import com.darkxell.common.util.XMLUtils;

public class PlayCutsceneCutsceneEnd extends CutsceneEnd
{

	public final String cutsceneID;

	public PlayCutsceneCutsceneEnd(Element xml)
	{
		this.cutsceneID = XMLUtils.getAttribute(xml, "id", null);
	}

	@Override
	public void onCutsceneEnd()
	{
		if (this.cutsceneID != null) CutsceneManager.loadCutscene(this.cutsceneID); // .doSomethingWith()
	}

}
