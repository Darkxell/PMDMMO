package com.darkxell.common.event.pokemon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.Logger;

/** The travel of Pokémon. */
public class PokemonTravelEvent extends DungeonEvent
{

	public static class PokemonTravel
	{
		public final Direction direction;
		public final Tile origin, destination;
		public final DungeonPokemon pokemon;
		public final boolean running;

		public PokemonTravel(DungeonPokemon pokemon, boolean running, Direction direction)
		{
			this.pokemon = pokemon;
			this.running = running;
			this.direction = direction;
			this.origin = pokemon.tile();
			this.destination = pokemon.tile().adjacentTile(this.direction);
			if (!this.destination.canMoveTo(this.pokemon, this.direction, false))
			{
				Logger.e("Illegal movement!");
				this.displayInConsole();
			}
		}

		private void displayInConsole()
		{
			System.out.println(this.direction);
			Floor floor = this.origin.floor;
			int xs = this.origin.x, ys = this.origin.y;
			for (int x = xs - 2; x <= xs + 2; ++x)
			{
				for (int y = ys - 2; y <= ys + 2; ++y)
					if (floor.tileAt(x, y) == this.origin) System.out.print("O");
					else if (floor.tileAt(x, y).getPokemon() != null) System.out.print("P");
					else System.out.print(floor.tileAt(x, y).type().c);
				System.out.println();
			}
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

	public PokemonTravelEvent(Floor floor, DungeonPokemon pokemon, boolean running, Direction direction)
	{
		this(floor, new PokemonTravel(pokemon, running, direction));
	}

	public PokemonTravelEvent(Floor floor, DungeonPokemon pokemon, Direction direction)
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
	public String loggerMessage()
	{
		String m = "";
		for (PokemonTravel t : this.travels)
		{
			if (!m.equals("")) m += ", ";
			m += t.pokemon;
		}
		return m + " travelled.";
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
