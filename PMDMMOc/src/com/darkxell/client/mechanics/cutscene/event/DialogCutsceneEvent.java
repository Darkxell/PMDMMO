package com.darkxell.client.mechanics.cutscene.event;

import java.util.ArrayList;

import org.jdom2.Element;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;
import com.darkxell.client.mechanics.cutscene.entity.CutscenePokemon;
import com.darkxell.client.state.dialog.DialogScreen;
import com.darkxell.client.state.dialog.DialogState;
import com.darkxell.client.state.dialog.DialogState.DialogEndListener;
import com.darkxell.client.state.dialog.NarratorDialogScreen;
import com.darkxell.client.state.dialog.PokemonDialogScreen;
import com.darkxell.common.util.XMLUtils;
import com.darkxell.common.util.language.Message;

public class DialogCutsceneEvent extends CutsceneEvent implements DialogEndListener
{

	public static class CutsceneDialogScreen
	{
		public final int emotion;
		/** <b>Only used in editor!</b> */
		public CutsceneEntity entity;
		public final int pokemon;
		public final String text;
		public final boolean translate;

		public CutsceneDialogScreen(Element xml)
		{
			this.text = xml.getText();
			this.pokemon = XMLUtils.getAttribute(xml, "target", -1);
			this.emotion = XMLUtils.getAttribute(xml, "emotion", -1);
			this.translate = XMLUtils.getAttribute(xml, "translate", true);
		}

		public CutsceneDialogScreen(String text, boolean translate, int emotion, CutsceneEntity entity)
		{
			this.text = text;
			this.translate = translate;
			this.emotion = emotion;
			this.pokemon = entity == null ? -1 : entity.id;
			this.entity = entity;
		}

		@Override
		public String toString()
		{
			return new Message(this.text, this.translate).toString();
		}

		public Element toXML()
		{
			Element root = new Element("dialogscreen").setText(this.text);
			XMLUtils.setAttribute(root, "translate", this.translate, true);
			XMLUtils.setAttribute(root, "emotion", this.emotion, -1);
			XMLUtils.setAttribute(root, "target", this.entity == null ? -1 : this.entity.id, -1);
			return root;
		}
	}

	public final boolean isNarratorDialog;
	private boolean isOver;
	public ArrayList<CutsceneDialogScreen> screens;

	public DialogCutsceneEvent(Element xml, Cutscene cutscene)
	{
		super(xml, CutsceneEventType.dialog, cutscene);
		this.isNarratorDialog = XMLUtils.getAttribute(xml, "isnarrator", false);
		this.screens = new ArrayList<>();
		for (Element screen : xml.getChildren("dialogscreen", xml.getNamespace()))
			this.screens.add(new CutsceneDialogScreen(screen));
		this.isOver = false;
	}

	public DialogCutsceneEvent(int id, boolean isNarrator, ArrayList<CutsceneDialogScreen> screens)
	{
		super(id, CutsceneEventType.dialog);
		this.isNarratorDialog = isNarrator;
		this.screens = screens;
	}

	@Override
	public boolean isOver()
	{
		return this.isOver;
	}

	@Override
	public void onDialogEnd(DialogState dialog)
	{
		Persistance.stateManager.setState(Persistance.cutsceneState);
		this.isOver = true;
	}

	@Override
	public void onStart()
	{
		super.onStart();
		DialogScreen[] screens = new DialogScreen[this.screens.size()];
		int index = 0;
		for (CutsceneDialogScreen s : this.screens)
		{
			CutscenePokemon pokemon = null;
			CutsceneEntity e = this.cutscene.player.getEntity(s.pokemon);
			if (e != null && e instanceof CutscenePokemon) pokemon = (CutscenePokemon) e;
			Message message = new Message(s.text, s.translate);
			DialogScreen screen = pokemon == null ? new DialogScreen(message) : new PokemonDialogScreen(pokemon.toPokemon(), message);
			if (this.isNarratorDialog) screen = new NarratorDialogScreen(message);
			screens[index++] = screen;
		}

		DialogState state = new DialogState(Persistance.cutsceneState, this, screens);
		Persistance.stateManager.setState(state);
	}

	@Override
	public String toString()
	{
		return this.displayID() + "Dialog: " + new Message(this.screens.get(0).text).toString() + "...";
	}

	@Override
	public Element toXML()
	{
		Element root = super.toXML();
		XMLUtils.setAttribute(root, "isnarrator", this.isNarratorDialog, false);
		for (CutsceneDialogScreen screen : this.screens)
			root.addContent(screen.toXML());
		return root;
	}

}
