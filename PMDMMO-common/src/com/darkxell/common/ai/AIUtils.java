package com.darkxell.common.ai;

import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Directions;

public final class AIUtils
{

	/** @param angle - An angle in degrees.
	 * @return The direction closest to the input angle (0 = North). */
	public static short closestDirection(double angle)
	{
		angle = 360 - angle; // Trigonometric to clockwise
		angle += 180; // So that 0=south => 0=north
		angle += 22.5; // So that 23 goes to 45, meaning 23° will be Northeast.
		if (angle >= 360) angle -= 360;
		if (angle == 360) angle = 359.9;
		return Directions.directions()[(short) (angle / 45)];
	}

	/** @return The general direction the input Pokémon has to face to look at the target. */
	public static short generalDirection(DungeonPokemon pokemon, DungeonPokemon target)
	{
		double angle = Math.toDegrees(Math.atan2(target.tile.x - pokemon.tile.x, target.tile.y - pokemon.tile.y));
		if (angle < 0) angle += 360;
		return closestDirection(angle);
	}

	private AIUtils()
	{}

}
