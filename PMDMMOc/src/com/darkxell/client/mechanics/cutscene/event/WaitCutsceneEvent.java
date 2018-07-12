package com.darkxell.client.mechanics.cutscene.event;

import java.util.ArrayList;

import org.jdom2.Element;

import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;

public class WaitCutsceneEvent extends CutsceneEvent
{

	public final boolean all;
	private ArrayList<CutsceneEvent> events;

	public WaitCutsceneEvent(Element xml, Cutscene cutscene)
	{
		super(xml, CutsceneEventType.wait, cutscene);
		this.events = new ArrayList<>();
		for (Element event : xml.getChildren("event", xml.getNamespace()))
			try
			{
				int id = Integer.parseInt(event.getText());
				CutsceneEvent e = this.cutscene.getEvent(id);
				if (e != null) this.events.add(e);
			} catch (Exception e)
			{}
		this.all = this.events.isEmpty();
		if (this.events.isEmpty()) this.events.addAll(this.cutscene.events);
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
		return "Wait for " + (this.all ? "all" : this.events.size()) + " events";
	}

	@Override
	public Element toXML()
	{
		Element root = super.toXML();
		if (!this.all) for (CutsceneEvent e : this.events)
			root.addContent(new Element("event").setText(String.valueOf(e.id)));
		return root;
	}

}
