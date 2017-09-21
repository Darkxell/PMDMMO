package com.darkxell.common.event.pokemon;

import java.util.ArrayList;

import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.stats.ExperienceGainedEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Message;

public class FaintedPokemonEvent extends DungeonEvent
{

	/** The Pokémon that damage the fainted Pokémon. Can be null if the fainting damage didn't result from a Pokémon's move. */
	public final DungeonPokemon damager;
	public final DungeonPokemon pokemon;

	public FaintedPokemonEvent(DungeonPokemon pokemon, DungeonPokemon damager)
	{
		this.pokemon = pokemon;
		this.damager = damager;

		this.messages.add(new Message("pokemon.fainted").addReplacement("<pokemon>", pokemon.pokemon.getNickname()));
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		if (this.pokemon.pokemon.getItem() != null) this.pokemon.tile.setItem(this.pokemon.pokemon.getItem());
		this.pokemon.tile.setPokemon(null);
		ArrayList<DungeonEvent> events = super.processServer();
		if (this.damager != null) events.add(new ExperienceGainedEvent(this.damager, this.pokemon.experienceGained()));
		return events;
	}

}
