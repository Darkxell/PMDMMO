package com.darkxell.client.resources.images.pokemons;

import com.darkxell.client.resources.images.AbstractPokemonSpriteset;

/** Bulbazaur's spriteset. */
public class PokemonSpriteset1 extends AbstractPokemonSpriteset {

	public static PokemonSpriteset1 instance = new PokemonSpriteset1();
	
	private PokemonSpriteset1() {
		super("/pokemons/pkmn1.png", 12, 16, 25, 25, new int[] { 7, 7, 200 }, 3, 3, 2, 2, true, 6, 2);
	}

}
