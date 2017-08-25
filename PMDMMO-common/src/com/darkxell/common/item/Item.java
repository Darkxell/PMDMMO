package com.darkxell.common.item;

import org.jdom2.Element;

/** Represents an Item type. */
public abstract class Item
{
	public static final String XML_ROOT = "item";

	/** This Item's ID. */
	public final int id;
	/** This Item's price to buy. */
	public final int price;
	/** This Item's price to sell. */
	public final int sell;

	public Item(Element xml)
	{
		this.id = Integer.parseInt(xml.getAttributeValue("id"));
		this.price = Integer.parseInt(xml.getAttributeValue("price"));
		this.sell = Integer.parseInt(xml.getAttributeValue("sell"));
	}

	public Item(int id, int price, int sell)
	{
		this.id = id;
		this.price = price;
		this.sell = sell;
	}

	public Element toXML()
	{
		Element root = new Element(XML_ROOT);
		root.setAttribute("id", Integer.toString(this.id));
		root.setAttribute("price", Integer.toString(this.price));
		root.setAttribute("sell", Integer.toString(this.sell));
		return root;
	}

}
