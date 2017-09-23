package com.darkxell.client.mechanics.animation;

import java.awt.Point;
import java.awt.geom.Point2D;

/** Represents the travel of from a Point to another. */
public class TravelAnimation
{

	private Point2D current;
	public final Point origin, destination, distance;

	public TravelAnimation(Point origin, Point destination)
	{
		this.origin = origin;
		this.destination = destination;
		this.distance = new Point(this.destination.x - this.origin.x, this.destination.y - this.origin.y);
		this.current = new Point2D.Float(this.origin.x, this.origin.y);
	}

	public final Point2D current()
	{
		return (Point2D) this.current.clone();
	}

	/** Updates this Animation.
	 * 
	 * @param completion- The completion of this Animation, between 0 and 1. */
	public void update(float completion)
	{
		if (completion < 0 || completion > 1) return;
		this.current = new Point2D.Float(this.origin.x + this.distance.x * completion, this.origin.y + this.distance.y * completion);
	}

}
