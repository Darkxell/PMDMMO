package com.darkxell.client.mechanics.animation.misc;

import com.darkxell.client.mechanics.animation.AnimationData;
import com.darkxell.client.mechanics.animation.AnimationEndListener;
import com.darkxell.client.mechanics.animation.PokemonAnimation;
import com.darkxell.client.renderers.pokemon.AbstractPokemonRenderer;

public class EarthquakeAnimation extends PokemonAnimation {

    public static final int DURATION = 30;

    public EarthquakeAnimation(AnimationData data, AbstractPokemonRenderer renderer, AnimationEndListener listener) {
        super(data, renderer, 30, listener);
    }

    @Override
    public void onFinish() {
        super.onFinish();
    }
    
    @Override
    public void update() {
        super.update();
    }

}
