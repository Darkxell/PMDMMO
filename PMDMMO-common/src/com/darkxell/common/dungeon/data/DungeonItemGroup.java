package com.darkxell.common.dungeon.data;

import com.darkxell.common.Registries;
import com.darkxell.common.item.Item;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.util.RandomUtil;
import com.darkxell.common.util.XMLUtils;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.Random;

/** A group of Items that can appear in a Dungeon. */
public class DungeonItemGroup
{
	public static final String XML_ROOT = "group";

	/** @return The list of weights associated with the input Items. */
	public static ArrayList<Integer> weights(ArrayList<DungeonItemGroup> items)
	{
		ArrayList<Integer> weights = new ArrayList<>();
		for (DungeonItemGroup item : items)
			weights.add(item.weight);
		return weights;
	}

	/** The weight of each Item. */
	public final int[] chances;
	/** The floors this Item can appear on. */
	public final FloorSet floors;
	/** The Item ID. */
	public final int[] items;
	/** This Item group's weight. */
	public final int weight;

	public DungeonItemGroup(Element xml)
	{
		this.weight = XMLUtils.getAttribute(xml, "weight", 1);
		this.floors = new FloorSet(xml.getChild(FloorSet.XML_ROOT, xml.getNamespace()));
		this.items = XMLUtils.readIntArray(xml.getChild("ids", xml.getNamespace()));
		if (xml.getChild("chances", xml.getNamespace()) == null)
		{
			this.chances = new int[this.items.length];
			for (int i = 0; i < this.chances.length; ++i)
				this.chances[i] = 1;
		} else this.chances = XMLUtils.readIntArray(xml.getChild("chances", xml.getNamespace()));
	}

	public DungeonItemGroup(FloorSet floors, int weight, int[] items, int[] chances)
	{
		this.weight = weight;
		this.floors = floors;
		this.items = items;
		this.chances = chances;
	}

	ItemStack generate(Random random, boolean allowMoney)
	{
		ArrayList<Integer> ids = new ArrayList<>();
		ArrayList<Integer> weights = new ArrayList<>();
		for (int i = 0; i < this.items.length; ++i)
			if (allowMoney || this.items[i] != Item.POKEDOLLARS)
			{
				ids.add(this.items[i]);
				weights.add(this.chances[i]);
			}
		return new ItemStack(RandomUtil.weightedRandom(ids, weights, random));
	}

	public Item[] items()
	{
		Item[] it = new Item[this.items.length];
		for (int i = 0; i < it.length; ++i)
			it[i] = Registries.items().find(this.items[i]);
		return it;
	}

	public Element toXML()
	{
		Element root = new Element(XML_ROOT);
		XMLUtils.setAttribute(root, "weight", this.weight, 1);
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
