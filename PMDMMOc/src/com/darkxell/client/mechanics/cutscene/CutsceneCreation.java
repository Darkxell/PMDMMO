package com.darkxell.client.mechanics.cutscene;

import java.util.ArrayList;

import org.jdom2.Element;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;
import com.darkxell.client.mechanics.cutscene.entity.CutscenePokemon;
import com.darkxell.client.mechanics.freezones.entities.FreezoneCamera;
import com.darkxell.common.util.XMLUtils;

public class CutsceneCreation
{

	public final int camerax, cameray;
	public final Cutscene cutscene;
	private final ArrayList<CutsceneEntity> entities;
	public final boolean fading;
	public final String freezoneID;
	public final CutscenePokemon player;

	public CutsceneCreation(Cutscene cutscene, Element xml)
	{
		this.cutscene = cutscene;
		this.freezoneID = XMLUtils.getAttribute(xml, "freezone", null);
		this.fading = XMLUtils.getAttribute(xml, "fade", true);
		this.camerax = XMLUtils.getAttribute(xml, "camerax", -1);
		this.cameray = XMLUtils.getAttribute(xml, "cameray", -1);
		this.player = new CutscenePokemon(Persistance.player.getTeamLeader(), xml.getChild("player", xml.getNamespace()));
		this.entities = new ArrayList<>();
		for (Element pokemon : xml.getChildren("pokemon", xml.getNamespace()))
			this.entities.add(new CutscenePokemon(pokemon));
		for (Element entity : xml.getChildren("entity", xml.getNamespace()))
			this.entities.add(new CutsceneEntity(entity));
	}

	public void create()
	{
		Persistance.currentmap = this.cutscene.loadMap();
		Persistance.freezoneCamera = new FreezoneCamera(null);
		if (this.camerax != -1) Persistance.freezoneCamera.x = this.camerax;
		if (this.cameray != -1) Persistance.freezoneCamera.y = this.cameray;
		for (CutsceneEntity entity : this.entities)
			this.cutscene.player.createEntity(entity);
		this.cutscene.player.createEntity(this.player);
	}

}
