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
		super(xml, CutsceneEventType.setanimated, cutscene);
		this.target = XMLUtils.getAttribute(xml, "target", -1);
		this.animated = XMLUtils.getAttribute(xml, "animated", false);
	}

	public SetAnimatedCutsceneEvent(int id, CutsceneEntity entity, boolean animated)
	{
		super(id, CutsceneEventType.setanimated);
		this.target = entity.id;
		this.animated = animated;
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
		return this.displayID() + "(" + this.target + ") becomes " + (this.animated ? "" : "not ") + "animated";
	}

	@Override
	public Element toXML()
	{
		return super.toXML().setAttribute("target", String.valueOf(this.target)).setAttribute("animated", String.valueOf(this.animated));
	}

}
