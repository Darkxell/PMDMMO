package com.darkxell.client.mechanics.cutscene.event;

import org.jdom2.Element;

import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.common.util.XMLUtils;

public class OptionResultCutsceneEvent extends CutsceneEvent
{

	public final int option;
	public final CutsceneEvent[] results;
	public final CutsceneEvent target;

	public OptionResultCutsceneEvent(Element xml, Cutscene cutscene)
	{
		super(xml, CutsceneEventType.optionresult, cutscene);
		this.option = XMLUtils.getAttribute(xml, "option", -1);
		this.target = this.cutscene.getEvent(XMLUtils.getAttribute(xml, "dialog", -10));
		this.results = new CutsceneEvent[xml.getChildren().size()];
		int i = 0;
		for (Element e : xml.getChildren())
			this.results[i++] = CutsceneEvent.create(e, cutscene);
	}

	public OptionResultCutsceneEvent(int id, int option, CutsceneEvent target, CutsceneEvent... results)
	{
		super(id, CutsceneEventType.optionresult);
		this.option = option;
		this.target = target;
		this.results = results;
	}

	@Override
	public void onStart()
	{
		super.onStart();
		if (this.target != null && this.target instanceof OptionDialogCutsceneEvent && ((OptionDialogCutsceneEvent) this.target).chosen() == this.option)
			this.cutscene.player.addEvents(this.results);
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
