package com.darkxell.client.mechanics.cutscene.event;

import java.util.ArrayList;

import org.jdom2.Element;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;
import com.darkxell.client.mechanics.cutscene.entity.CutscenePokemon;
import com.darkxell.client.state.dialog.AbstractDialogState;
import com.darkxell.client.state.dialog.AbstractDialogState.DialogEndListener;
import com.darkxell.client.state.dialog.AbstractDialogState.DialogScreen;
import com.darkxell.client.state.dialog.DialogState;
import com.darkxell.client.state.dialog.NarratorDialogState;
import com.darkxell.common.util.XMLUtils;
import com.darkxell.common.util.language.Message;

public class DialogCutsceneEvent extends CutsceneEvent implements DialogEndListener
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
	private boolean isOver;
	private ArrayList<CutsceneDialogScreen> screens;

	public DialogCutsceneEvent(Element xml, Cutscene cutscene)
	{
		super(xml, cutscene);
		this.isNarratorDialog = XMLUtils.getAttribute(xml, "isnarrator", false);
		this.screens = new ArrayList<>();
		for (Element screen : xml.getChildren("dialogscreen", xml.getNamespace()))
			this.screens.add(new CutsceneDialogScreen(screen));
		this.isOver = false;
	}

	@Override
	public boolean isOver()
	{
		return this.isOver;
	}

	@Override
	public void onDialogEnd(AbstractDialogState dialog)
	{
		Persistance.stateManager.setState(Persistance.cutsceneState);
		this.isOver = true;
	}

	@Override
	public void onStart()
	{
		super.onStart();
		ArrayList<DialogScreen> screens = new ArrayList<>();
		for (CutsceneDialogScreen s : this.screens)
		{
			CutscenePokemon pokemon = null;
			CutsceneEntity e = this.cutscene.player.getEntity(s.pokemon);
			if (e != null && e instanceof CutscenePokemon) pokemon = (CutscenePokemon) e;
			Message message = new Message(s.text, s.translate);
			DialogScreen screen = new DialogScreen(pokemon == null ? null : pokemon.toPokemon(), message);
			screens.add(screen);
		}

		AbstractDialogState state;
		if (this.isNarratorDialog) state = new NarratorDialogState(this, screens);
		else state = new DialogState(Persistance.cutsceneState, this, screens);
		Persistance.stateManager.setState(state);
	}

}
