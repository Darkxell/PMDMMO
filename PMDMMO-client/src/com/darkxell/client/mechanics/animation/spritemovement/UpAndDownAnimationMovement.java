package com.darkxell.client.mechanics.animation.spritemovement;

import java.awt.geom.Point2D;

import com.darkxell.client.mechanics.animation.SpritesetAnimation;
import com.darkxell.client.mechanics.animation.travel.TravelAnimation;
import com.darkxell.client.resources.image.tileset.dungeon.AbstractDungeonTileset;

public class UpAndDownAnimationMovement extends SpritesetAnimationMovement {

    private TravelAnimation travel;

    public UpAndDownAnimationMovement(SpritesetAnimation animation) {
        super(animation);
    }

    @Override
    public void start() {
        super.start();
        Point2D origin = new Point2D.Double(0, 0);
        Point2D destination = new Point2D.Double(0, -1.5);
        this.travel = new TravelAnimation(origin, destination);
    }

    @Override
    public void update() {
        double completion = this.completion() * 2;
        if (completion > 1)
            completion = 2 - completion;

        this.travel.update(completion);
        this.setSpriteLocation((int) (this.travel.current().getX() * AbstractDungeonTileset.TILE_SIZE),
                (int) (this.travel.current().getY() * AbstractDungeonTileset.TILE_SIZE));
    }

}
