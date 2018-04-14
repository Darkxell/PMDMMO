package com.darkxell.client.mechanics.cutscene.event;

import org.jdom2.Element;

import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.common.util.XMLUtils;

public class MoveCutsceneEvent extends CutsceneEvent
{
	public static final double UNSPECIFIED = Double.MAX_VALUE;

	public final double speed;
	public final int target;
	public final double xPos, yPos;

	public MoveCutsceneEvent(Element xml, Cutscene cutscene)
	{
		super(xml, cutscene);
		this.target = XMLUtils.getAttribute(xml, "target", -1);
		this.xPos = XMLUtils.getAttribute(xml, "xpos", UNSPECIFIED);
		this.yPos = XMLUtils.getAttribute(xml, "ypos", UNSPECIFIED);
		this.speed = XMLUtils.getAttribute(xml, "speed", 1.);
	}

}
