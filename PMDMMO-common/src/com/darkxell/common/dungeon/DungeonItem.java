package com.darkxell.common.dungeon;

import org.jdom2.Element;

/** Describes how an Item appears in a Dungeon. */
public class DungeonItem
{
	public static final String XML_ROOT = "item";

	/** The floors this Item can appear on. */
	public final FloorSet floors;
	/** The Item ID. */
	public final int id;
	/** The Level of the Item. */
	public final int quantity;

	public DungeonItem(Element xml)
	{
		this.id = Integer.parseInt(xml.getAttributeValue("id"));
		this.quantity = Integer.parseInt(xml.getAttributeValue("quantity"));
		this.floors = new FloorSet(xml.getChild(FloorSet.XML_ROOT));
	}

	public DungeonItem(int id, int quantity, FloorSet floors)
	{
		this.id = id;
		this.quantity = quantity;
		this.floors = floors;
	}

	/* public Item item() { return ItemRegistry.find(this.id); } */// TODO: uncomment when ItemRegistry done.

	public Element toXML()
	{
		Element root = new Element(XML_ROOT);
		root.setAttribute("id", Integer.toString(this.id));
		root.setAttribute("quantity", Integer.toString(this.quantity));
		root.addContent(this.floors.toXML());
		return root;
	}

}
