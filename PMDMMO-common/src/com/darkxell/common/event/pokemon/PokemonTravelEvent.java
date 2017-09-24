package com.darkxell.common.event.pokemon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
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

	public PokemonTravelEvent(Floor floor, DungeonPokemon pokemon, short direction)
	{
		this(floor, new PokemonTravel(pokemon, direction));
	}

	public PokemonTravelEvent(Floor floor, PokemonTravel... travels)
	{
		super(floor);
		this.travels = travels;
	}

	/** @return The first of these Event's travels. */
	public PokemonTravel getTravel()
	{
		return this.travels[0];
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		for (PokemonTravel travel : this.travels)
		{
			travel.origin.removePokemon(travel.pokemon);
			travel.destination.setPokemon(travel.pokemon);
			this.resultingEvents.addAll(travel.destination.onPokemonStep(this.floor, travel.pokemon));
		}
		return super.processServer();
	}

	public PokemonTravel[] travels()
	{
		return this.travels.clone();
	}

}
