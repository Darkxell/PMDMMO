package com.darkxell.client.resources.image.pokemon.portrait;

import java.awt.image.BufferedImage;

import com.darkxell.common.pokemon.PokemonSpecies;

public class EmotionPortraitsSpriteset extends AbstractPortraitSpriteset {

    EmotionPortraitsSpriteset(String path) {
        super(path, 15, 45);
    }

    @Override
    BufferedImage getPortrait(PokemonSpecies pokemon, PortraitEmotion emotion) {
        return this.getSprite(emotion.index, Portraits.emotionPokemons.indexOf(pokemon.getID()));
    }

}
