package com.darkxell.client.mechanics.cutscene.event;

import org.jdom2.Element;

import com.darkxell.client.mechanics.cutscene.CutsceneContext;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.state.dialog.NarratorDialogScreen;
import com.darkxell.common.util.xml.XMLUtils;

public class DrawMapCutsceneEvent extends CutsceneEvent
{

	public boolean shouldDraw;
	private int tick;

	public DrawMapCutsceneEvent(Element xml, CutsceneContext context)
	{
		super(xml, CutsceneEventType.drawmap, context);
		this.shouldDraw = XMLUtils.getAttribute(xml, "draw", true);
	}

	public DrawMapCutsceneEvent(int id, boolean shouldDraw)
	{
		super(id, CutsceneEventType.drawmap);
		this.shouldDraw = shouldDraw;
	}

	@Override
	public boolean isOver()
	{
		return this.tick >= NarratorDialogScreen.FADETIME;
	}

	@Override
	public void onStart()
	{
		super.onStart();
		this.tick = 0;
		if ((this.shouldDraw && this.context.parent().player.mapAlpha == 1) || (!this.shouldDraw && this.context.parent().player.mapAlpha == 0))
			this.tick = NarratorDialogScreen.FADETIME;
	}

	@Override
	public String toString()
	{
		if (this.shouldDraw) return "Start drawing the map";
		return "Stop drawing the map";
	}

	@Override
	public Element toXML()
	{
		Element e = super.toXML();
		XMLUtils.setAttribute(e, "draw", this.shouldDraw, true);
		return e;
	}

	@Override
	public void update()
	{
		super.update();
		++this.tick;
		double alpha = this.tick * 1. / NarratorDialogScreen.FADETIME;
		if (!this.shouldDraw) alpha = (NarratorDialogScreen.FADETIME - this.tick) * 1. / NarratorDialogScreen.FADETIME;
		this.context.parent().player.mapAlpha = alpha;
	}

}
