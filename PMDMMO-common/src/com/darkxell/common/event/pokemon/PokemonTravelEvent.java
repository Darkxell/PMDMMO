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
		public final boolean running;

		public PokemonTravel(DungeonPokemon pokemon, boolean running, short direction)
		{
			this.pokemon = pokemon;
			this.running = running;
			this.direction = direction;
			this.origin = pokemon.tile;
			this.destination = pokemon.tile.adjacentTile(this.direction);
		}

		public boolean isReversed(PokemonTravel t)
		{
			return this.origin == t.destination && this.destination == t.origin;
		}

		@Override
		public String toString()
		{
			return this.pokemon.toString() + " travels";
		}
	}

	private PokemonTravel[] travels;

	public PokemonTravelEvent(Floor floor, DungeonPokemon pokemon, boolean running, short direction)
	{
		this(floor, new PokemonTravel(pokemon, running, direction));
	}

	public PokemonTravelEvent(Floor floor, DungeonPokemon pokemon, short direction)
	{
		this(floor, pokemon, false, direction);
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

	public boolean isRunning()
	{
		for (PokemonTravel travel : this.travels)
			if (travel.running) return true;
		return false;
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		for (PokemonTravel travel : this.travels)
		{

			if (travel.pokemon.isTeamLeader())
				this.resultingEvents.add(new BellyChangedEvent(this.floor, travel.pokemon, -.1 * travel.pokemon.energyMultiplier()));
			travel.origin.removePokemon(travel.pokemon);
			travel.destination.setPokemon(travel.pokemon);
			this.resultingEvents.addAll(travel.destination.onPokemonStep(this.floor, travel.pokemon, travel.running));
		}
		return super.processServer();
	}

	public PokemonTravel[] travels()
	{
		return this.travels.clone();
	}

}
