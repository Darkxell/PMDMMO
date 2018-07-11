package com.darkxell.client.mechanics.cutscene.event;

import java.util.ArrayList;

import org.jdom2.Element;

import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;

public class WaitCutsceneEvent extends CutsceneEvent
{

	private ArrayList<CutsceneEvent> events;

	public WaitCutsceneEvent(Element xml, Cutscene cutscene)
	{
		super(xml, cutscene);
		this.events = new ArrayList<>();
		for (Element event : xml.getChildren("event", xml.getNamespace()))
			try
			{
				int id = Integer.parseInt(event.getText());
				CutsceneEvent e = this.cutscene.getEvent(id);
				if (e != null) this.events.add(e);
			} catch (Exception e)
			{}
		if (this.events.isEmpty()) this.events.addAll(this.cutscene.events);
	}

	@Override
	public String getIconPath()
	{
		return "/icons/events/wait.png";
	}

	@Override
	public boolean isOver()
	{
		for (CutsceneEvent event : this.events)
			if (!event.isOver()) return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "Wait for " + this.events.size() + " events";
	}

	@Override
	public Element toXML()
	{
		return new Element("wait");
	}

}
