package com.darkxell.common.item;

import org.jdom2.Element;

import com.darkxell.common.util.Communicable;
import com.darkxell.common.util.XMLUtils;
import com.darkxell.common.util.language.Lang;
import com.darkxell.common.util.language.Message;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

public class ItemStack implements Comparable<ItemStack>, Communicable
{

	public static final String XML_ROOT = "item";

	/** The ID of the Item. */
	private int id;
	/** The number of Items in this Stack. Almost always 1 except for Poké, Gravelerock and similar items. */
	private int quantity;

	public ItemStack()
	{
		this(-1);
	}

	public ItemStack(Element xml)
	{
		this.id = Integer.parseInt(xml.getAttributeValue("id"));
		this.quantity = XMLUtils.getAttribute(xml, "quantity", 1);
	}

	public ItemStack(int id)
	{
		this.id = id;
		this.quantity = 1;
	}
        
        public ItemStack(int id, int quantity)
	{
		this.id = id;
		this.quantity = quantity;
	}

	@Override
	public int compareTo(ItemStack o)
	{
		int category = Integer.compare(this.item().category().order, o.item().category().order);
		return category == 0 ? this.name().toString().compareTo(o.name().toString()) : category;
	}

	/** @return A copy of this Item Stack. */
	public ItemStack copy()
	{
		return new ItemStack(this.id).setQuantity(this.quantity);
	}

	public int getQuantity()
	{
		return this.quantity;
	}
	
	public int getId()
	{
		return this.id;
	}

	/** @return The message describing this Item. */
	public Message info()
	{
		return new Message("item.info." + this.id);
	}

	public Item item()
	{
		return ItemRegistry.find(this.id);
	}

	public Message name()
	{
		if (Lang.containsKey("item." + this.id + ".stack"))
			return new Message("item." + this.id + ".stack").addReplacement("<quantity>", Integer.toString(this.quantity));
		return this.item().name();
	}

	@Override
	public void read(JsonObject value)
	{
		this.id = value.getInt("id", -1);
		this.quantity = value.getInt("quantity", 1);
	}

	public ItemStack setQuantity(int quantity)
	{
		this.quantity = quantity;
		if (this.quantity > 999) this.quantity = 999;
		return this;
	}

	@Override
	public JsonObject toJson()
	{
		JsonObject root = Json.object();
		root.add("id", this.id);
		if (this.quantity != 1) root.add("quantity", this.quantity);
		return root;
	}

	public Element toXML()
	{
		Element root = new Element(Item.XML_ROOT);
		root.setAttribute("id", Integer.toString(this.id));
		XMLUtils.setAttribute(root, "quantity", this.quantity, 1);
		return root;
	}

}
