package com.darkxell.client.mechanics.cutscene;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.jdom2.Element;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.cutscene.end.EnterDungeonCutsceneEnd;
import com.darkxell.client.mechanics.cutscene.end.LoadFreezoneCutsceneEnd;
import com.darkxell.client.mechanics.cutscene.end.PlayCutsceneCutsceneEnd;
import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;
import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.client.mechanics.freezones.entities.FreezoneCamera;
import com.darkxell.client.mechanics.freezones.zones.BaseFreezone;
import com.darkxell.client.renderers.AbstractRenderer;

public class Cutscene
{

	public static abstract class CutsceneEnd
	{
		public static CutsceneEnd create(Cutscene cutscene, Element xml)
		{
			if (xml.getChild("enterdungeon", xml.getNamespace()) != null) return new EnterDungeonCutsceneEnd(cutscene, xml);
			if (xml.getChild("playcutscene", xml.getNamespace()) != null) return new PlayCutsceneCutsceneEnd(cutscene, xml);
			if (xml.getChild("loadfreezone", xml.getNamespace()) != null) return new LoadFreezoneCutsceneEnd(cutscene, xml);
			return null;
		}

		public final Cutscene cutscene;

		public CutsceneEnd(Cutscene cutscene)
		{
			this.cutscene = cutscene;
		}

		public void onCutsceneEnd()
		{
			Persistance.freezoneCamera = new FreezoneCamera(Persistance.currentplayer);
		}
	}

	public final CutsceneCreation creation;
	private final ArrayList<CutsceneEntity> entities;
	public final ArrayList<CutsceneEvent> events;
	public final CutsceneEnd onFinish;

	public Cutscene(Element xml)
	{
		this.creation = new CutsceneCreation(this, xml.getChild("creation", xml.getNamespace()));
		this.onFinish = CutsceneEnd.create(this, xml.getChild("onfinish", xml.getNamespace()));

		this.events = new ArrayList<>();
		for (Element event : xml.getChild("events", xml.getNamespace()).getChildren())
		{
			CutsceneEvent e = CutsceneEvent.create(event, this);
			if (e != null) this.events.add(e);
		}

		this.entities = new ArrayList<>();
	}

	public void createEntity(CutsceneEntity entity)
	{
		this.entities.add(entity);
		if (Persistance.cutsceneState != null)
		{
			AbstractRenderer renderer = entity.createRenderer();
			if (renderer != null) Persistance.currentmap.cutsceneEntityRenderers.register(entity, renderer);
		}
	}

	public CutsceneEntity getEntity(int id)
	{
		if (id == -1) return null;
		for (CutsceneEntity e : this.entities)
			if (e.id == id) return e;
		return null;
	}

	public CutsceneEvent getEvent(int id)
	{
		for (CutsceneEvent e : this.events)
			if (e.id == id) return e;
		return null;
	}

	public FreezoneMap loadMap()
	{
		String baseName = BaseFreezone.class.getName();
		Class<?> c;
		try
		{
			c = Class.forName(baseName.substring(0, baseName.length() - "BaseFreezone".length()) + this.creation.freezoneID + "Freezone");
			if (c == null) return null;
			FreezoneMap map = (FreezoneMap) c.getConstructor().newInstance();
			return map;
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public void removeEntity(CutsceneEntity entity)
	{
		if (this.entities.contains(entity))
		{
			this.entities.remove(entity);
			if (Persistance.cutsceneState != null) Persistance.currentmap.cutsceneEntityRenderers.unregister(entity);
		}
	}

}
