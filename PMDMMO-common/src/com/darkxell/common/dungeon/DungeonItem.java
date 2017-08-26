package com.darkxell.common.dungeon;

import org.jdom2.Element;

import com.darkxell.common.item.Item;
import com.darkxell.common.item.ItemRegistry;

/** Describes how an Item appears in a Dungeon. */
public class DungeonItem
{
	public static final String XML_ROOT = "item";

	/** The floors this Item can appear on. */
	public final FloorSet floors;
	/** The Item ID. */
	public final int id;
	/** The Quantity of the Item. Always 1 except for Poké, Gravelerock and similar Items. */
	public final int quantityMin, quantityMax;

	public DungeonItem(Element xml)
	{
		this.id = Integer.parseInt(xml.getAttributeValue("id"));
		this.quantityMin = xml.getAttributeValue("min") == null ? 1 : Integer.parseInt(xml.getAttributeValue("min"));
		this.quantityMax = xml.getAttributeValue("max") == null ? 1 : Integer.parseInt(xml.getAttributeValue("max"));
		this.floors = new FloorSet(xml.getChild(FloorSet.XML_ROOT));
	}

	public DungeonItem(int id, int quantityMin, int quantityMax, FloorSet floors)
	{
		this.id = id;
		this.quantityMin = quantityMin;
		this.quantityMax = quantityMax;
		this.floors = floors;
	}

	public Item item()
	{
		return ItemRegistry.find(this.id);
	}

	public Element toXML()
	{
		Element root = new Element(XML_ROOT);
		root.setAttribute("id", Integer.toString(this.id));
		if (this.quantityMin != 1) root.setAttribute("min", Integer.toString(this.quantityMin));
		if (this.quantityMax != 1) root.setAttribute("max", Integer.toString(this.quantityMax));
		root.addContent(this.floors.toXML());
		return root;
	}

}
