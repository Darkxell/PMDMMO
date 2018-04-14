package com.darkxell.client.mechanics.cutscene.event;

import java.util.ArrayList;

import org.jdom2.Element;

import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.common.util.XMLUtils;

public class DialogCutsceneEvent extends CutsceneEvent
{

	public static class CutsceneDialogScreen
	{
		public final int emotion;
		public final int pokemon;
		public final String text;
		public final boolean translate;

		public CutsceneDialogScreen(Element xml)
		{
			this.text = XMLUtils.getAttribute(xml, "text", null);
			this.pokemon = XMLUtils.getAttribute(xml, "target", -1);
			this.emotion = XMLUtils.getAttribute(xml, "emotion", -1);
			this.translate = XMLUtils.getAttribute(xml, "translate", true);
		}
	}

	public final boolean isNarratorDialog;
	private ArrayList<CutsceneDialogScreen> screens;

	public DialogCutsceneEvent(Element xml, Cutscene cutscene)
	{
		super(xml, cutscene);
		this.isNarratorDialog = XMLUtils.getAttribute(xml, "isnarrator", false);
		this.screens = new ArrayList<>();
		for (Element screen : xml.getChildren("dialogscreen", xml.getNamespace()))
			this.screens.add(new CutsceneDialogScreen(screen));
	}

}
