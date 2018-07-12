package com.darkxell.client.mechanics.cutscene.event;

import org.jdom2.Element;

import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;
import com.darkxell.common.util.XMLUtils;

public class AnimateCutsceneEvent extends CutsceneEvent
{

	public final String animation;
	public final int target;

	public AnimateCutsceneEvent(Element xml, Cutscene cutscene)
	{
		super(xml, CutsceneEventType.animate, cutscene);
		this.target = XMLUtils.getAttribute(xml, "target", -1);
		this.animation = XMLUtils.getAttribute(xml, "animation", null);
	}

	public AnimateCutsceneEvent(int id, String animation, CutsceneEntity target)
	{
		super(id, CutsceneEventType.animate);
		this.animation = animation;
		this.target = target == null ? -1 : target.id;
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
		return this.displayID() + "Play animation " + this.animation;
	}

	@Override
	public Element toXML()
	{
		Element root = super.toXML();
		root.setAttribute("animation", this.animation);
		XMLUtils.setAttribute(root, "target", this.target, -1);
		return root;
	}

}
