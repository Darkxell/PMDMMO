package com.darkxell.common.event.dungeon.weather;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.weather.WeatherDamaging;

public class WeatherDamageEvent extends DungeonEvent
{

	public final int damage;
	private final ArrayList<DungeonPokemon> pokemon;
	public final WeatherDamaging source;

	public WeatherDamageEvent(Floor floor, WeatherDamaging source, ArrayList<DungeonPokemon> pokemon, int damage)
	{
		super(floor);
		this.source = source;
		this.pokemon = pokemon;
		this.damage = damage;
	}

	@Override
	public String loggerMessage()
	{
		return this.source.name() + " dealt " + this.damage + " damage to " + this.pokemon.size() + " Pokemon.";
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		for (DungeonPokemon pokemon : this.pokemon)
			this.resultingEvents.add(new DamageDealtEvent(this.floor, pokemon, this.source, this.damage));
		return super.processServer();
	}

}
