package com.darkxell.common.util;

import static com.darkxell.common.util.Direction.*;

import java.util.ArrayList;

public class DirectionSet
{

	private short value;

	public DirectionSet()
	{
		this(new Direction[0]);
	}

	public DirectionSet(Direction... directions)
	{
		this.value = 0;
		for (Direction d : directions)
			this.add(d);
	}

	public DirectionSet(DirectionSet set)
	{
		this.value = set.value;
	}

	/** Adds the input Direction to this set. */
	public void add(Direction direction)
	{
		if (!this.contains(direction)) this.value += direction.value;
	}

	/** @return The list of Directions contained in this set. */
	public Direction[] asArray()
	{
		ArrayList<Direction> array = new ArrayList<Direction>();
		for (Direction dir : directions)
			if (this.contains(dir)) array.add(dir);
		array.sort(COMPARATOR);
		return array.toArray(new Direction[array.size()]);
	}

	public void clear()
	{
		this.value = 0;
	}

	/** @return True if this set contains the input Direction. */
	public boolean contains(Direction direction)
	{
		short v = this.value;
		for (int i = directions.length - 1; i >= 0; --i)
		{
			if (directions[i] == direction) return v >= directions[i].value;
			if (v >= directions[i].value) v -= directions[i].value;
		}
		return v == direction.value;
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof DirectionSet && ((DirectionSet) obj).value == this.value;
	}

	@Override
	public int hashCode()
	{
		return this.value;
	}

	/** Removes the input Direction from this set. */
	public void remove(Direction direction)
	{
		if (this.contains(direction)) this.value -= direction.value;
	}

	/** Removes corner directions that are not connected to both adjacent directions. */
	public void removeFreeCorners()
	{
		if (this.contains(NORTHEAST) && !(this.contains(NORTH) && this.contains(EAST))) this.remove(NORTHEAST);
		if (this.contains(NORTHWEST) && !(this.contains(NORTH) && this.contains(WEST))) this.remove(NORTHWEST);
		if (this.contains(SOUTHEAST) && !(this.contains(SOUTH) && this.contains(EAST))) this.remove(SOUTHEAST);
		if (this.contains(SOUTHWEST) && !(this.contains(SOUTH) && this.contains(WEST))) this.remove(SOUTHWEST);
	}

	@Override
	public String toString()
	{
		return String.valueOf(this.value);
	}

}
