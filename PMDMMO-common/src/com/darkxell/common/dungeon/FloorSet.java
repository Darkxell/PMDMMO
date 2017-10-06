package com.darkxell.common.dungeon;

import java.util.ArrayList;
import java.util.Comparator;

import javafx.util.Pair;

import org.jdom2.Element;

import com.darkxell.common.util.XMLUtils;

/** Holds a set of Floors. */
@SuppressWarnings("restriction")
public class FloorSet
{
	public static final String XML_ROOT = "floors";

	/** Lists of floors not part of this set. */
	private ArrayList<Integer> except;
	/** List of parts, with start and end. */
	private ArrayList<Pair<Integer, Integer>> set;

	public FloorSet(ArrayList<Pair<Integer, Integer>> set, ArrayList<Integer> except)
	{
		this.set = set;
		this.except = except;
	}

	public FloorSet(Element xml)
	{
		this.set = new ArrayList<Pair<Integer, Integer>>();
		this.except = XMLUtils.readIntArrayAsList(xml.getChild("except"));
		for (Element part : xml.getChildren("part"))
		{
			if (part.getAttribute("floor") != null) this.set.add(new Pair<Integer, Integer>(Integer.parseInt(part.getAttributeValue("floor")), Integer
					.parseInt(part.getAttributeValue("floor"))));
			else this.set.add(new Pair<Integer, Integer>(Integer.parseInt(part.getAttributeValue("start")), Integer.parseInt(part.getAttributeValue("end"))));
		}
	}

	public FloorSet(int start, int end)
	{
		this.set = new ArrayList<Pair<Integer, Integer>>();
		this.set.add(new Pair<Integer, Integer>(start, end));
		this.except = new ArrayList<Integer>();
	}

	/** @return True if this Set contains the input floor. */
	public boolean contains(int floor)
	{
		for (Pair<Integer, Integer> part : this.set)
			if (floor >= part.getKey() && floor <= part.getValue() && !this.except.contains(floor)) return true;
		return false;
	}

	/** @return A copy of this Floor set. */
	public FloorSet copy()
	{
		ArrayList<Pair<Integer, Integer>> s = new ArrayList<Pair<Integer, Integer>>();
		for (Pair<Integer, Integer> pair : this.set)
			s.add(new Pair<Integer, Integer>(pair.getKey(), pair.getValue()));

		ArrayList<Integer> e = new ArrayList<Integer>();
		e.addAll(this.except);

		return new FloorSet(s, e);
	}

	/** @return The number of Floors in this Set. */
	public int floorCount()
	{
		int count = 0;
		for (Pair<Integer, Integer> part : this.set)
			count += part.getValue() - part.getKey() + 1; // 15 - 15 + 1 = 1 ; 15 - 18 + 1 = 4
		return count - this.except.size();
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
				if (!this.except.contains(floors)) floors.add(floor);
				++floor;
			} while (floor <= part.getValue());
		}

		floors.sort(Comparator.naturalOrder());
		int[] array = new int[floors.size()];
		for (int i = 0; i < array.length; ++i)
			array[i] = floors.get(i);
		return array;
	}

	public Element toXML()
	{
		Element root = new Element(XML_ROOT);
		for (Pair<Integer, Integer> part : this.set)
		{
			if (part.getKey().intValue() == part.getValue().intValue()) root.addContent(new Element("part").setAttribute("floor",
					Integer.toString(part.getKey())));
			else root.addContent(new Element("part").setAttribute("start", Integer.toString(part.getKey())).setAttribute("end",
					Integer.toString(part.getValue())));
		}
		if (this.except.size() != 0) root.addContent(XMLUtils.toXML("except", this.except));
		return root;
	}

}
