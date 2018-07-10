package com.darkxell.client.mechanics.cutscene;

import java.util.ArrayList;

import org.jdom2.Element;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.cutscene.end.EnterDungeonCutsceneEnd;
import com.darkxell.client.mechanics.cutscene.end.LoadFreezoneCutsceneEnd;
import com.darkxell.client.mechanics.cutscene.end.PlayCutsceneCutsceneEnd;
import com.darkxell.client.mechanics.freezones.entities.FreezoneCamera;

public class Cutscene implements Comparable<Cutscene>
{

	public static abstract class CutsceneEnd
	{
		public static CutsceneEnd create(Cutscene cutscene, Element xml)
		{
			if (xml.getChild("enterdungeon", xml.getNamespace()) != null)
				return new EnterDungeonCutsceneEnd(cutscene, xml.getChild("enterdungeon", xml.getNamespace()));
			if (xml.getChild("playcutscene", xml.getNamespace()) != null)
				return new PlayCutsceneCutsceneEnd(cutscene, xml.getChild("playcutscene", xml.getNamespace()));
			if (xml.getChild("loadfreezone", xml.getNamespace()) != null)
				return new LoadFreezoneCutsceneEnd(cutscene, xml.getChild("loadfreezone", xml.getNamespace()));
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

	public CutsceneCreation creation;
	public final ArrayList<CutsceneEvent> events;
	public String name;
	public CutsceneEnd onFinish;
	public CutscenePlayer player;

	public Cutscene(String name)
	{
		this.name = name;
		this.creation = null;
		this.onFinish = null;
		this.events = new ArrayList<>();
		this.player = new CutscenePlayer(this);
	}

	public Cutscene(String name, Element xml)
	{
		this.name = name;
		this.creation = new CutsceneCreation(this, xml.getChild("creation", xml.getNamespace()));
		this.onFinish = CutsceneEnd.create(this, xml.getChild("onfinish", xml.getNamespace()));

		this.events = new ArrayList<>();
		for (Element event : xml.getChild("events", xml.getNamespace()).getChildren())
		{
			CutsceneEvent e = CutsceneEvent.create(event, this);
			if (e != null) this.events.add(e);
		}

		this.player = new CutscenePlayer(this);
	}

	@Override
	public int compareTo(Cutscene o)
	{
		return this.name.compareTo(o.name);
	}

	public CutsceneEvent getEvent(int id)
	{
		for (CutsceneEvent e : this.events)
			if (e.id == id) return e;
		return null;
	}

	public Element toXML()
	{
		return new Element("cutscene").setAttribute("name", this.name);
	}

}
