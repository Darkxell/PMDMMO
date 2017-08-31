package com.darkxell.client.resources.images.pokemons;

import com.darkxell.client.resources.images.AbstractPokemonSpriteset;

/** Wurmple's spriteset. */
public class PokemonSpriteset265 extends AbstractPokemonSpriteset
{

	public static PokemonSpriteset265 instance = new PokemonSpriteset265();

	private PokemonSpriteset265()
	{
		super("/pokemons/pkmn265.png", 16, 18, 32, new int[]
		{ 7, 7 }, 3, 3, 2, 2);
	}

}
