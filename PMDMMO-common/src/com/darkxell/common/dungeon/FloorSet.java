package com.darkxell.common.dungeon;

import java.util.ArrayList;
import java.util.Comparator;

import javafx.util.Pair;

import org.jdom2.Element;

/** Holds a set of Floors. */
public class FloorSet
{

	/** List of parts, with start and end. */
	private ArrayList<Pair<Integer, Integer>> set;

	public FloorSet(ArrayList<Pair<Integer, Integer>> set)
	{
		this.set = set;
	}

	public FloorSet(Element xml)
	{
		this.set = new ArrayList<Pair<Integer, Integer>>();
		for (Element part : xml.getChildren("part"))
			this.set.add(new Pair<Integer, Integer>(Integer.parseInt(part.getAttributeValue("start")), Integer.parseInt(part.getAttributeValue("end"))));
	}

	/** @return True if this Set contains the input floor. */
	public boolean contains(int floor)
	{
		for (Pair<Integer, Integer> part : this.set)
			if (floor >= part.getKey() && floor <= part.getValue()) return true;
		return false;
	}

	/** @return The number of Floors in this Set. */
	public int floorCount()
	{
		int count = 0;
		for (Pair<Integer, Integer> part : this.set)
			count += part.getValue() - part.getKey() + 1; // 15 - 15 + 1 = 1 ; 15 - 18 + 1 = 4
		return count;
	}

	/** @return The list of Floors this Set holds. */
	public int[] list()
	{
		ArrayList<Integer> floors = new ArrayList<Integer>();
		for (Pair<Integer, Integer> part : this.set)
		{
			int floor = part.getKey();
			do
			{
				floors.add(floor);
				++floor;
			} while (floor <= part.getValue());
		}

		floors.sort(Comparator.naturalOrder());
		int[] array = new int[floors.size()];
		for (int i = 0; i < array.length; ++i)
			array[i] = floors.get(i);
		return array;
	}

}
