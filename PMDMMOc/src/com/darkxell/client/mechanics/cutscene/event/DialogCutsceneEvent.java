package com.darkxell.client.mechanics.cutscene.event;

import java.util.ArrayList;
import java.util.List;

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
		public final Message message;
		public final int pokemon;

		public CutsceneDialogScreen(Element xml)
		{
			this.message = new Message(xml.getText(), XMLUtils.getAttribute(xml, "translate", true));
			this.pokemon = XMLUtils.getAttribute(xml, "target", -1);
			this.emotion = XMLUtils.getAttribute(xml, "emotion", -1);
		}

		public CutsceneDialogScreen(Message message, int emotion, CutsceneEntity entity)
		{
			this.message = message;
			this.emotion = emotion;
			this.pokemon = entity == null ? -1 : entity.id;
		}

		public CutsceneDialogScreen(String text, boolean translate, int emotion, CutsceneEntity entity)
		{
			this(new Message(text, translate), emotion, entity);
		}

		@Override
		public String toString()
		{
			return this.message.toString();
		}

		public Element toXML()
		{
			Element root = new Element("dialogscreen").setText(this.message.id);
			XMLUtils.setAttribute(root, "translate", this.message.shouldTranslate, true);
			XMLUtils.setAttribute(root, "emotion", this.emotion, -1);
			XMLUtils.setAttribute(root, "target", this.pokemon, -1);
			return root;
		}
	}

	public final boolean isNarratorDialog;
	private boolean isOver;
	public List<CutsceneDialogScreen> screens;

	public DialogCutsceneEvent(Element xml, Cutscene cutscene)
	{
		super(xml, CutsceneEventType.dialog, cutscene);
		this.isNarratorDialog = XMLUtils.getAttribute(xml, "isnarrator", false);
		this.screens = new ArrayList<>();
		for (Element screen : xml.getChildren("dialogscreen", xml.getNamespace()))
			this.screens.add(new CutsceneDialogScreen(screen));
		this.isOver = false;
	}

	public DialogCutsceneEvent(int id, boolean isNarrator, List<CutsceneDialogScreen> screens)
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
			DialogScreen screen = pokemon == null ? new DialogScreen(s.message) : new PokemonDialogScreen(pokemon.toPokemon(), s.message);
			if (this.isNarratorDialog) screen = new NarratorDialogScreen(s.message);
			screens[index++] = screen;
		}

		DialogState state = new DialogState(Persistance.cutsceneState, this, screens);
		Persistance.stateManager.setState(state);
	}

	@Override
	public String toString()
	{
		return this.displayID() + "Dialog: " + this.screens.get(0).message.toString() + "...";
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
