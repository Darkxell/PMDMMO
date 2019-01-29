package com.darkxell.client.mechanics.cutscene.event;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;

import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.CutsceneContext;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.common.util.xml.XMLUtils;

public class OptionResultCutsceneEvent extends CutsceneEvent implements CutsceneContext
{

	public final int option;
	public final CutsceneEvent[] results;
	public final CutsceneEvent target;

	public OptionResultCutsceneEvent(Element xml, CutsceneContext context)
	{
		super(xml, CutsceneEventType.optionresult, context);
		this.option = XMLUtils.getAttribute(xml, "option", -1);
		this.target = this.context.getEvent(XMLUtils.getAttribute(xml, "dialog", -10));
		this.results = new CutsceneEvent[xml.getChildren().size()];
		int i = 0;
		for (Element e : xml.getChildren())
			this.results[i++] = CutsceneEvent.create(e, this);
	}

	public OptionResultCutsceneEvent(int id, int option, CutsceneEvent target, CutsceneEvent... results)
	{
		super(id, CutsceneEventType.optionresult);
		this.option = option;
		this.target = target;
		this.results = results;
	}

	@Override
	public List<CutsceneEvent> availableEvents()
	{
		ArrayList<CutsceneEvent> events = new ArrayList<>(this.context.availableEvents());
		for (CutsceneEvent e : this.results)
			if (e != null) events.add(e);
		return events;
	}

	@Override
	public void onStart()
	{
		super.onStart();
		if (this.target != null && this.target instanceof OptionDialogCutsceneEvent && ((OptionDialogCutsceneEvent) this.target).chosen() == this.option)
			this.context.parent().player.addEvents(this.results);
	}

	@Override
	public Cutscene parent()
	{
		return this.context.parent();
	}

	@Override
	public String toString()
	{
		return this.displayID() + "If choice for event (" + this.target + ") is " + this.option + ": Create " + this.results.length + " events";
	}

	@Override
	public Element toXML()
	{
		Element root = super.toXML();
		root.setAttribute("option", String.valueOf(this.option));
		if (this.target != null) root.setAttribute("dialog", String.valueOf(this.target.id));

		for (CutsceneEvent e : this.results)
			root.addContent(e.toXML());

		return root;
	}

}
