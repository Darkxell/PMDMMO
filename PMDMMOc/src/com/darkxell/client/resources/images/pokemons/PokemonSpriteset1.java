package com.darkxell.client.resources.images.pokemons;

import com.darkxell.client.resources.images.AbstractPokemonSpriteset;

/** Bulbazaur's spriteset. */
public class PokemonSpriteset1 extends AbstractPokemonSpriteset {

	public static PokemonSpriteset1 instance = new PokemonSpriteset1();
	
	private PokemonSpriteset1() {
		super("/pokemons/pkmn1.png", 12, 15, 25, 25, new int[] { 6, 6, 70 }, 3, 3, 2, 2, true, 6, 2);
	}

}
