package com.darkxell.common.ai;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Directions;

import javafx.util.Pair;

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

	/** @return The direction to go to for the input pokemon to reach the target. May return -1 if there is no path. */
	public static short direction(Floor floor, DungeonPokemon pokemon, DungeonPokemon target)
	{
		short direction = generalDirection(pokemon, target);
		if (pokemon.tile().adjacentTile(direction).canMoveTo(pokemon, direction, false)) return direction;

		if (Directions.isDiagonal(direction))
		{
			short cardinal = generalCardinalDirection(pokemon, target);
			if (pokemon.tile().adjacentTile(cardinal).canMoveTo(pokemon, cardinal, false)) return cardinal;

			Pair<Short, Short> split = Directions.splitDiagonal(direction);
			short other;
			if (cardinal == split.getKey()) other = split.getValue();
			else other = split.getKey();
			if (pokemon.tile().adjacentTile(other).canMoveTo(pokemon, other, false)) return other;
		} else
		{
			int distance = pokemon.tile().distance(target.tile());
			if (pokemon.tile().adjacentTile(Directions.rotateClockwise(direction)).distance(target.tile()) < distance
					&& pokemon.tile().adjacentTile(Directions.rotateClockwise(direction)).canMoveTo(pokemon, direction, false))
				return Directions.rotateClockwise(direction);
			if (pokemon.tile().adjacentTile(Directions.rotateCounterClockwise(direction)).distance(target.tile()) < distance
					&& pokemon.tile().adjacentTile(Directions.rotateCounterClockwise(direction)).canMoveTo(pokemon, direction, false))
				return Directions.rotateCounterClockwise(direction);
		}
		return -1;
	}

	/** @return The general cardinal direction the input Pokémon has to face to look at the target. */
	public static short generalCardinalDirection(DungeonPokemon pokemon, DungeonPokemon target)
	{
		double angle = Math.toDegrees(Math.atan2(target.tile().x - pokemon.tile().x, target.tile().y - pokemon.tile().y));
		if (angle < 0) angle += 360;
		if (angle < 45 || angle > 315) return Directions.SOUTH;
		if (angle < 135) return Directions.EAST;
		if (angle < 225) return Directions.NORTH;
		return Directions.WEST;
	}

	/** @return The general direction the input Pokémon has to face to look at the target. */
	public static short generalDirection(DungeonPokemon pokemon, DungeonPokemon target)
	{
		/* if (pokemon == null) System.out.println("pokemon"); if (target == null) System.out.println("target"); if (pokemon.tile() == null) System.out.println(pokemon); if (target.tile == null) System.out.println(target); */
		double angle = Math.toDegrees(Math.atan2(target.tile().x - pokemon.tile().x, target.tile().y - pokemon.tile().y));
		if (angle < 0) angle += 360;
		return closestDirection(angle);
	}

	/** @return True if the input Pokémon can see any enemy Pokémon. <br>
	 *         This method avoids calling visibleEnemies when there is none, which is heavier than this one. */
	public static boolean hasVisibleEnemies(Floor floor, DungeonPokemon pokemon)
	{
		if (pokemon.tile() == null) System.out.println(pokemon);
		// Change this to use floor shadows when exploring AI is done
		for (int x = pokemon.tile().x - 3; x <= pokemon.tile().x + 3; ++x)
			for (int y = pokemon.tile().y - 3; y <= pokemon.tile().y + 3; ++y)
				if (floor.tileAt(x, y).getPokemon() != null && !floor.tileAt(x, y).getPokemon().pokemon.isAlliedWith(pokemon.pokemon)) return true;

		if (pokemon.tile().isInRoom())
		{
			for (Tile t : floor.room(pokemon.tile()).listTiles())
				if (t.getPokemon() != null && !t.getPokemon().pokemon.isAlliedWith(pokemon.pokemon)) return true;
			for (Tile t : floor.room(pokemon.tile()).outline())
				if (t.getPokemon() != null && !t.getPokemon().pokemon.isAlliedWith(pokemon.pokemon)) return true;
		}

		return false;
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
		if (pokemon.tile().adjacentTile(direction).getPokemon() != target) return false; // Adjacent test
		else if (!checkBlockingWalls) return true;
		return !pokemon.tile().blockingWalls(pokemon, direction); // Blocking walls test
	}

	public static boolean isVisible(Floor floor, DungeonPokemon pokemon, DungeonPokemon target)
	{
		if (pokemon.tile().isInRoom())
		{
			if (target.tile().isInRoom()) return floor.room(pokemon.tile()) == floor.room(target.tile());
			for (Tile t : floor.room(pokemon.tile()).outline())
				if (t == target.tile()) return true;
		}

		// Change this to use floor shadows when exploring AI is done
		return pokemon.tile().distance(target.tile()) <= 3;
	}

	/** @return The list of enemy Pokémon the input Pokémon can see, sorted by distance to the Pokémon. */
	public static ArrayList<DungeonPokemon> visibleEnemies(Floor floor, DungeonPokemon pokemon)
	{
		ArrayList<DungeonPokemon> visible = new ArrayList<>();
		if (pokemon.tile().isInRoom())
		{
			for (Tile t : floor.room(pokemon.tile()).listTiles())
				if (t.getPokemon() != null) visible.add(t.getPokemon());
			for (Tile t : floor.room(pokemon.tile()).outline())
				if (t.getPokemon() != null) visible.add(t.getPokemon());
		}

		// Change this to use floor shadows when exploring AI is done
		for (int x = pokemon.tile().x - 3; x <= pokemon.tile().x + 3; ++x)
			for (int y = pokemon.tile().y - 3; y <= pokemon.tile().y + 3; ++y)
				if (floor.tileAt(x, y).getPokemon() != null) visible.add(floor.tileAt(x, y).getPokemon());

		visible.removeIf((DungeonPokemon p) -> {
			return pokemon.pokemon.isAlliedWith(p.pokemon);
		});
		visible.sort((DungeonPokemon p1, DungeonPokemon p2) -> {
			return Integer.compare(pokemon.tile().distance(p1.tile()), pokemon.tile().distance(p2.tile()));
		});
		return visible;
	}

	private AIUtils()
	{}

}
