package com.darkxell.client.mechanics.animation;

import java.awt.Point;
import java.awt.geom.Point2D;

import com.darkxell.common.pokemon.DungeonPokemon;

/** Represents the travel of from a Point to another. */
public class TravelAnimation
{

	private Point2D current;
	public final short direction;
	public final Point origin, arrival, distance;

	public TravelAnimation(DungeonPokemon pokemon, short direction)
	{
		this.direction = direction;
		this.origin = pokemon.tile.location();
		this.arrival = pokemon.tile.adjacentTile(direction).location();
		this.distance = new Point(this.arrival.x - this.origin.x, this.arrival.y - this.origin.y);
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
