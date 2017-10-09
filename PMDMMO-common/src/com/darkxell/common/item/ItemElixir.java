package com.darkxell.common.item;

import org.jdom2.Element;

import com.darkxell.common.util.Message;

/** An Item that restores PP when eaten. */
public class ItemElixir extends ItemFood
{

	public final int pp;

	public ItemElixir(Element xml)
	{
		super(xml);
		this.pp = Integer.parseInt(xml.getAttributeValue("pp"));
	}

	public ItemElixir(int id, int price, int sell, int sprite, boolean isStackable, int food, int bellyIfFull, int belly, int pp)
	{
		super(id, price, sell, sprite, isStackable, food, bellyIfFull, belly);
		this.pp = pp;
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
		return super.toXML().setAttribute("pp", Integer.toString(this.pp));
	}
	
	@Override
	public boolean usedOnTeamMember()
	{
		return true;
	}

}
