package com.darkxell.client.mechanics.cutscene.event;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.cutscene.CutsceneContext;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;
import com.darkxell.client.mechanics.cutscene.entity.CutscenePokemon;
import com.darkxell.client.state.dialog.DialogScreen;
import com.darkxell.client.state.dialog.DialogState;
import com.darkxell.client.state.dialog.DialogState.DialogEndListener;
import com.darkxell.client.state.dialog.NarratorDialogScreen;
import com.darkxell.client.state.dialog.PokemonDialogScreen;
import com.darkxell.client.state.dialog.PokemonDialogScreen.DialogPortraitLocation;
import com.darkxell.common.util.XMLUtils;
import com.darkxell.common.util.language.Message;

public class DialogCutsceneEvent extends CutsceneEvent implements DialogEndListener
{

	public static class CutsceneDialogScreen
	{
		public final int emotion;
		boolean hasReplacements = false;
		public final Message message;
		public final int pokemon;
		public final DialogPortraitLocation portraitLocation;

		public CutsceneDialogScreen(Element xml)
		{
			this.message = new Message(xml.getText(), XMLUtils.getAttribute(xml, "translate", true));
			this.pokemon = XMLUtils.getAttribute(xml, "target", -1);
			this.emotion = XMLUtils.getAttribute(xml, "emotion", -1);
			this.portraitLocation = DialogPortraitLocation.valueOf(XMLUtils.getAttribute(xml, "portrait-location", DialogPortraitLocation.BOTTOM_LEFT.name()));
		}

		public CutsceneDialogScreen(Message message, int emotion, CutsceneEntity entity, DialogPortraitLocation portraitLocation)
		{
			this.message = message;
			this.emotion = emotion;
			this.pokemon = entity == null ? -1 : entity.id;
			this.portraitLocation = portraitLocation;
		}

		public CutsceneDialogScreen(String text, boolean translate, int emotion, CutsceneEntity entity, DialogPortraitLocation portraitLocation)
		{
			this(new Message(text, translate), emotion, entity, portraitLocation);
		}

		void addReplacements(CutscenePokemon speaker)
		{
			this.hasReplacements = true;
			this.message.addReplacement("<account-name>", Persistance.player.name());
			this.message.addReplacement("<player-name>", Persistance.player.getTeamLeader().getNickname());
			this.message.addReplacement("<player-type>", Persistance.player.getTeamLeader().species().formName());
			if (Persistance.player.allies.size() >= 1)
			{
				this.message.addReplacement("<partner-name>", Persistance.player.getMember(1).getNickname());
				this.message.addReplacement("<partner-type>", Persistance.player.getMember(1).species().formName());
			}
			if (speaker != null)
			{
				this.message.addReplacement("<speaker-name>", speaker.toPokemon().getNickname());
				this.message.addReplacement("<speaker-type>", speaker.toPokemon().species().formName());
			}
		}

		@Override
		public String toString()
		{
			return this.message.toString();
		}

		public Element toXML(String elementName)
		{
			Element root = new Element(elementName).setText(this.message.id);
			XMLUtils.setAttribute(root, "translate", this.message.shouldTranslate, true);
			XMLUtils.setAttribute(root, "emotion", this.emotion, -1);
			XMLUtils.setAttribute(root, "target", this.pokemon, -1);
			XMLUtils.setAttribute(root, "portrait-location", this.portraitLocation.name(), DialogPortraitLocation.BOTTOM_LEFT.name());
			return root;
		}
	}

	public final boolean isNarratorDialog;
	private boolean isOver;
	public List<CutsceneDialogScreen> screens;

	public DialogCutsceneEvent(Element xml, CutsceneContext context)
	{
		super(xml, CutsceneEventType.dialog, context);
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
		this.isOver = false;
		for (CutsceneDialogScreen s : this.screens)
		{
			CutscenePokemon pokemon = null;
			CutsceneEntity e = this.context.parent().player.getEntity(s.pokemon);
			if (e != null && e instanceof CutscenePokemon) pokemon = (CutscenePokemon) e;
			if (!s.hasReplacements) s.addReplacements(pokemon);
			DialogScreen screen = pokemon == null ? new DialogScreen(s.message) : new PokemonDialogScreen(pokemon.toPokemon(), s.message, s.portraitLocation);
			if (this.isNarratorDialog)
			{
				screen = new NarratorDialogScreen(s.message);
				((NarratorDialogScreen) screen).forceBlackBackground = false;
			}
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
			root.addContent(screen.toXML("dialogscreen"));
		return root;
	}

}
