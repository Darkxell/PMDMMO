package com.darkxell.common.event.pokemon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.pokemon.DungeonPokemon;

/** The travel of Pokémon. */
public class PokemonTravelEvent extends DungeonEvent
{

	public static class PokemonTravel
	{
		public final short direction;
		public final Tile origin, destination;
		public final DungeonPokemon pokemon;

		public PokemonTravel(DungeonPokemon pokemon, short direction)
		{
			this.pokemon = pokemon;
			this.direction = direction;
			this.origin = pokemon.tile;
			this.destination = pokemon.tile.adjacentTile(this.direction);
		}
	}

	private PokemonTravel[] travels;

	public PokemonTravelEvent(DungeonPokemon pokemon, short direction)
	{
		this(new PokemonTravel(pokemon, direction));
	}

	public PokemonTravelEvent(PokemonTravel... travels)
	{
		this.travels = travels;
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		ArrayList<DungeonEvent> events = new ArrayList<DungeonEvent>();
		for (PokemonTravel travel : this.travels)
		{
			travel.origin.removePokemon(travel.pokemon);
			travel.destination.setPokemon(travel.pokemon);
			events.addAll(travel.destination.onPokemonStep(travel.pokemon));
		}
		return events;
	}

	public PokemonTravel[] travels()
	{
		return this.travels.clone();
	}

}
