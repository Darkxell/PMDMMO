package com.darkxell.common.item;

import java.util.ArrayList;

import org.jdom2.Element;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.DungeonEvent.MessageEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.language.Message;

/** An Item that has different effects when used. */
public class ItemOrb extends Item
{

	public ItemOrb(Element xml)
	{
		super(xml);
	}

	public ItemOrb(int id, int price, int sell, int sprite, boolean isStackable)
	{
		super(id, price, sell, sprite, isStackable);
	}

	public ItemCategory category()
	{
		return ItemCategory.ORBS;
	}

	@Override
	public Message name()
	{
		return super.name().addPrefix("<orb>");
	}

	protected void orbEffect(Floor floor, DungeonPokemon pokemon, DungeonPokemon target, ArrayList<DungeonEvent> events)
	{}

	@Override
	public final void use(Floor floor, DungeonPokemon pokemon, DungeonPokemon target, ArrayList<DungeonEvent> events)
	{
		super.use(floor, pokemon, target, events);
		if (floor.data.isBossFloor()) events.add(new MessageEvent(floor, new Message("item.orb.boss")));
		else this.orbEffect(floor, pokemon, target, events);
	}

}
