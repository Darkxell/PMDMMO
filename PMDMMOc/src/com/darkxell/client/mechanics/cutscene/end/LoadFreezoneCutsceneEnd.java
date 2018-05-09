package com.darkxell.client.mechanics.cutscene.end;

import org.jdom2.Element;

import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.Cutscene.CutsceneEnd;
import com.darkxell.common.util.XMLUtils;

public class LoadFreezoneCutsceneEnd extends CutsceneEnd
{

	public final String freezoneID;
	public final int xPos, yPos;

	public LoadFreezoneCutsceneEnd(Cutscene cutscene, Element xml)
	{
		super(cutscene);
		this.freezoneID = XMLUtils.getAttribute(xml, "id", "base");
		this.xPos = XMLUtils.getAttribute(xml, "xpos", -1);
		this.yPos = XMLUtils.getAttribute(xml, "ypos", -1);
	}

	@Override
	public void onCutsceneEnd()
	{
		super.onCutsceneEnd();
		// TODO Auto-generated method stub
	}

}
