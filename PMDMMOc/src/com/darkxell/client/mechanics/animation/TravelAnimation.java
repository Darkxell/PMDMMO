package com.darkxell.client.mechanics.animation;

import java.awt.geom.Point2D;

/** Represents the travel of from a Point to another. */
public class TravelAnimation
{

	private Point2D current;
	public final Point2D origin, destination, distance;

	public TravelAnimation(Point2D origin, Point2D destination)
	{
		this.origin = origin;
		this.destination = destination;
		this.distance = new Point2D.Double(this.destination.getX() - this.origin.getX(), this.destination.getY() - this.origin.getY());
		this.current = new Point2D.Double(this.origin.getX(), this.origin.getY());
	}

	public final Point2D current()
	{
		return (Point2D) this.current.clone();
	}

	/** Updates this Animation.
	 * 
	 * @param completion- The completion of this Animation, between 0 and 1. */
	public void update(double completion)
	{
		if (completion < 0 || completion > 1) return;
		this.current = new Point2D.Double(this.origin.getX() + this.distance.getX() * completion, this.origin.getY() + this.distance.getY() * completion);
	}

}
