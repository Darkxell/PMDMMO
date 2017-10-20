package com.darkxell.common.weather;

import java.awt.Color;
import java.util.ArrayList;
import java.util.function.Predicate;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.dungeon.weather.WeatherDamageEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent.DamageSource;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.PokemonType;

public class WeatherDamaging extends Weather implements DamageSource
{
	public static final int PERIOD = 10, DAMAGE = 5;

	/** The Damage this Weather deals. */
	public final int damage;
	/** A list of types that are immune to this Weather. */
	public final PokemonType[] immunes;
	/** The number of ticks it takes for this Weather to deal damage. */
	public final int period;

	public WeatherDamaging(byte id, Color layer, int period, int damage, PokemonType... immunes)
	{
		super(id, layer);
		this.period = period;
		this.damage = damage;
		this.immunes = immunes;
	}

	@Override
	public ArrayList<DungeonEvent> weatherTick(Floor floor, int tick)
	{
		ArrayList<DungeonEvent> e = super.weatherTick(floor, tick);
		if (tick % this.period == 0)
		{
			System.out.println(tick);
			ArrayList<DungeonPokemon> pokemon = floor.listPokemon();
			pokemon.removeIf(new Predicate<DungeonPokemon>()
			{
				@Override
				public boolean test(DungeonPokemon pokemon)
				{
					for (PokemonType type : immunes)
					{
						if (pokemon.pokemon.species.isType(type)) return true;
					}
					return false;
				}
			});
			e.add(new WeatherDamageEvent(floor, this, pokemon, this.damage));
		}
		return e;
	}

}
