package com.darkxell.client.state.quiz;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;

import com.darkxell.client.resources.images.pokemon.PokemonPortrait;
import com.darkxell.client.state.dialog.DialogState;
import com.darkxell.client.state.dialog.OptionDialogScreen;
import com.darkxell.client.state.menu.OptionState;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.pokemon.PokemonSpecies;
import com.darkxell.common.util.language.Message;

public class PartnerChoiceScreen extends OptionDialogScreen
{

	static class PartnerOptionState extends OptionState
	{

		public PartnerOptionState(DialogState parent, PartnerChoiceScreen screen, boolean isOpaque)
		{
			super(parent, screen, isOpaque);
		}

		@Override
		protected Rectangle mainWindowDimensions()
		{
			Rectangle r = super.mainWindowDimensions();
			return new Rectangle((int) ((DialogState) this.background).dialogBox().getMinX(), r.y, r.width, r.height);
		}

	}

	private OptionState currentOptionState;
	private final Pokemon[] partners;

	public PartnerChoiceScreen(PersonalityQuizDialog parent)
	{
		super(new Message("quiz.choose_partner"), parent.partnersAsOptions());
		PokemonSpecies[] s = parent.partners();
		this.partners = new Pokemon[s.length];
		for (int i = 0; i < this.partners.length; ++i)
			this.partners[i] = s[i].generate(new Random(), 1);
	}

	@Override
	protected OptionState getOptionSelectionState()
	{
		return this.currentOptionState = new PartnerOptionState(this.parentState(), this, this.isOpaque);
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		super.render(g, width, height);
		if (this.currentOptionState != null) PokemonPortrait.drawPortrait(g, this.partners[this.currentOptionState.optionIndex()], width / 2,
				(int) (height / 2 - this.parentState().dialogBox().getHeight() / 2));
	}

}
