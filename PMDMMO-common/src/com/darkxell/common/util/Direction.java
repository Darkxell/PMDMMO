package com.darkxell.common.util;

import java.awt.Point;
import java.util.Comparator;
import java.util.Random;

import javafx.util.Pair;

/** Directions used in Dungeons. */
public enum Direction
{

	EAST((short) 4),
	NORTH((short) 1),
	NORTHEAST((short) 2),
	NORTHWEST((short) 128),
	SOUTH((short) 16),
	SOUTHEAST((short) 8),
	SOUTHWEST((short) 32),
	WEST((short) 64);

	/** List of orthogonal Directions. */
	public static final Direction[] cardinal = { NORTH, EAST, SOUTH, WEST };
	public static final Comparator<Direction> COMPARATOR = new Comparator<Direction>() {
		@Override
		public int compare(Direction d1, Direction d2)
		{
			return Short.compare(d1.value, d2.value);
		}
	};
	/** List of all Directions. */
	public static final Direction[] directions = { NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, NORTHWEST };
	/** Names of the Directions. */
	private static final String[] names = new String[] { "North", "Northeast", "East", "Southeast", "South", "Southeast", "West", "Northwest" };

	/** @return A random Direction. */
	public static Direction randomDirection(Random random)
	{
		return directions[random.nextInt(8)];
	}

	public final short value;

	private Direction(short value)
	{
		this.value = value;
	}

	/** @return True if this Direction equals the input Direction or if this is a diagonal Direction made with the input Direction. */
	public boolean contains(Direction direction)
	{
		if (direction == this) return true;
		if (direction == WEST && (this == NORTHWEST || this == SOUTHWEST)) return true;
		if (direction == EAST && (this == NORTHEAST || this == SOUTHEAST)) return true;
		if (direction == NORTH && (this == NORTHEAST || this == NORTHWEST)) return true;
		if (direction == SOUTH && (this == SOUTHEAST || this == SOUTHWEST)) return true;
		return false;
	}

	/** @return This Direction's name. */
	public String getName()
	{
		return names[this.index()];
	}

	/** @return This Direction's index in the Directions array. */
	public int index()
	{
		for (int i = 0; i < directions.length; ++i)
			if (directions[i] == this) return i;
		return 0;
	}

	/** @return True if this Direction is diagonal. */
	public boolean isDiagonal()
	{
		return this == NORTHEAST || this == SOUTHEAST || this == SOUTHWEST || this == NORTHWEST;
	}

	/** @return The coordinates given when moving from the input X,Y coordinates along this Direction. */
	public Point move(int x, int y)
	{
		if (this.contains(WEST)) --x;
		else if (this.contains(EAST)) ++x;

		if (this.contains(NORTH)) --y;
		else if (this.contains(SOUTH)) ++y;

		return new Point(x, y);
	}

	/** @return The coordinates given when moving from the input coordinates along this Direction. */
	public Point move(Point origin)
	{
		return this.move(origin.x, origin.y);
	}

	/** @return The Direction opposite to this Direction. */
	public Direction opposite()
	{
		int i = this.index();
		i += 4;
		if (i > 7) i -= 8;
		return directions[i];
	}

	/** @return The Direction corresponding to this Direction rotated 45° clockwise. */
	public Direction rotateClockwise()
	{
		int i = this.index();
		i += 1;
		if (i > 7) i = 0;
		return directions[i];
	}

	/** @return The Direction corresponding to this Direction rotated -45° clockwise. */
	public Direction rotateCounterClockwise()
	{
		int i = this.index();
		i -= 1;
		if (i < 0) i = 7;
		return directions[i];
	}

	/** @return The pair of Directions forming this diagonal Direction. */
	public Pair<Direction, Direction> splitDiagonal()
	{
		switch (this)
		{
			case SOUTHEAST:
				return new Pair<Direction, Direction>(SOUTH, EAST);
			case SOUTHWEST:
				return new Pair<Direction, Direction>(SOUTH, WEST);
			case NORTHWEST:
				return new Pair<Direction, Direction>(NORTH, WEST);
			default:
				return new Pair<Direction, Direction>(NORTH, EAST);
		}
	}

}
