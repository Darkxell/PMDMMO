package com.darkxell.client.persistance;

import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.client.mechanics.freezones.FreezonePlayer;
import com.darkxell.client.resources.images.PokemonSprite;
import com.darkxell.client.resources.images.PokemonSpritesets;

/**
 * Holds a static reference to the freezone map currently active. Can be null.
 */
public class FreezoneMapHolder {

	public static FreezoneMap currentmap;
	public static FreezonePlayer currentplayer = new FreezonePlayer(
			new PokemonSprite(PokemonSpritesets.getSpriteset(1)), 35, 28);
	// TODO : change the player's skin to fit the playing pokemon.
}
