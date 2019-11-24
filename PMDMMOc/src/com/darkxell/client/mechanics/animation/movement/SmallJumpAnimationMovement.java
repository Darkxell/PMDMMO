package com.darkxell.client.mechanics.animation.movement;

import java.awt.geom.Point2D;

import com.darkxell.client.mechanics.animation.PokemonAnimation;
import com.darkxell.client.mechanics.animation.travel.TravelAnimation;
import com.darkxell.client.resources.image.tileset.dungeon.AbstractDungeonTileset;

public class SmallJumpAnimationMovement extends PokemonAnimationMovement {

    protected TravelAnimation travel;

    public SmallJumpAnimationMovement(PokemonAnimation animation) {
        this(animation, 10);
    }

    protected SmallJumpAnimationMovement(PokemonAnimation animation, int duration) {
        super(animation, duration);
        this.travel = this.createTravel();
    }

    protected TravelAnimation createTravel() {
        return new TravelAnimation(new Point2D.Double(0, this.jumpDistance() * AbstractDungeonTileset.TILE_SIZE));
    }

    protected double jumpDistance() {
        return -.3;
    }

    @Override
    public void onFinish() {
        super.onFinish();
        this.renderer.sprite().setXYOffset(0, 0);
    }

    @Override
    public void update() {
        float completion = this.tick();
        final int movement = this.duration * 2 / 5, pause = this.duration / 5;
        if (this.tick() >= pause + movement && this.tick() <= this.duration)
            completion = this.duration - completion;
        else if (this.tick() > movement)
            completion = -1;

        if (completion != -1 && !this.isOver()) {
            this.travel.update(completion * 1. / movement);
            this.renderer.sprite().setXYOffset((int) this.travel.current().getX(), (int) this.travel.current().getY());
        }
    }
}
