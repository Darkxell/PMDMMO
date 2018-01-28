package com.darkxell.common.event.pokemon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent.DamageSource;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.language.Message;

public class FaintedPokemonEvent extends DungeonEvent
{

	/** The source that damaged the fainted Pokémon. Can be null if the fainting damage didn't result from a Pokémon's move. */
	public final DamageSource damage;
	public final DungeonPokemon pokemon;

	public FaintedPokemonEvent(Floor floor, DungeonPokemon pokemon, DamageSource damage)
	{
		super(floor);
		this.pokemon = pokemon;
		this.damage = damage;

		this.messages.add(new Message("pokemon.fainted").addReplacement("<pokemon>", pokemon.pokemon.getNickname()));
	}

	@Override
	public String loggerMessage()
	{
		return this.messages.get(0).toString();
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		this.floor.unsummonPokemon(this.pokemon);
		if (this.pokemon.pokemon.getItem() != null) this.pokemon.tile().setItem(this.pokemon.pokemon.getItem());
		if (this.damage.getExperienceEvent() != null) this.damage.getExperienceEvent().experience += this.pokemon.experienceGained();

		return super.processServer();
	}
}
