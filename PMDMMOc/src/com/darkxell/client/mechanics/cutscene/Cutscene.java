package com.darkxell.client.mechanics.cutscene;

import java.util.ArrayList;

import org.jdom2.Element;

import com.darkxell.client.mechanics.cutscene.end.EnterDungeonCutsceneEnd;
import com.darkxell.client.mechanics.cutscene.end.LoadFreezoneCutsceneEnd;
import com.darkxell.client.mechanics.cutscene.end.PlayCutsceneCutsceneEnd;

public class Cutscene
{

	public static abstract class CutsceneEnd
	{
		public static CutsceneEnd create(Element xml)
		{
			if (xml.getChild("enterdungeon", xml.getNamespace()) != null) return new EnterDungeonCutsceneEnd(xml);
			if (xml.getChild("playcutscene", xml.getNamespace()) != null) return new PlayCutsceneCutsceneEnd(xml);
			if (xml.getChild("loadfreezone", xml.getNamespace()) != null) return new LoadFreezoneCutsceneEnd(xml);
			return null;
		}

		public abstract void onCutsceneEnd();
	}

	public final CutsceneCreation creation;
	public final ArrayList<CutsceneEvent> events;
	public final CutsceneEnd onFinish;

	public Cutscene(Element xml)
	{
		this.creation = new CutsceneCreation(xml.getChild("creation", xml.getNamespace()));
		this.onFinish = CutsceneEnd.create(xml.getChild("onfinish", xml.getNamespace()));

		this.events = new ArrayList<>();
		for (Element event : xml.getChild("events", xml.getNamespace()).getChildren())
		{
			CutsceneEvent e = CutsceneEvent.create(event, this);
			if (e != null) this.events.add(e);
		}
	}

	public CutsceneEvent getEvent(int id)
	{
		for (CutsceneEvent e : this.events)
			if (e.id == id) return e;
		return null;
	}

}
