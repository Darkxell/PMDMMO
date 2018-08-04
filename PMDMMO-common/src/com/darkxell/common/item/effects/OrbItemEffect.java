package com.darkxell.common.item.effects;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.DungeonEvent.MessageEvent;
import com.darkxell.common.item.Item;
import com.darkxell.common.item.ItemEffect;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.language.Message;

/** An Item that has different effects when used. */
public class OrbItemEffect extends ItemEffect
{

	public OrbItemEffect(int id)
	{
		super(id);
	}

	@Override
	public Message name(Item item)
	{
		return super.name(item).addPrefix("<orb>");
	}

	protected void orbEffect(Floor floor, DungeonPokemon pokemon, DungeonPokemon target, ArrayList<DungeonEvent> events)
	{}

	@Override
	public final void use(Floor floor, Item item, DungeonPokemon pokemon, DungeonPokemon target, ArrayList<DungeonEvent> events)
	{
		if (floor.data.isBossFloor()) events.add(new MessageEvent(floor, new Message("item.orb.boss")));
		else this.orbEffect(floor, pokemon, target, events);
	}

}
