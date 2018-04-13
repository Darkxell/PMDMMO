package com.darkxell.client.mechanics.cutscene;

import java.util.ArrayList;

import org.jdom2.Element;

public class Cutscene
{

	public static abstract class CutsceneEnd
	{
		public static CutsceneEnd create(Element xml)
		{
			// TODO Auto-generated method stub
			return null;
		}

		public abstract void onCutsceneEnd();
	}

	public final CutsceneCreation creation;
	private final ArrayList<CutsceneEvent> events;
	public final CutsceneEnd onFinish;

	public Cutscene(Element xml)
	{
		this.creation = new CutsceneCreation(xml.getChild("creation", xml.getNamespace()));
		this.onFinish = CutsceneEnd.create(xml.getChild("onfinish", xml.getNamespace()));

		this.events = new ArrayList<>();
		for (Element event : xml.getChild("events", xml.getNamespace()).getChildren())
			this.events.add(CutsceneEvent.create(event));
	}

}
