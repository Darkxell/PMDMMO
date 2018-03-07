package com.darkxell.client.mechanics.event;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.pokemon.PokemonTravelEvent;

public class PokemonTravelsEvent extends DungeonEvent
{
	private PokemonTravelEvent[] events;

	public PokemonTravelsEvent(Floor floor, ArrayList<PokemonTravelEvent> travels)
	{
		super(floor);
		this.events = travels.toArray(new PokemonTravelEvent[travels.size()]);
	}

	@Override
	public String loggerMessage()
	{
		String s = "";
		boolean first = true;
		for (PokemonTravelEvent e : this.events)
		{
			if (first) s += ", ";
			s += e.pokemon;
		}
		return s + " travel.";
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		for (PokemonTravelEvent e : this.events)
			this.resultingEvents.addAll(e.processServer());
		return super.processServer();
	}

	public PokemonTravelEvent[] travels()
	{
		return this.events.clone();
	}

}
