package com.darkxell.common.item;

import org.jdom2.Element;

import com.darkxell.common.util.Message;

/** Represents an Item type. */
public class Item
{
	public static final String XML_ROOT = "item";

	/** This Item's ID. */
	public final int id;
	/** This Item's price to buy. */
	public final int price;
	/** This Item's price to sell. */
	public final int sell;
	/** The ID of the Item's sprite. */
	public final int spriteID;
	/** True if this Item can be stacked. */
	public final boolean isStackable;

	public Item(Element xml)
	{
		this.id = Integer.parseInt(xml.getAttributeValue("id"));
		this.price = Integer.parseInt(xml.getAttributeValue("price"));
		this.sell = Integer.parseInt(xml.getAttributeValue("sell"));
		this.spriteID = Integer.parseInt(xml.getAttributeValue("sprite"));
		this.isStackable = "true".equals(xml.getAttributeValue("stackable"));
	}

	public Item(int id, int price, int sell, int spriteID, boolean stackable)
	{
		this.id = id;
		this.price = price;
		this.sell = sell;
		this.spriteID = spriteID;
		this.isStackable = stackable;
	}

	public Message name()
	{
		return new Message("item." + this.id);
	}

	public Element toXML()
	{
		Element root = new Element(XML_ROOT);
		root.setAttribute("id", Integer.toString(this.id));
		root.setAttribute("type", this.getClass().getName().substring(Item.class.getName().length()));
		root.setAttribute("price", Integer.toString(this.price));
		root.setAttribute("sell", Integer.toString(this.sell));
		root.setAttribute("sprite", Integer.toString(this.spriteID));
		if (this.isStackable) root.setAttribute("stackable", "true");
		return root;
	}

}
