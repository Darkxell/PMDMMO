package com.darkxell.client.mechanics.animation.spritemovement;

import com.darkxell.client.mechanics.animation.SpritesetAnimation;

public class FogAnimationMovement extends SpritesetAnimationMovement {

    public FogAnimationMovement(SpritesetAnimation animation) {
        super(animation);
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void update() {
        this.parentAnimation.data.setGravityX(this.parentAnimation.data.getGravityX() - 1);
    }

}
