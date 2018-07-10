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

		Cutscene cutscene;

		public CutsceneEnd(Cutscene cutscene)
		{
			this.cutscene = cutscene;
		}

		public void onCutsceneEnd()
		{
			Persistance.freezoneCamera = new FreezoneCamera(Persistance.currentplayer);
		}

		public abstract Element toXML();
	}

	public final CutsceneCreation creation;
	public final ArrayList<CutsceneEvent> events;
	public String name;
	public final CutsceneEnd onFinish;
	public final CutscenePlayer player;

	public Cutscene(String name)
	{
		this(name, null, null, new ArrayList<>());
	}

	public Cutscene(String name, CutsceneCreation creation, CutsceneEnd end, ArrayList<CutsceneEvent> events)
	{
		this.name = name;
		this.creation = creation != null ? creation : new CutsceneCreation(this);
		this.onFinish = end != null ? end : new LoadFreezoneCutsceneEnd(this);
		this.events = events;
		this.player = new CutscenePlayer(this);
	}

	public Cutscene(String name, Element xml)
	{
		this.name = name;
		this.creation = new CutsceneCreation(this, xml.getChild("creation", xml.getNamespace()));
		this.onFinish = CutsceneEnd.create(this, xml.getChild("onfinish", xml.getNamespace()));
		this.onFinish.cutscene = this;

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
		Element root = new Element("cutscene").setAttribute("name", this.name);
		root.addContent(this.creation.toXML());
		Element events = new Element("events");
		for (CutsceneEvent event : this.events)
			events.addContent(event.toXML());
		root.addContent(events);
		root.addContent(new Element("onfinish").addContent(this.onFinish.toXML()));
		return root;
	}

}
