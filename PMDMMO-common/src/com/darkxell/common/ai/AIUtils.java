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

	/** @return True if the input pokemon is just one tile away from the target. If they're diagonally disposed, also checks if there is a wall blocking their interaction. */
	public static boolean isAdjacentTo(DungeonPokemon pokemon, DungeonPokemon target)
	{
		return isAdjacentTo(pokemon, target, true);
	}

	/** @param checkBlockingWalls - If true and the pokemon are diagonally disposed, also checks if there is a wall blocking their interaction.
	 * @return True if the input pokemon is just one tile away from the target. */
	public static boolean isAdjacentTo(DungeonPokemon pokemon, DungeonPokemon target, boolean checkBlockingWalls)
	{
		short direction = generalDirection(pokemon, target);
		if (pokemon.tile.adjacentTile(direction).getPokemon() != target) return false; // Adjacent test
		else if (!checkBlockingWalls) return true;
		return pokemon.tile.blockingWalls(pokemon, direction); // Blocking walls test
	}

	private AIUtils()
	{}

}
