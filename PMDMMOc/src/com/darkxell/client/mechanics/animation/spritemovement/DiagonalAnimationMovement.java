package com.darkxell.client.mechanics.animation.spritemovement;

import java.awt.geom.Point2D;

import com.darkxell.client.mechanics.animation.SpritesetAnimation;
import com.darkxell.client.mechanics.animation.travel.TravelAnimation;
import com.darkxell.client.resources.images.tilesets.AbstractDungeonTileset;

public class DiagonalAnimationMovement extends SpritesetAnimationMovement {

    private TravelAnimation travel;

    public DiagonalAnimationMovement(SpritesetAnimation animation) {
        super(animation);
    }

    @Override
    public void start() {
        super.start();
        Point2D origin = new Point2D.Double(-1, -1);
        Point2D destination = new Point2D.Double(1, 1);
        this.travel = new TravelAnimation(origin, destination);
    }

    @Override
    public void update() {
        this.travel.update(this.completion());
        this.setSpriteLocation((int) (this.travel.current().getX() * AbstractDungeonTileset.TILE_SIZE),
                (int) (this.travel.current().getY() * AbstractDungeonTileset.TILE_SIZE));
    }

}
