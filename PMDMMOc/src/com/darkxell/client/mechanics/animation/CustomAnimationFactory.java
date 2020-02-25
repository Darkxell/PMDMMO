package com.darkxell.client.mechanics.animation;

import com.darkxell.client.mechanics.animation.misc.EarthquakeAnimation;
import com.darkxell.client.mechanics.animation.misc.ImmobilizedAnimation;
import com.darkxell.client.model.animation.AnimationVariantModel;
import com.darkxell.client.renderers.pokemon.AbstractPokemonRenderer;

public class CustomAnimationFactory {

    public static PokemonAnimation load(AnimationVariantModel animationData, AbstractPokemonRenderer renderer,
            AnimationEndListener listener) {
        
        switch (animationData.getCustomAnimation()) {
        case "Earthquake":
            return new EarthquakeAnimation(animationData, renderer, listener);
        case "Immobilized":
            return new ImmobilizedAnimation(animationData, renderer, null);
        }
        
        return null;
    }

}
