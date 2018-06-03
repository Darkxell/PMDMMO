package com.darkxell.common.item;

import org.jdom2.Element;

import com.darkxell.common.dbobject.DBItemstack;
import com.darkxell.common.dungeon.TempIDRegistry.HasID;
import com.darkxell.common.util.XMLUtils;
import com.darkxell.common.util.language.Lang;
import com.darkxell.common.util.language.Message;

public class ItemStack implements Comparable<ItemStack>, HasID
{

	public static final String XML_ROOT = "item";

	private DBItemstack data;

	private Item item;

	public ItemStack()
	{
		this(-1);
	}

	public ItemStack(DBItemstack data)
	{
		this.setData(data);
	}

	public ItemStack(Element xml)
	{
		this(Integer.parseInt(xml.getAttributeValue("id")), XMLUtils.getAttribute(xml, "quantity", 1));
	}

	public ItemStack(int itemid)
	{
		this(itemid, 1);
	}

	public ItemStack(int itemid, int quantity)
	{
		this(new DBItemstack(-1, itemid, quantity));
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
		return new ItemStack(this.itemid()).setQuantity(this.quantity());
	}

	public DBItemstack getData()
	{
		return this.data;
	}

	@Override
	public long id()
	{
		return this.getData().id;
	}

	/** @return The message describing this Item. */
	public Message info()
	{
		return new Message("item.info." + this.itemid());
	}

	public Item item()
	{
		return this.item;
	}

	/** The ID of the Item. */
	public int itemid()
	{
		return this.data.itemid;
	}

	public Message name()
	{
		if (Lang.containsKey("item." + this.itemid() + ".stack"))
			return new Message("item." + this.itemid() + ".stack").addReplacement("<quantity>", Long.toString(this.quantity()));
		return this.item().name();
	}

	/** The number of Items in this Stack. Almost always 1 except for Pok�, Gravelerock and similar items. */
	public long quantity()
	{
		return this.data.quantity;
	}

	public void setData(DBItemstack data)
	{
		this.data = data;
		this.item = ItemRegistry.find(this.data.itemid);
	}

	@Override
	public void setId(long id)
	{
		this.getData().id = id;
	}

	public ItemStack setQuantity(long quantity)
	{
		this.data.quantity = quantity;
		if (this.quantity() > 999) this.data.quantity = 999;
		return this;
	}

	public Element toXML()
	{
		Element root = new Element(Item.XML_ROOT);
		root.setAttribute("id", Integer.toString(this.itemid()));
		XMLUtils.setAttribute(root, "quantity", this.quantity(), 1);
		return root;
	}

}
