package com.darkxell.client.state.quiz;

import java.awt.Graphics2D;

import com.darkxell.client.resources.images.pokemon.PokemonPortrait;
import com.darkxell.client.state.dialog.DialogScreen;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.language.Message;

public class StarterScreen extends DialogScreen
{

	public final Pokemon pokemon;

	public StarterScreen(Message message, Pokemon pokemon)
	{
		super(message);
		this.pokemon = pokemon;
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		super.render(g, width, height);
		PokemonPortrait.drawPortrait(g, this.pokemon, width / 2 - PokemonPortrait.PORTRAIT_SIZE / 2, height / 2 - PokemonPortrait.PORTRAIT_SIZE / 2);
	}

}
