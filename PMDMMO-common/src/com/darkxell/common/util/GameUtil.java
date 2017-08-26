package com.darkxell.common.util;

import java.awt.Point;

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
	public static final byte NORTH = 0, NORTHEAST = 1, EAST = 2, SOUTHEAST = 3, SOUTH = 4, SOUTHWEST = 5, WEST = 6, NORTHWEST = 7;

	/** @return The coordinates given when moving from the input X,Y coordinates along the input direction. */
	public static Point moveTo(int x, int y, byte direction)
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
	public static byte oppositeOf(byte direction)
	{
		direction += 4;
		if (direction > 7) direction -= 8;
		return direction;
	}

	/** @return A random direction. */
	public static byte randomDirection()
	{
		return (byte) (Math.random() * 8);
	}

}
