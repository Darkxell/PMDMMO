package com.darkxell.client.mechanics.cutscene;

import java.util.ArrayList;

import org.jdom2.Element;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.cutscene.end.ArbitraryCutsceneEnds;
import com.darkxell.client.mechanics.cutscene.end.EnterDungeonCutsceneEnd;
import com.darkxell.client.mechanics.cutscene.end.LoadFreezoneCutsceneEnd;
import com.darkxell.client.mechanics.cutscene.end.PlayCutsceneCutsceneEnd;
import com.darkxell.client.mechanics.cutscene.end.ResumeExplorationCutsceneEnd;
import com.darkxell.client.mechanics.freezones.entities.FreezoneCamera;
import com.darkxell.common.util.XMLUtils;

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
			if (xml.getChild("resumeexploration", xml.getNamespace()) != null)
				return new ResumeExplorationCutsceneEnd(cutscene, xml.getChild("resumeexploration", xml.getNamespace()));
			return null;
		}

		String arbitraryFunction;
		Cutscene cutscene;

		public CutsceneEnd(Cutscene cutscene, Element xml)
		{
			this(cutscene, XMLUtils.getAttribute(xml, "function", null));
		}

		public CutsceneEnd(Cutscene cutscene, String function)
		{
			this.cutscene = cutscene;
			this.arbitraryFunction = function;
		}

		public String function()
		{
			return this.arbitraryFunction;
		}

		public void onCutsceneEnd()
		{
			Persistance.freezoneCamera = new FreezoneCamera(Persistance.currentplayer);
		}

		public Element toXML()
		{
			Element xml = new Element(this.xmlName());
			if (this.arbitraryFunction != null) xml.setAttribute("function", this.arbitraryFunction);
			return xml;
		}

		protected abstract String xmlName();
	}

	public final CutsceneCreation creation;
	public final ArrayList<CutsceneEvent> events;
	public String name;
	public CutsceneEnd onFinish;
	public final CutscenePlayer player;

	public Cutscene(String name)
	{
		this(name, null, null, new ArrayList<>());
	}

	public Cutscene(String name, CutsceneCreation creation, CutsceneEnd end, ArrayList<CutsceneEvent> events)
	{
		this.name = name;
		this.creation = creation != null ? creation : new CutsceneCreation(this);
		this.onFinish = end != null ? end : new LoadFreezoneCutsceneEnd(this, (String) null);
		this.events = events;
		this.player = new CutscenePlayer(this);

		this.creation.cutscene = this;
		this.onFinish.cutscene = this;
		for (CutsceneEvent e : this.events)
			e.cutscene = this;
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

	public void onCutsceneEnd()
	{
		this.onFinish.onCutsceneEnd();
		if (this.onFinish.arbitraryFunction != null) ArbitraryCutsceneEnds.execute(this.onFinish.arbitraryFunction, this);
	}

	@Override
	public String toString()
	{
		return this.name;
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
