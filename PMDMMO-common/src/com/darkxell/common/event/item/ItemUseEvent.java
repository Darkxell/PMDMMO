package com.darkxell.common.event.item;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.item.Item;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Message;

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
	public ArrayList<DungeonEvent> processServer()
	{
		this.resultingEvents.addAll(this.item.use(this.floor, this.user, this.target));
		if (this.resultingEvents.size() == 0) this.resultingEvents.add(new MessageEvent(new Message("item.no_effect")));
		return super.processServer();
	}

}
