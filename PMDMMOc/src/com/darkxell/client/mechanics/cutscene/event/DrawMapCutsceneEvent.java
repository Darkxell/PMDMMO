package com.darkxell.client.mechanics.cutscene.event;

import org.jdom2.Element;

import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.state.dialog.NarratorDialogScreen;
import com.darkxell.common.util.XMLUtils;

public class DrawMapCutsceneEvent extends CutsceneEvent
{

	public boolean shouldDraw;
	private int tick;

	public DrawMapCutsceneEvent(Element xml, Cutscene cutscene)
	{
		super(xml, CutsceneEventType.drawmap, cutscene);
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
		if ((this.shouldDraw && this.cutscene.player.mapAlpha == 1) || (!this.shouldDraw && this.cutscene.player.mapAlpha == 0))
			this.tick = NarratorDialogScreen.FADETIME;
	}

	@Override
	public String toString()
	{
		if (this.shouldDraw) return "Start drawing the map";
		return "Stop drawing the map";
	}

	@Override
	public void update()
	{
		super.update();
		++this.tick;
		double alpha = this.tick * 1. / NarratorDialogScreen.FADETIME;
		if (this.shouldDraw) alpha = 1 - alpha;
		this.cutscene.player.mapAlpha = alpha;
	}

}
