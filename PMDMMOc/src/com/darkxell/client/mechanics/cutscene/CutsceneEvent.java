package com.darkxell.client.mechanics.cutscene;

import org.jdom2.Element;

import com.darkxell.common.util.XMLUtils;

public abstract class CutsceneEvent
{

	public static CutsceneEvent create(Element xml)
	{
		return new CutsceneEvent(xml) {};
	}

	private int duration = 1;
	int id;
	private int tick = 0;

	public CutsceneEvent(Element xml)
	{
		this.id = XMLUtils.getAttribute(xml, "eventid", -1);
	}

	public int duration()
	{
		return this.duration;
	}

	public boolean isOver()
	{
		return this.tick >= this.duration;
	}

	public void onFinish()
	{}

	public void onStart()
	{}

	public void update()
	{
		++this.tick;
	}

}
