package com.darkxell.common.pokemon.ability;

import java.util.HashMap;

import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.util.Message;

public abstract class Ability
{
	private static final HashMap<Integer, Ability> abilities = new HashMap<Integer, Ability>();

	public static final AbilityTypeBoost OVERGROW = new AbilityTypeBoost(1, PokemonType.GRASS);
	public static final AbilityTypeBoost BLAZE = new AbilityTypeBoost(2, PokemonType.FIRE);
	public static final AbilityTypeBoost TORRENT = new AbilityTypeBoost(3, PokemonType.WATER);
	public static final AbilityTypeBoost SWARM = new AbilityTypeBoost(4, PokemonType.BUG);

	/** @return The Ability with the input ID. */
	public static Ability find(int id)
	{
		return abilities.get(id);
	}

	/** This Ability's ID. */
	public final int id;

	public Ability(int id)
	{
		this.id = id;
		abilities.put(this.id, this);
	}

	public Message name()
	{
		return new Message("ability." + this.id);
	}

}
