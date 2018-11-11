package com.darkxell.client.mechanics.cutscene.event;

import org.jdom2.Element;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;
import com.darkxell.client.mechanics.cutscene.entity.CutscenePokemon;
import com.darkxell.client.mechanics.cutscene.event.DialogCutsceneEvent.CutsceneDialogScreen;
import com.darkxell.client.state.dialog.DialogState;
import com.darkxell.client.state.dialog.DialogState.DialogEndListener;
import com.darkxell.client.state.dialog.OptionDialogScreen;
import com.darkxell.client.state.dialog.PokemonDialogScreen.DialogPortraitLocation;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.XMLUtils;
import com.darkxell.common.util.language.Message;

public class OptionDialogCutsceneEvent extends CutsceneEvent implements DialogEndListener
{

	private int chosen = -1;
	private boolean isOver;
	private Message[] options;
	private CutsceneDialogScreen question;

	public OptionDialogCutsceneEvent(Element xml, Cutscene cutscene)
	{
		super(xml, CutsceneEventType.option, cutscene);
		if (xml.getChild("question", xml.getNamespace()) == null)
			this.question = new CutsceneDialogScreen(new Message("ERROR"), 0, null, DialogPortraitLocation.TOP_LEFT);
		else this.question = new CutsceneDialogScreen(xml.getChild("question", xml.getNamespace()));
		this.options = new Message[xml.getChildren("option", xml.getNamespace()).size()];
		int i = 0;
		for (Element option : xml.getChildren("option", xml.getNamespace()))
			this.options[i] = new Message(XMLUtils.getAttribute(option, "value", "option" + i), XMLUtils.getAttribute(option, "translate", true));
	}

	public OptionDialogCutsceneEvent(int id, CutsceneDialogScreen question, Message... options)
	{
		super(id, CutsceneEventType.option);
		this.question = question;
		this.options = options;
	}

	public int chosen()
	{
		return this.chosen;
	}

	@Override
	public boolean isOver()
	{
		return this.isOver;
	}

	@Override
	public void onDialogEnd(DialogState dialog)
	{
		this.isOver = true;
		this.chosen = ((OptionDialogScreen) dialog.getScreen(1)).chosenIndex();
		Persistance.stateManager.setState(Persistance.cutsceneState);
	}

	@Override
	public void onStart()
	{
		super.onStart();
		this.isOver = false;
		CutscenePokemon pokemon = null;
		CutsceneEntity e = this.cutscene.player.getEntity(this.question.pokemon);
		if (e != null && e instanceof CutscenePokemon) pokemon = (CutscenePokemon) e;
		if (!this.question.hasReplacements) this.question.addReplacements(pokemon);
		Pokemon instance = pokemon == null ? null : pokemon.toPokemon();

		OptionDialogScreen screen = new OptionDialogScreen(instance, this.question.message, this.question.portraitLocation, this.options);
		screen.id = 1;
		DialogState state = new DialogState(Persistance.cutsceneState, this, screen);
		Persistance.stateManager.setState(state);
	}

	@Override
	public String toString()
	{
		return this.displayID() + "Choose option for: " + this.question.message.toString();
	}

	@Override
	public Element toXML()
	{
		Element root = super.toXML();
		root.addContent(this.question.toXML());
		for (Message option : this.options)
		{
			Element o = new Element("option").setAttribute("value", option.id);
			XMLUtils.setAttribute(o, "translate", option.shouldTranslate, true);
			root.addContent(o);
		}
		return root;
	}

}
