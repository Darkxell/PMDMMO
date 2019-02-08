package com.darkxell.client.mechanics.animation.spritemovement;

import com.darkxell.client.mechanics.animation.SpritesetAnimation;

public abstract class SpritesetAnimationMovement {

    public static SpritesetAnimationMovement create(String id, SpritesetAnimation parent) {
        if (id == null)
            return null;
        switch (id) {
        case "1tilefacing":
            return new FacingTileAnimationMovement(parent);

        case "diagonal":
            return new DiagonalAnimationMovement(parent);

        case "upanddown":
            return new UpAndDownAnimationMovement(parent);

        default:
            return null;
        }
    }

    public final SpritesetAnimation parentAnimation;

    public SpritesetAnimationMovement(SpritesetAnimation animation) {
        this.parentAnimation = animation;
    }

    public float completion() {
        return this.parentAnimation.completion();
    }

    public int duration() {
        return this.parentAnimation.duration();
    }

    public boolean isOver() {
        return this.parentAnimation.isOver();
    }

    public void onFinish() {
    }

    /** Changes Spriteset gravity to match the input coordinates (Spriteset will be centered around these coords.) */
    protected void setSpriteLocation(int x, int y) {
        this.parentAnimation.data.gravityX = this.parentAnimation.spriteset.spriteWidth / 2 - x;
        this.parentAnimation.data.gravityY = this.parentAnimation.spriteset.spriteHeight / 2 - y;
    }

    public void start() {
    }

    public int tick() {
        return this.parentAnimation.tick();
    }

    public abstract void update();

}
