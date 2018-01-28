package com.darkxell.common.ai;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;
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
	public static short direction(DungeonPokemon pokemon, DungeonPokemon target)
	{
		return direction(pokemon, target.tile());
	}

	/** @return The direction to go to for the input pokemon to reach the destination. May return -1 if there is no path. */
	public static short direction(DungeonPokemon pokemon, Tile destination)
	{
		short direction = generalDirection(pokemon, destination);
		if (pokemon.tile().adjacentTile(direction).canMoveTo(pokemon, direction, false)) return direction;

		if (Directions.isDiagonal(direction))
		{
			short cardinal = generalCardinalDirection(pokemon, destination);
			if (pokemon.tile().adjacentTile(cardinal).canMoveTo(pokemon, cardinal, false)) return cardinal;

			Pair<Short, Short> split = Directions.splitDiagonal(direction);
			short other;
			if (cardinal == split.getKey()) other = split.getValue();
			else other = split.getKey();
			if (pokemon.tile().adjacentTile(other).canMoveTo(pokemon, other, false)) return other;
		} else
		{
			double distance = pokemon.tile().distance(destination);
			if (pokemon.tile().adjacentTile(Directions.rotateClockwise(direction)).distance(destination) < distance
					&& pokemon.tile().adjacentTile(Directions.rotateClockwise(direction)).canMoveTo(pokemon, direction, false))
				return Directions.rotateClockwise(direction);
			if (pokemon.tile().adjacentTile(Directions.rotateCounterClockwise(direction)).distance(destination) < distance
					&& pokemon.tile().adjacentTile(Directions.rotateCounterClockwise(direction)).canMoveTo(pokemon, direction, false))
				return Directions.rotateCounterClockwise(direction);
			if (pokemon.tile().adjacentTile(Directions.rotateClockwise(Directions.rotateClockwise(direction))).distance(destination) < distance
					&& pokemon.tile().adjacentTile(Directions.rotateClockwise(Directions.rotateClockwise(direction))).canMoveTo(pokemon, direction, false))
				return Directions.rotateClockwise(Directions.rotateClockwise(direction));
			if (pokemon.tile().adjacentTile(Directions.rotateCounterClockwise(Directions.rotateCounterClockwise(direction))).distance(destination) < distance
					&& pokemon.tile().adjacentTile(Directions.rotateCounterClockwise(Directions.rotateCounterClockwise(direction))).canMoveTo(pokemon,
							direction, false))
				return Directions.rotateCounterClockwise(Directions.rotateCounterClockwise(direction));
		}
		return -1;
	}

	/** @return The furthest Tiles that can be seen and walked on by the input Pokémon. This method condiers the Pokémon is not in a Room. */
	public static ArrayList<Tile> furthestWalkableTiles(Floor floor, DungeonPokemon pokemon)
	{
		int shadow = 3 - floor.data.shadows();
		double maxDistance = 0, distance;
		ArrayList<Tile> tiles = new ArrayList<>();
		for (int x = pokemon.tile().x - shadow; x <= pokemon.tile().x + shadow; ++x)
			for (int y = pokemon.tile().y - shadow; y <= pokemon.tile().y + shadow; ++y)
			{
				Tile t = floor.tileAt(x, y);
				distance = t.distance(pokemon.tile());
				if (distance >= maxDistance && t.canWalkOn(pokemon, false) && isReachable(floor, pokemon, t))
				{
					maxDistance = distance;
					tiles.add(t);
				}
			}

		final double maxD = maxDistance;
		tiles.removeIf((Tile t) -> {
			return t.distance(pokemon.tile()) < maxD;
		});

		return tiles;
	}

	/** @return The general cardinal direction the input Pokémon has to face to look at the target. */
	public static short generalCardinalDirection(DungeonPokemon pokemon, DungeonPokemon target)
	{
		return generalCardinalDirection(pokemon, target.tile());
	}

	/** @return The general cardinal direction the input Pokémon has to face to look at the destination. */
	public static short generalCardinalDirection(DungeonPokemon pokemon, Tile destination)
	{
		double angle = Math.toDegrees(Math.atan2(destination.x - pokemon.tile().x, destination.y - pokemon.tile().y));
		if (angle < 0) angle += 360;
		if (angle < 45 || angle > 315) return Directions.SOUTH;
		if (angle < 135) return Directions.EAST;
		if (angle < 225) return Directions.NORTH;
		return Directions.WEST;
	}

	/** @return The general direction the input Pokémon has to face to look at the target. */
	public static short generalDirection(DungeonPokemon pokemon, DungeonPokemon target)
	{
		return generalDirection(pokemon, target.tile());
	}

	/** @return The general direction the input Pokémon has to face to look at the destination. */
	public static short generalDirection(DungeonPokemon pokemon, Tile destination)
	{
		/* if (pokemon == null) System.out.println("pokemon"); if (target == null) System.out.println("target"); if (pokemon.tile() == null) System.out.println(pokemon); if (target.tile == null) System.out.println(target); */
		double angle = Math.toDegrees(Math.atan2(destination.x - pokemon.tile().x, destination.y - pokemon.tile().y));
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

	public static boolean isReachable(Floor floor, DungeonPokemon pokemon, Tile tile)
	{
		if (pokemon.tile() == tile) return true;

		ArrayList<Tile> visible = new ArrayList<>(visibleTiles(floor, pokemon));
		Stack<Tile> accessible = new Stack<>();
		HashSet<Tile> treated = new HashSet<>();
		accessible.add(pokemon.tile());

		while (!accessible.isEmpty())
		{
			Tile t = accessible.pop();
			for (short direction : Directions.directions())
			{
				Tile a = t.adjacentTile(direction);
				if (!treated.contains(a) && visible.contains(a) && a.canWalkOn(pokemon, false))
				{
					boolean ok = true;
					if (Directions.isDiagonal(direction))
					{
						Pair<Short, Short> others = Directions.splitDiagonal(direction);
						if (!t.adjacentTile(others.getKey()).canCross(pokemon) || !t.adjacentTile(others.getValue()).canCross(pokemon)) ok = false;
					}
					if (ok)
					{
						if (a == tile) return true;
						accessible.push(a);
						treated.add(a);
					}
				}
			}
		}

		return false;
	}

	public static boolean isVisible(Floor floor, DungeonPokemon pokemon, DungeonPokemon target)
	{
		if (target.isFainted()) return false;
		if (pokemon.tile().isInRoom())
		{
			if (target.tile().isInRoom()) return floor.room(pokemon.tile()) == floor.room(target.tile());
			for (Tile t : floor.room(pokemon.tile()).outline())
				if (t == target.tile()) return true;
		}

		// Change this to use floor shadows when exploring AI is done
		return pokemon.tile().distance(target.tile()) <= 3;
	}

	public static boolean shouldStopRunning(DungeonPokemon pokemon)
	{
		Tile tile = pokemon.tile();
		Tile previous = tile.adjacentTile(Directions.oppositeOf(pokemon.facing()));

		if (!pokemon.tryMoveTo(pokemon.facing(), false)) return true;
		if (tile.isInRoom() && !previous.isInRoom()) return true;
		if (tile.type() == TileType.STAIR || tile.trapRevealed || tile.getItem() != null) return true;
		int origin = 0, destination = 0;
		short facing = pokemon.facing();

		for (short dir : Directions.isDiagonal(facing)
				? new short[] { facing, Directions.rotateClockwise(facing), Directions.rotateClockwise(facing), Directions.rotateCounterClockwise(facing) }
				: new short[] { facing, Directions.rotateClockwise(facing), Directions.rotateClockwise(facing),
						Directions.rotateClockwise(Directions.rotateClockwise(facing)), Directions.rotateCounterClockwise(facing),
						Directions.rotateCounterClockwise(Directions.rotateCounterClockwise(facing)) })
		{
			Tile o = previous.adjacentTile(dir);
			Tile d = tile.adjacentTile(dir);
			if (!(Directions.isDiagonal(dir) && !o.isInRoom()) && o.type().canWalkOn(pokemon)) ++origin;
			if (!(Directions.isDiagonal(dir) && !d.isInRoom()) && d.type().canWalkOn(pokemon)) ++destination;
			if (d.type() == TileType.STAIR || d.trapRevealed || d.getItem() != null) return true;
		}

		return destination > origin;
	}

	/** @return The list of enemy Pokémon the input Pokémon can see, sorted by distance to the Pokémon. */
	public static ArrayList<DungeonPokemon> visibleEnemies(Floor floor, DungeonPokemon pokemon)
	{
		ArrayList<DungeonPokemon> visible = new ArrayList<>();
		ArrayList<Tile> tiles = visibleTiles(floor, pokemon);
		for (Tile t : tiles)
			if (t.getPokemon() != null && !pokemon.pokemon.isAlliedWith(t.getPokemon().pokemon)) visible.add(t.getPokemon());
		return visible;
	}

	public static ArrayList<Tile> visibleTiles(Floor floor, DungeonPokemon pokemon)
	{
		ArrayList<Tile> visible = new ArrayList<>();
		if (pokemon.tile().isInRoom())
		{
			visible.addAll(floor.room(pokemon.tile()).listTiles());
			visible.addAll(floor.room(pokemon.tile()).outline());
		}

		int visibility = 3 - floor.data.shadows();
		for (int x = pokemon.tile().x - visibility; x <= pokemon.tile().x + visibility; ++x)
			for (int y = pokemon.tile().y - visibility; y <= pokemon.tile().y + visibility; ++y)
				visible.add(floor.tileAt(x, y));

		visible.sort((Tile t1, Tile t2) -> {
			return Double.compare(pokemon.tile().distance(t1), pokemon.tile().distance(t2));
		});
		return visible;
	}

	private AIUtils()
	{}

}
