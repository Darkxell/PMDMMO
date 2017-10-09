package com.darkxell.common.item;

import org.jdom2.Element;

import com.darkxell.common.util.language.Message;

/** An Item that increases a stat when drunk. */
public class ItemDrink extends ItemFood
{

	public final byte stat;

	public ItemDrink(Element xml)
	{
		super(xml);
		this.stat = Byte.parseByte(xml.getAttributeValue("stat"));
	}

	public ItemDrink(int id, int price, int sell, int sprite, boolean isStackable, int food, int bellyIfFull, int belly, byte stat)
	{
		super(id, price, sell, sprite, isStackable, food, bellyIfFull, belly);
		this.stat = stat;
	}

	public ItemCategory category()
	{
		return ItemCategory.DRINKS;
	}

	@Override
	protected String getUseID()
	{
		return "item.ingested";
	}

	@Override
	public Message getUseName()
	{
		return new Message("item.ingest");
	}

	@Override
	public Element toXML()
	{
		return super.toXML().setAttribute("stat", Byte.toString(this.stat));
	}
	
	@Override
	public boolean usedOnTeamMember()
	{
		return true;
	}

}
