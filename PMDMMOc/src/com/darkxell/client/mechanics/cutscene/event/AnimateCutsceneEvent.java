package com.darkxell.client.mechanics.cutscene.event;

import org.jdom2.Element;

import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.common.util.XMLUtils;

public class AnimateCutsceneEvent extends CutsceneEvent
{

	public final String animation;
	public final int target;

	public AnimateCutsceneEvent(Element xml, Cutscene cutscene)
	{
		super(xml, cutscene);
		this.target = XMLUtils.getAttribute(xml, "target", -1);
		this.animation = XMLUtils.getAttribute(xml, "animation", null);
	}

	@Override
	public String getIconPath()
	{
		return "/icons/events/animate.png";
	}

	@Override
	public void onStart()
	{
		super.onStart();
		System.out.println("Playing animation: " + this.animation);
	}

	@Override
	public String toString()
	{
		return "Play animation " + this.animation;
	}

	@Override
	public Element toXML()
	{
		return new Element("animate");
	}

}
