package com.darkxell.common.item;

import java.util.ArrayList;

import org.jdom2.Element;

public class ItemUnusable extends Item
{

	public ItemUnusable(Element xml)
	{
		super(xml);
	}

	public ItemUnusable(int id, int price, int sell, int spriteID, boolean stackable)
	{
		super(id, price, sell, spriteID, stackable);
	}

	@Override
	public ArrayList<ItemAction> getLegalActions(boolean inDungeon)
	{
		ArrayList<ItemAction> actions = super.getLegalActions(inDungeon);
		actions.remove(ItemAction.USE);
		return actions;
	}

}
