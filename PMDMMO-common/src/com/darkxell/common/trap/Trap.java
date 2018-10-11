package com.darkxell.common.trap;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.language.Message;

public abstract class Trap implements Comparable<Trap>
{

	public final int id;

	public Trap(int id)
	{
		this.id = id;
		TrapRegistry.traps.put(this.id, this);
	}

	@Override
	public int compareTo(Trap o)
	{
		return Integer.compare(this.id, o.id);
	}

	public Message name()
	{
		return new Message("trap." + this.id);
	}

	public abstract void onPokemonStep(Floor floor, DungeonPokemon pokemon, ArrayList<DungeonEvent> events);

	@Override
	public String toString()
	{
		return this.name().toString();
	}

}
