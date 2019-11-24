package com.darkxell.client.mechanics.animation;

import com.darkxell.client.mechanics.animation.misc.EarthquakeAnimation;
import com.darkxell.client.renderers.pokemon.AbstractPokemonRenderer;

public class CustomAnimationFactory {

    public static PokemonAnimation load(AnimationData animationData, AbstractPokemonRenderer renderer,
            AnimationEndListener listener) {
        
        switch (animationData.customAnimation) {
        case "Earthquake":
            return new EarthquakeAnimation(animationData, renderer, listener);
        }
        
        return null;
    }

}
