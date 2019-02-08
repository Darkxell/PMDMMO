package com.darkxell.client.mechanics.animation.travel;

import java.awt.geom.Point2D;

import com.darkxell.common.dungeon.floor.Tile;

public class ArcAnimation extends TravelAnimation {

    public ArcAnimation(Tile origin, Tile destination) {
        super(origin, destination);
    }

    @Override
    public void update(double completion) {
        super.update(completion);
        this.current = new Point2D.Double(this.current.getX(),
                this.current.getY() - Math.sin(completion * Math.PI) * this.distance() / 2);
    }

}
