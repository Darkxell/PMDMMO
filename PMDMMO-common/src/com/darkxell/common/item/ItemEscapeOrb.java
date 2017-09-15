package com.darkxell.common.item;

import org.jdom2.Element;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.DungeonExitEvent;
import com.darkxell.common.pokemon.DungeonPokemon;

/** An Item that has different effects when used. */
public class ItemEscapeOrb extends ItemOrb
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
	public DungeonEvent[] use(Floor floor, DungeonPokemon pokemon, DungeonPokemon target)
	{
		return new DungeonEvent[]
		{ new DungeonExitEvent(pokemon) };
	}
}
