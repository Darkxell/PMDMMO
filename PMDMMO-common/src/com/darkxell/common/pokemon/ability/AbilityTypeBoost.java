package com.darkxell.common.pokemon.ability;

import com.darkxell.common.pokemon.PokemonType;

/** An ability that boosts moves of a certain type when the user has 1/4 of its HP or less. */
public class AbilityTypeBoost extends Ability
{

	/** The type of moves to boost. */
	public final PokemonType type;

	public AbilityTypeBoost(int id, PokemonType type)
	{
		super(id);
		this.type = type;
	}

}
