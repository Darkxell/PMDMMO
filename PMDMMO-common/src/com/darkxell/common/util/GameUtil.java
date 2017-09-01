package com.darkxell.common.util;

import java.awt.Point;

import javafx.util.Pair;

/** Contains various constants and methods for the Game. */
public class GameUtil
{

	/** Cardinal directions.<br />
	 * <ul>
	 * <li>NORTH = 0</li>
	 * <li>NORTHEAST = 1</li>
	 * <li>EAST = 2</li>
	 * <li>SOUTHEAST = 3</li>
	 * <li>SOUTH = 4</li>
	 * <li>SOUTHWEST = 5</li>
	 * <li>WEST = 6</li>
	 * <li>NORTHWEST = 7</li>
	 * </ul> */
	public static final short NORTH = 1, NORTHEAST = 2, EAST = 4, SOUTHEAST = 8, SOUTH = 16, SOUTHWEST = 32, WEST = 64, NORTHWEST = 128;

	/** Lists directions. */
	private static short[] directions = new short[]
	{ NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, NORTHWEST };

	/** Removes corner directions that are not connected to both adjacent directions. */
	public static short clean(short neighbors)
	{
		if (containsDirection(neighbors, NORTHEAST) && !(containsDirection(neighbors, NORTH) && containsDirection(neighbors, EAST))) neighbors -= NORTHEAST;
		if (containsDirection(neighbors, NORTHWEST) && !(containsDirection(neighbors, NORTH) && containsDirection(neighbors, WEST))) neighbors -= NORTHWEST;
		if (containsDirection(neighbors, SOUTHEAST) && !(containsDirection(neighbors, SOUTH) && containsDirection(neighbors, EAST))) neighbors -= SOUTHEAST;
		if (containsDirection(neighbors, SOUTHWEST) && !(containsDirection(neighbors, SOUTH) && containsDirection(neighbors, WEST))) neighbors -= SOUTHWEST;
		return neighbors;
	}

	/** @return True if the input direction sum contains the input direction. */
	public static boolean containsDirection(short directionSum, short direction)
	{
		for (int i = directions.length - 1; i >= 0; --i)
		{
			if (directions[i] == direction) return directionSum >= directions[i];
			if (directionSum >= directions[i]) directionSum -= directions[i];
		}
		return directionSum == direction;
	}

	/** @return The list of directions, starting north and going clockwise. */
	public static short[] directions()
	{
		return directions.clone();
	}

	public static int indexOf(short direction)
	{
		for (int i = 0; i < directions.length; ++i)
			if (directions[i] == direction) return i;
		return 0;
	}

	/** @return True if the input direction is diagonal. */
	public static boolean isDiagonal(short direction)
	{
		return direction == NORTHEAST || direction == SOUTHEAST || direction == SOUTHWEST || direction == NORTHWEST;
	}

	/** @return The coordinates given when moving from the input X,Y coordinates along the input direction. */
	public static Point moveTo(int x, int y, short direction)
	{
		// Move X
		switch (direction)
		{
			case WEST:
			case NORTHWEST:
			case SOUTHWEST:
				--x;
				break;

			case EAST:
			case NORTHEAST:
			case SOUTHEAST:
				++x;
				break;

			default:
				break;
		}

		// Move Y
		switch (direction)
		{
			case NORTH:
			case NORTHEAST:
			case NORTHWEST:
				--y;
				break;

			case SOUTH:
			case SOUTHEAST:
			case SOUTHWEST:
				++y;
				break;

			default:
				break;
		}

		return new Point(x, y);
	}

	/** @return The opposite of the input direction. */
	public static short oppositeOf(short direction)
	{
		int d = indexOf(direction);
		d += 4;
		if (d > 7) d -= 8;
		return directions[d];
	}

	/** @return A random direction. */
	public static short randomDirection()
	{
		return directions[(int) (Math.random() * 8)];
	}

	/** @return The next direction after rotating clockwise from the input direction. */
	public static short rotateClockwise(short direction)
	{
		int d = indexOf(direction);
		d += 1;
		if (d > 7) d = 0;
		return directions[d];
	}

	/** @return The orthogonal directions forming the input diagonal. */
	public static Pair<Short, Short> splitDiagonal(short direction)
	{
		switch (direction)
		{
			case SOUTHEAST:
				return new Pair<Short, Short>(SOUTH, EAST);
			case SOUTHWEST:
				return new Pair<Short, Short>(SOUTH, WEST);
			case NORTHWEST:
				return new Pair<Short, Short>(NORTH, WEST);
			default:
				return new Pair<Short, Short>(NORTH, EAST);
		}
	}

}
