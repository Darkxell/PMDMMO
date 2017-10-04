package com.darkxell.common.dungeon;

import java.util.Random;

import org.jdom2.Element;

import com.darkxell.common.item.ItemStack;
import com.darkxell.common.util.XMLUtils;

/** A group of Items that can appear in a Dungeon. */
public class DungeonItem
{
	public static final String XML_ROOT = "group";

	/** The weight of each Item. */
	public final int[] chances;
	/** The floors this Item can appear on. */
	public final FloorSet floors;
	/** The Item ID. */
	public final int[] items;
	/** This Item group's weight. */
	public final int weight;

	public DungeonItem(Element xml)
	{
		this.weight = Integer.parseInt(xml.getAttributeValue("weight"));
		this.floors = new FloorSet(xml.getChild(FloorSet.XML_ROOT));
		this.items = XMLUtils.readIntArray(xml.getChild("ids"));
		if (xml.getChild("chances") == null)
		{
			this.chances = new int[this.items.length];
			for (int i = 0; i < this.chances.length; ++i)
				this.chances[i] = 1;
		} else this.chances = XMLUtils.readIntArray(xml.getChild("chances"));
	}

	public DungeonItem(FloorSet floors, int weight, int[] items, int[] chances)
	{
		this.weight = weight;
		this.floors = floors;
		this.items = items;
		this.chances = chances;
	}

	public ItemStack generate(Random random)
	{
		return new ItemStack(this.items[random.nextInt(this.items.length)]);
	}

	public Element toXML()
	{
		Element root = new Element(XML_ROOT);
		root.setAttribute("weight", Integer.toString(this.weight));
		root.addContent(this.floors.toXML());
		root.addContent(XMLUtils.toXML("ids", this.items));

		boolean chances = false;
		for (int c : this.chances)
			if (c != 1)
			{
				chances = true;
				break;
			}
		if (chances) root.addContent(XMLUtils.toXML("chances", this.chances));
		return root;
	}

}
