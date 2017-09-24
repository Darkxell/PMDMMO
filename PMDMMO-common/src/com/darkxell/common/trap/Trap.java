package com.darkxell.common.trap;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.DungeonEvent.MessageEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Message;

public abstract class Trap
{

	public final int id;

	public Trap(int id)
	{
		this.id = id;
		TrapRegistry.traps.put(this.id, this);
	}

	public Message name()
	{
		return new Message("trap." + this.id);
	}

	/** Called when a Pokémon steps on this Trap. */
	public ArrayList<DungeonEvent> onPokemonStep(Floor floor, DungeonPokemon pokemon)
	{
		ArrayList<DungeonEvent> events = new ArrayList<DungeonEvent>();
		events.add(new MessageEvent(floor, new Message("trap.stepped").addReplacement("<pokemon>", pokemon.pokemon.getNickname()).addReplacement("<trap>",
				this.name())));
		return events;
	}

}
