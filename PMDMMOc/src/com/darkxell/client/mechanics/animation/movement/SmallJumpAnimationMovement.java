package com.darkxell.client.mechanics.animation.movement;

import java.awt.geom.Point2D;

import com.darkxell.client.mechanics.animation.PokemonAnimation;
import com.darkxell.client.mechanics.animation.travel.TravelAnimation;

public class SmallJumpAnimationMovement extends PokemonAnimationMovement {
    public static final int TOTAL = 10, PAUSE = 2, MOVEMENT = 4;

    protected TravelAnimation travel;

    public SmallJumpAnimationMovement(PokemonAnimation animation) {
        super(animation, TOTAL);
        this.travel = this.createTravel();
    }

    protected TravelAnimation createTravel() {
        return new TravelAnimation(new Point2D.Double(0, -.3));
    }

    @Override
    public void onFinish() {
        super.onFinish();
        if (this.renderer != null)
            this.renderer.unregisterOffset(this.travel);
    }

    @Override
    public void start() {
        super.start();
        if (this.renderer != null)
            this.renderer.registerOffset(this.travel);
    }

    @Override
    public void update() {
        float completion = this.tick();
        if (this.tick() >= PAUSE + MOVEMENT && this.tick() <= TOTAL)
            completion = TOTAL - completion;
        else if (this.tick() > MOVEMENT)
            completion = -1;

        if (completion != -1 && !this.isOver()) {
            this.travel.update(completion / MOVEMENT);
        }
    }
}
