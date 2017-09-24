package com.darkxell.common.event.pokemon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.stats.ExperienceGainedEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.Message;

public class FaintedPokemonEvent extends DungeonEvent
{

	/** The Pokémon that damage the fainted Pokémon. Can be null if the fainting damage didn't result from a Pokémon's move. */
	public final DungeonPokemon damager;
	public final DungeonPokemon pokemon;

	public FaintedPokemonEvent(Floor floor, DungeonPokemon pokemon, DungeonPokemon damager)
	{
		super(floor);
		this.pokemon = pokemon;
		this.damager = damager;

		this.messages.add(new Message("pokemon.fainted").addReplacement("<pokemon>", pokemon.pokemon.getNickname()));
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		if (this.pokemon.pokemon.getItem() != null) this.pokemon.tile.setItem(this.pokemon.pokemon.getItem());
		this.pokemon.tile.setPokemon(null);
		this.floor.dungeon.unregisterActor(this.pokemon);

		if (this.damager != null && this.damager.pokemon.player != null) for (Pokemon pokemon : this.damager.pokemon.player.getTeam())
			this.resultingEvents.add(new ExperienceGainedEvent(this.floor, pokemon, this.pokemon.experienceGained()));

		return super.processServer();
	}

}
