package com.darkxell.client.mechanics.cutscene.event;

import org.jdom2.Element;

import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.common.util.XMLUtils;

public class DelayCutsceneEvent extends CutsceneEvent
{
	public final int duration;

	public DelayCutsceneEvent(Element xml, Cutscene cutscene)
	{
		super(xml, cutscene);
		this.duration = XMLUtils.getAttribute(xml, "ticks", 0);
	}

}
