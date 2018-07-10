package com.darkxell.client.mechanics.cutscene.entity;

import org.jdom2.Element;

import com.darkxell.client.renderers.AbstractRenderer;
import com.darkxell.common.util.XMLUtils;

public class CutsceneEntity
{

	public final int id;
	public double xPos, yPos;

	public CutsceneEntity()
	{
		this.id = -1;
		this.xPos = this.yPos = 0;
	}

	public CutsceneEntity(Element xml)
	{
		this.id = XMLUtils.getAttribute(xml, "cutsceneid", -1);
		this.xPos = XMLUtils.getAttribute(xml, "xpos", 0);
		this.yPos = XMLUtils.getAttribute(xml, "ypos", 0);
	}

	public AbstractRenderer createRenderer()
	{
		return null;
	}

}
