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

	public CutsceneEntity(int id, int xpos, int ypos)
	{
		this.id = id;
		this.xPos = xpos;
		this.yPos = ypos;
	}

	public AbstractRenderer createRenderer()
	{
		return null;
	}

	@Override
	public String toString()
	{
		return this.id + " @ X=" + this.xPos + ", Y=" + this.yPos;
	}

	public Element toXML()
	{
		Element root = new Element("entity");
		XMLUtils.setAttribute(root, "cutsceneid", this.id, -1);
		XMLUtils.setAttribute(root, "ypos", (int) this.xPos, (int) 0);
		XMLUtils.setAttribute(root, "xpos", (int) this.yPos, (int) 0);
		return root;
	}

}
