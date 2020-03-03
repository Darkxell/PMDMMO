package com.darkxell.client.resources.image.pokemon.portrait;

import java.awt.image.BufferedImage;

import com.darkxell.client.resources.image.spritefactory.PMDRegularSpriteset;
import com.darkxell.common.pokemon.PokemonSpecies;

public abstract class AbstractPortraitSpriteset extends PMDRegularSpriteset {

    public static final int PORTRAIT_SIZE = 40;

    AbstractPortraitSpriteset(String path, int columns, int lines) {
        super(path, PORTRAIT_SIZE, PORTRAIT_SIZE, columns, lines);
    }

    abstract BufferedImage getPortrait(PokemonSpecies pokemon, PortraitEmotion emotion);
}
