package com.darkxell.client.mechanics.freezones.entities;

import com.darkxell.client.resources.images.pokemon.PokemonSprite;
import com.darkxell.client.resources.images.pokemon.PokemonSpritesets;
import com.darkxell.common.pokemon.Pokemon;

public class FriendPokemonEntity extends PokemonFreezoneEntity {

    public final Pokemon pokemon;

    public FriendPokemonEntity(Pokemon pokemon) {
        super(0, 0, new PokemonSprite(PokemonSpritesets.getSpriteset(pokemon.species().id)));
        this.pokemon = pokemon;
    }

}
