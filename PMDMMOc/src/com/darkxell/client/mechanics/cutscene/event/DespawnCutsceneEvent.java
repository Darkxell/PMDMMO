package com.darkxell.client.mechanics.cutscene.event;

import org.jdom2.Element;

import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;
import com.darkxell.common.util.XMLUtils;

public class DespawnCutsceneEvent extends CutsceneEvent
{

	public final int target;

	public DespawnCutsceneEvent(Element xml, Cutscene cutscene)
	{
		super(xml, CutsceneEventType.despawn, cutscene);
		this.target = XMLUtils.getAttribute(xml, "target", -1);
	}

	public DespawnCutsceneEvent(int id, CutsceneEntity entity)
	{
		super(id, CutsceneEventType.despawn);
		this.target = entity.id;
	}

	@Override
	public void onStart()
	{
		super.onStart();
		CutsceneEntity entity = this.cutscene.player.getEntity(this.target);
		if (entity != null) this.cutscene.player.removeEntity(entity);
	}

	@Override
	public String toString()
	{
		return this.displayID() + "(" + this.target + ") despawns";
	}

	@Override
	public Element toXML()
	{
		return super.toXML().setAttribute("target", String.valueOf(this.target));
	}

}
