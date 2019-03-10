package com.darkxell.client.mechanics.freezone.entity;

import com.darkxell.client.resources.images.pokemon.PokemonSprite;
import com.darkxell.client.resources.images.pokemon.PokemonSpritesets;
import com.darkxell.common.pokemon.Pokemon;

public class FriendPokemonEntity extends PokemonFreezoneEntity {

    public final Pokemon pokemon;

    public FriendPokemonEntity(Pokemon pokemon) {
        this.pokemon = pokemon;
        this.sprite = new PokemonSprite(PokemonSpritesets.getSpriteset(this.pokemon.species().id));
    }

}
