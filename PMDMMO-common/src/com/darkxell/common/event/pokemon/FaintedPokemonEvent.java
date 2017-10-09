package com.darkxell.common.event.pokemon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.stats.ExperienceGainedEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.language.Message;

public class FaintedPokemonEvent extends DungeonEvent
{

	/** The Pok�mon that damage the fainted Pok�mon. Can be null if the fainting damage didn't result from a Pok�mon's move. */
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
		this.floor.unsummonPokemon(this.pokemon);
		if (this.pokemon.pokemon.getItem() != null) this.pokemon.tile.setItem(this.pokemon.pokemon.getItem());

		if (this.damager != null && this.damager.pokemon.player != null) for (Pokemon pokemon : this.damager.pokemon.player.getTeam())
			this.resultingEvents.add(new ExperienceGainedEvent(this.floor, pokemon, this.pokemon.experienceGained()));

		return super.processServer();
	}

}
