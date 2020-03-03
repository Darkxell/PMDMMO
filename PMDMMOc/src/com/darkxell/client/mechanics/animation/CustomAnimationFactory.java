package com.darkxell.client.mechanics.animation;

import com.darkxell.client.mechanics.animation.misc.EarthquakeAnimation;
import com.darkxell.client.mechanics.animation.misc.ImmobilizedAnimation;
import com.darkxell.client.mechanics.animation.misc.WindAnimation;
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

        if (animationData.getCustomAnimation().startsWith("wind=")) {
            int particleID = 0, particleCount = 6;
            String[] windData = animationData.getCustomAnimation().substring("wind=".length()).split(",");
            particleID = Integer.valueOf(windData[0]);
            if (windData.length > 1)
                particleCount = Integer.valueOf(windData[1]);
            return new WindAnimation(particleID, particleCount, animationData, renderer, listener);
        }

        return null;
    }

}
