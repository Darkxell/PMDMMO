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
		super(xml, cutscene);
		this.target = XMLUtils.getAttribute(xml, "target", -1);
	}

	@Override
	public void onStart()
	{
		super.onStart();
		CutsceneEntity entity = this.cutscene.player.getEntity(this.target);
		if (entity != null) this.cutscene.player.removeEntity(entity);
	}

}
