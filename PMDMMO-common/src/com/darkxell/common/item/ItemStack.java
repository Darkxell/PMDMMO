package com.darkxell.common.item;

import org.jdom2.Element;

public class ItemStack
{

	public static final String XML_ROOT = "item";

	/** The ID of the Item. */
	public final int id;
	/** The number of Items in this Stack. Almost always 1 except for Poké, Gravelerock and similar items. */
	private int quantity;

	public ItemStack(Element xml)
	{
		this.id = Integer.parseInt(xml.getAttributeValue("id"));
		this.quantity = xml.getAttribute("quantity") == null ? 1 : Integer.parseInt(xml.getAttributeValue("quantity"));
	}

	public ItemStack(int id)
	{
		this.id = id;
		this.quantity = 1;
	}

	public int getQuantity()
	{
		return this.quantity;
	}

	public Item item()
	{
		return ItemRegistry.find(this.id);
	}

	public ItemStack setQuantity(int quantity)
	{
		this.quantity = quantity;
		return this;
	}

	public Element toXML()
	{
		Element root = new Element(Item.XML_ROOT);
		root.setAttribute("id", Integer.toString(this.id));
		if (this.quantity != 1) root.setAttribute("quantity", Integer.toString(this.quantity));
		return root;
	}

}
