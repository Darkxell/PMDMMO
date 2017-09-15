package com.darkxell.common.event.item;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.item.Item;
import com.darkxell.common.pokemon.DungeonPokemon;

/** Describes the events occurring while using an Item. */
public class ItemUseEvent extends DungeonEvent
{

	/** The Floor the user is on. */
	public final Floor floor;
	/** The Item that was used. */
	public final Item item;
	/** The Pokémon that the Item was used on. null if there was no target. */
	public final DungeonPokemon target;
	/** The Pokémon that used the Item. */
	public final DungeonPokemon user;

	public ItemUseEvent(Item item, DungeonPokemon user, DungeonPokemon target, Floor floor)
	{
		this.item = item;
		this.user = user;
		this.target = target;
		this.floor = floor;
	}

	@Override
	public DungeonEvent[] processServer()
	{
		return this.item.use(this.floor, this.user, this.target);
	}

}
