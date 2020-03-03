package com.darkxell.client.mechanics.animation.movement;

import com.darkxell.client.mechanics.animation.PokemonAnimation;

public class BigJumpAnimationMovement extends SmallJumpAnimationMovement {

    public BigJumpAnimationMovement(PokemonAnimation animation) {
        super(animation, 40);
    }

    @Override
    protected double jumpDistance() {
        return -1;
    }
    
    @Override
    public void update() {
        super.update();
    }

}
