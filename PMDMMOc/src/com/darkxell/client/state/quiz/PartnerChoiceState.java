package com.darkxell.client.state.quiz;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.resources.images.pokemon.PokemonPortrait;
import com.darkxell.client.state.dialog.OptionDialogState;
import com.darkxell.client.state.menu.OptionState;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.pokemon.PokemonSpecies;
import com.darkxell.common.util.language.Message;

public class PartnerChoiceState extends OptionDialogState
{

	static class PartnerOptionState extends OptionState
	{

		public PartnerOptionState(PartnerChoiceState parent, boolean isOpaque)
		{
			super(parent, isOpaque);
		}

		@Override
		protected Rectangle mainWindowDimensions()
		{
			PartnerChoiceState s = (PartnerChoiceState) this.backgroundState;
			Rectangle r = super.mainWindowDimensions();
			return new Rectangle((int) s.dialogBox().getMinX(), r.y, r.width, r.height);
		}

	}

	private OptionState currentOptionState;
	public final PersonalityQuizState parent;
	private final Pokemon[] partners;

	public PartnerChoiceState(PersonalityQuizState parent)
	{
		super(parent, parent, true, new DialogScreen(new Message("quiz.choose_partner")), parent.partnersAsOptions());
		this.parent = parent;
		PokemonSpecies[] s = this.parent.partners();
		this.partners = new Pokemon[s.length];
		for (int i = 0; i < this.partners.length; ++i)
			this.partners[i] = s[i].generate(new Random(), 1);
	}

	@Override
	public void nextMessage()
	{
		if (this.currentScreen == this.screens.size() - 1 && !this.showingOptions)
		{
			this.showingOptions = true;
			Persistance.stateManager.setState(this.currentOptionState = new PartnerOptionState(this, this.isOpaque));
		} else super.nextMessage();
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		super.render(g, width, height);
		if (this.currentOptionState != null) PokemonPortrait.drawPortrait(g, this.partners[this.currentOptionState.optionIndex()], width / 2,
				(int) (height / 2 - this.dialogBox().getHeight() / 2));
	}

}
