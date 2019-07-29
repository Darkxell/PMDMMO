package com.darkxell.client.resources.image.pokemon.portrait;

import java.awt.image.BufferedImage;

import com.darkxell.common.pokemon.PokemonSpecies;

public class PortraitSpriteset extends AbstractPortraitSpriteset {

    public final int startingIndex;

    PortraitSpriteset(String path, int startingIndex, int lines) {
        super(path, 10, lines);
        this.startingIndex = startingIndex;
    }

    @Override
    BufferedImage getPortrait(PokemonSpecies pokemon, PortraitEmotion emotion) {
        return this.getSprite(pokemon.id - this.startingIndex);
    }
}
