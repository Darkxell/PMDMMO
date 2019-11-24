package com.darkxell.client.mechanics.animation.travel;

import java.awt.geom.Point2D;

import com.darkxell.client.renderers.RenderOffset;
import com.darkxell.common.dungeon.floor.Tile;

/** Represents the travel of from a Point to another. */
public class TravelAnimation implements RenderOffset {

    protected Point2D current;
    public final Point2D origin, destination, distance;

    public TravelAnimation(Point2D destination) {
        this(new Point2D.Double(0, 0), destination);
    }

    public TravelAnimation(Point2D origin, Point2D destination) {
        this.origin = origin;
        this.destination = destination;
        this.distance = new Point2D.Double(this.destination.getX() - this.origin.getX(),
                this.destination.getY() - this.origin.getY());
        this.current = new Point2D.Double(this.origin.getX(), this.origin.getY());
    }

    public TravelAnimation(Tile origin, Tile destination) {
        this(new Point2D.Double(origin.x + .5, origin.y + .5),
                new Point2D.Double(destination.x + .5, destination.y + .5));
    }

    public final Point2D current() {
        return (Point2D) this.current.clone();
    }

    public double distance() {
        return this.distance.distance(0, 0);
    }

    /**
     * Updates this Animation.
     *
     * @param completion- The completion of this Animation, between 0 and 1.
     */
    public void update(double completion) {
        if (completion < 0 || completion > 1)
            return;
        this.current = new Point2D.Double(this.origin.getX() + this.distance.getX() * completion,
                this.origin.getY() + this.distance.getY() * completion);
    }

    @Override
    public double xOffset() {
        return this.current().getX();
    }

    @Override
    public double yOffset() {
        return this.current().getY();
    }

}
