package com.darkxell.client.mechanics.cutscene;

import java.util.ArrayList;

import org.jdom2.Element;

import com.darkxell.client.mechanics.cutscene.end.EnterDungeonCutsceneEnd;
import com.darkxell.client.mechanics.cutscene.end.LoadFreezoneCutsceneEnd;
import com.darkxell.client.mechanics.cutscene.end.PlayCutsceneCutsceneEnd;
import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;

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

		public abstract void onCutsceneEnd();
	}

	public final CutsceneCreation creation;
	public final ArrayList<CutsceneEntity> entities;
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

	public CutsceneEntity get(int id)
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

}
