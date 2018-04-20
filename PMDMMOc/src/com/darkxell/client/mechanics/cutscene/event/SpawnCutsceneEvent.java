package com.darkxell.client.mechanics.cutscene.event;

import org.jdom2.Element;

import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;
import com.darkxell.client.mechanics.cutscene.entity.CutscenePokemon;

public class SpawnCutsceneEvent extends CutsceneEvent
{

	public final CutsceneEntity entity;

	public SpawnCutsceneEvent(Element xml, Cutscene cutscene)
	{
		super(xml, cutscene);
		if (xml.getName().equals("spawnpokemon")) this.entity = new CutscenePokemon(xml);
		else this.entity = new CutsceneEntity(xml);
	}

	@Override
	public void onStart()
	{
		super.onStart();
		this.cutscene.createEntity(this.entity);
	}

}
