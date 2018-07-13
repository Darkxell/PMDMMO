package com.darkxell.client.mechanics.cutscene;

import java.util.ArrayList;

import org.jdom2.Element;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;
import com.darkxell.client.mechanics.cutscene.entity.CutscenePokemon;
import com.darkxell.client.mechanics.freezones.Freezones;
import com.darkxell.client.mechanics.freezones.entities.FreezoneCamera;
import com.darkxell.common.util.XMLUtils;
import com.darkxell.common.zones.FreezoneInfo;

public class CutsceneCreation
{

	public final int camerax, cameray;
	Cutscene cutscene;
	private final ArrayList<CutsceneEntity> entities;
	public final boolean fading;
	public final FreezoneInfo freezone;

	public CutsceneCreation(Cutscene cutscene)
	{
		this.cutscene = cutscene;
		this.freezone = null;
		this.camerax = this.cameray = -1;
		this.fading = false;
		this.entities = new ArrayList<>();
	}

	public CutsceneCreation(Cutscene cutscene, Element xml)
	{
		this.cutscene = cutscene;
		this.freezone = FreezoneInfo.find(xml.getAttributeValue("freezone"));
		this.fading = XMLUtils.getAttribute(xml, "fade", true);
		this.camerax = XMLUtils.getAttribute(xml, "camerax", -1);
		this.cameray = XMLUtils.getAttribute(xml, "cameray", -1);
		this.entities = new ArrayList<>();
		for (Element pokemon : xml.getChildren("pokemon", xml.getNamespace()))
			this.entities.add(new CutscenePokemon(pokemon));
		for (Element entity : xml.getChildren("entity", xml.getNamespace()))
			this.entities.add(new CutsceneEntity(entity));
	}

	public CutsceneCreation(FreezoneInfo freezone, boolean fading, int camerax, int cameray, ArrayList<CutsceneEntity> entities)
	{
		this.freezone = freezone;
		this.fading = fading;
		this.camerax = camerax;
		this.cameray = cameray;
		this.entities = entities;
	}

	public void create()
	{
		Persistance.currentmap = Freezones.loadMap(this.freezone);
		Persistance.freezoneCamera = new FreezoneCamera(null);
		if (this.camerax != -1) Persistance.freezoneCamera.x = this.camerax;
		if (this.cameray != -1) Persistance.freezoneCamera.y = this.cameray;
		for (CutsceneEntity entity : this.entities)
			this.cutscene.player.createEntity(entity);
	}

	public CutsceneEntity[] entities()
	{
		return this.entities.toArray(new CutsceneEntity[this.entities.size()]);
	}

	public Element toXML()
	{
		Element root = new Element("creation");
		XMLUtils.setAttribute(root, "freezone", this.freezone.id, null);
		XMLUtils.setAttribute(root, "camerax", this.camerax, -1);
		XMLUtils.setAttribute(root, "cameray", this.cameray, -1);
		XMLUtils.setAttribute(root, "fading", this.fading, false);
		for (CutsceneEntity entity : this.entities)
			root.addContent(entity.toXML());
		return root;
	}

}
