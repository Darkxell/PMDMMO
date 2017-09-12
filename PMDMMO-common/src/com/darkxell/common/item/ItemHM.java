package com.darkxell.common.item;

import org.jdom2.Element;

import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveRegistry;

/** An Item that teaches a move to a Pokémon when used. */
public class ItemHM extends Item
{

	/** The move this TM teaches. */
	public final int moveID;

	public ItemHM(Element xml)
	{
		super(xml);
		this.moveID = Integer.parseInt(xml.getAttributeValue("move"));
	}

	public ItemHM(int id, int price, int sell, int sprite, boolean isStackable, int moveID)
	{
		super(id, price, sell, sprite, isStackable);
		this.moveID = moveID;
	}

	public ItemCategory category()
	{
		return ItemCategory.HMS;
	}

	/** @return The Move this TM teaches. */
	public Move move()
	{
		return MoveRegistry.find(this.moveID);
	}

	@Override
	public Element toXML()
	{
		return super.toXML().setAttribute("move", Integer.toString(this.moveID));
	}

}
