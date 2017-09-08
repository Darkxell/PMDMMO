package com.darkxell.common.item;

import org.jdom2.Element;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.ItemUseEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Message;

/** An Item that has different effects when used. */
public class ItemEscapeOrb extends Item
{

	public ItemEscapeOrb(Element xml)
	{
		super(xml);
	}

	public ItemEscapeOrb(int id, int price, int sell, int sprite, boolean isStackable)
	{
		super(id, price, sell, sprite, isStackable);
	}

	@Override
	public ItemUseEvent use(Floor floor, DungeonPokemon pokemon)
	{
		return new ItemUseEvent(this, pokemon, null, floor, new Message("item.orb.escape").addReplacement("<pokemon>", pokemon.pokemon.getNickname()));
	}

}
