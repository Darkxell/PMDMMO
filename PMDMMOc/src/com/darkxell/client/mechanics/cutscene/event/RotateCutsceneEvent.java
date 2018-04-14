package com.darkxell.client.mechanics.cutscene.event;

import org.jdom2.Element;

import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.XMLUtils;

public class RotateCutsceneEvent extends CutsceneEvent
{

	public final Direction direction;
	public final boolean instantly;
	public final int target;

	public RotateCutsceneEvent(Element xml, Cutscene cutscene)
	{
		super(xml, cutscene);
		this.target = XMLUtils.getAttribute(xml, "target", -1);
		this.instantly = XMLUtils.getAttribute(xml, "instantly", false);
		Direction d = null;
		try
		{
			d = Direction.valueOf(XMLUtils.getAttribute(xml, "facing", null));
		} catch (Exception e)
		{}
		this.direction = d;
	}

}
