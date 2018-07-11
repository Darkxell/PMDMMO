package com.darkxell.client.mechanics.cutscene.event;

import org.jdom2.Element;

import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;
import com.darkxell.client.mechanics.cutscene.entity.CutscenePokemon;
import com.darkxell.common.util.XMLUtils;

public class SetAnimatedCutsceneEvent extends CutsceneEvent
{

	public final boolean animated;
	public final int target;

	public SetAnimatedCutsceneEvent(Element xml, Cutscene cutscene)
	{
		super(xml, cutscene);
		this.target = XMLUtils.getAttribute(xml, "target", -1);
		this.animated = XMLUtils.getAttribute(xml, "animate", false);
	}

	@Override
	public String getIconPath()
	{
		return "/icons/events/setanimated.png";
	}

	@Override
	public void onStart()
	{
		super.onStart();
		CutsceneEntity entity = this.cutscene.player.getEntity(this.target);
		if (entity != null && entity instanceof CutscenePokemon) ((CutscenePokemon) entity).animated = this.animated;
	}

	@Override
	public String toString()
	{
		return "(" + this.target + ") becomes " + (this.animated ? "" : "not ") + "animated";
	}

	@Override
	public Element toXML()
	{
		return new Element("setanimated");
	}

}
