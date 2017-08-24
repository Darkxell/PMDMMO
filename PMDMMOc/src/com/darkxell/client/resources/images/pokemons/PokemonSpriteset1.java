package com.darkxell.client.resources.images.pokemons;

import com.darkxell.client.resources.images.AbstractPokemonSpriteset;

/** Bulbazaur's spriteset. */
public class PokemonSpriteset1 extends AbstractPokemonSpriteset {

	public static PokemonSpriteset1 instance = new PokemonSpriteset1();
	
	private PokemonSpriteset1() {
		super("", 12, 19, 25, 25, new int[] { 20, 20, 100 }, 3, 3, 2, 2, true, 6, 2);
	}

}
