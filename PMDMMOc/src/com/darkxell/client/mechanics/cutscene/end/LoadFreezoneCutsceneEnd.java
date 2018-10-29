package com.darkxell.client.mechanics.cutscene.end;

import org.jdom2.Element;

import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.Cutscene.CutsceneEnd;
import com.darkxell.client.state.StateManager;
import com.darkxell.common.util.XMLUtils;
import com.darkxell.common.zones.FreezoneInfo;

public class LoadFreezoneCutsceneEnd extends CutsceneEnd
{

	public final FreezoneInfo freezone;
	public final int xPos, yPos;

	public LoadFreezoneCutsceneEnd(Cutscene cutscene, Element xml)
	{
		super(cutscene, xml);
		this.freezone = FreezoneInfo.find(xml.getAttributeValue("id"));
		this.xPos = XMLUtils.getAttribute(xml, "xpos", -1);
		this.yPos = XMLUtils.getAttribute(xml, "ypos", -1);
	}

	public LoadFreezoneCutsceneEnd(Cutscene cutscene, String function)
	{
		super(cutscene, function);
		this.freezone = FreezoneInfo.BASE;
		this.xPos = -1;
		this.yPos = -1;
	}

	public LoadFreezoneCutsceneEnd(FreezoneInfo freezone, int x, int y, String function)
	{
		super(null, function);
		this.freezone = freezone;
		this.xPos = x;
		this.yPos = y;
	}

	@Override
	public void onCutsceneEnd()
	{
		super.onCutsceneEnd();
		StateManager.setExploreState(this.freezone, null, this.xPos, this.yPos);
	}

	@Override
	public Element toXML()
	{
		Element root = super.toXML().setAttribute("id", this.freezone.id);
		XMLUtils.setAttribute(root, "xpos", this.xPos, -1);
		XMLUtils.setAttribute(root, "ypos", this.yPos, -1);
		return root;
	}

	@Override
	protected String xmlName()
	{
		return "loadfreezone";
	}

}
