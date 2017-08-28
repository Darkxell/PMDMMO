package com.darkxell.client.mechanics.freezones.zones;

import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.client.mechanics.freezones.entities.PokemonFreezoneEntity;
import com.darkxell.client.resources.images.PokemonSprite;
import com.darkxell.client.resources.images.pokemons.PokemonSpriteset1;

public class PokemonSquareFreezone extends FreezoneMap {

	public PokemonSquareFreezone() {
		super("resources\\freezones\\square.xml");
		this.entities.add(new PokemonFreezoneEntity(71, 34, new PokemonSprite(PokemonSpriteset1.instance)));
	}

}
