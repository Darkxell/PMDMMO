package com.darkxell.common.event;

import java.util.ArrayList;

import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Message;

public class FaintedPokemonEvent extends DungeonEvent
{

	public final DungeonPokemon pokemon;

	public FaintedPokemonEvent(DungeonPokemon pokemon)
	{
		this.pokemon = pokemon;

		this.messages.add(new Message("pokemon.fainted").addReplacement("<pokemon>", pokemon.pokemon.getNickname()));
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		if (this.pokemon.pokemon.getItem() != null) this.pokemon.tile.setItem(this.pokemon.pokemon.getItem());
		this.pokemon.tile.setPokemon(null);
		return super.processServer();
	}

}
