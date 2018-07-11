package com.darkxell.client.mechanics.cutscene.end;

import org.jdom2.Element;

import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.Cutscene.CutsceneEnd;
import com.darkxell.client.state.StateManager;
import com.darkxell.common.util.XMLUtils;

public class LoadFreezoneCutsceneEnd extends CutsceneEnd
{

	public final String freezoneID;
	public final int xPos, yPos;

	public LoadFreezoneCutsceneEnd(Cutscene cutscene)
	{
		super(cutscene);
		this.freezoneID = "Base";
		this.xPos = -1;
		this.yPos = -1;
	}

	public LoadFreezoneCutsceneEnd(Cutscene cutscene, Element xml)
	{
		super(cutscene);
		this.freezoneID = XMLUtils.getAttribute(xml, "id", "Base");
		this.xPos = XMLUtils.getAttribute(xml, "xpos", -1);
		this.yPos = XMLUtils.getAttribute(xml, "ypos", -1);
	}

	public LoadFreezoneCutsceneEnd(String freezoneID, int x, int y)
	{
		super(null);
		this.freezoneID = freezoneID;
		this.xPos = x;
		this.yPos = y;
	}

	@Override
	public void onCutsceneEnd()
	{
		super.onCutsceneEnd();
		StateManager.setExploreState(this.freezoneID, this.xPos, this.yPos);
	}

	@Override
	public Element toXML()
	{
		Element root = new Element("loadfreezone").setAttribute("id", this.freezoneID);
		XMLUtils.setAttribute(root, "xpos", this.xPos, -1);
		XMLUtils.setAttribute(root, "ypos", this.yPos, -1);
		return root;
	}

}
