package com.darkxell.client.mechanics.animation.movement;

import java.awt.geom.Point2D;

import com.darkxell.client.mechanics.animation.PokemonAnimation;
import com.darkxell.client.mechanics.animation.travel.TravelAnimation;

public class LongTackleAnimationMovement extends PokemonAnimationMovement {
    public static final int TOTAL = 30, CHARGE = TOTAL / 3, MOVEMENT = TOTAL / 6;

    protected TravelAnimation travel;

    public LongTackleAnimationMovement(PokemonAnimation animation) {
        super(animation, TOTAL);
        this.travel = this.createTravel();
    }

    protected TravelAnimation createTravel() {
        return new TravelAnimation(this.renderer.sprite().getFacingDirection().move(new Point2D.Double(0, 0)));
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
        float completion = this.tick() * 1f;
        if (this.tick() >= CHARGE && this.tick() <= CHARGE + MOVEMENT)
            completion -= CHARGE;
        else if (this.tick() >= CHARGE * 2 + MOVEMENT && this.tick() <= TOTAL)
            completion = 30 - completion;
        else
            completion = -1;

        if (completion != -1 && !this.isOver()) {
            this.travel.update(completion * 0.75f / MOVEMENT);
        }
    }
}
