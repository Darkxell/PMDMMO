package com.darkxell.client.mechanics.cutscene.event;

import org.jdom2.Element;

import com.darkxell.client.mechanics.cutscene.CutsceneContext;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.common.util.XMLUtils;

public class DelayCutsceneEvent extends CutsceneEvent
{
	public final int duration;
	private int tick = 0;

	public DelayCutsceneEvent(Element xml, CutsceneContext context)
	{
		super(xml, CutsceneEventType.delay, context);
		this.duration = XMLUtils.getAttribute(xml, "ticks", 0);
	}

	public DelayCutsceneEvent(int id, int duration)
	{
		super(id, CutsceneEventType.delay);
		this.duration = duration;
	}

	@Override
	public boolean isOver()
	{
		return this.tick == this.duration;
	}

	@Override
	public String toString()
	{
		return this.displayID() + "Wait for " + this.duration + " ticks";
	}

	@Override
	public Element toXML()
	{
		return super.toXML().setAttribute("ticks", String.valueOf(this.duration));
	}

	@Override
	public void update()
	{
		super.update();
		if (!this.isOver()) ++this.tick;
	}

}
