package com.darkxell.common.pokemon.ability;

import java.util.HashMap;

import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.util.language.Message;

public abstract class Ability
{
	private static final HashMap<Integer, Ability> abilities = new HashMap<Integer, Ability>();

	public static final AbilityTypeBoost BLAZE = new AbilityTypeBoost(1, PokemonType.FIRE);
	public static final AbilityTypeBoost OVERGROW = new AbilityTypeBoost(2, PokemonType.GRASS);
	public static final AbilityTypeBoost SWARM = new AbilityTypeBoost(3, PokemonType.BUG);
	public static final AbilityTypeBoost TORRENT = new AbilityTypeBoost(4, PokemonType.WATER);

	/** @return The Ability with the input ID. */
	public static Ability find(int id)
	{
		if (!abilities.containsKey(id)) return OVERGROW;
		return abilities.get(id);
	}

	/** This Ability's ID. */
	public final int id;

	public Ability(int id)
	{
		this.id = id;
		abilities.put(this.id, this);
	}

	public Message description()
	{
		Message d = this.name();
		d.addPrefix("<red>");
		d.addSuffix("</color>");
		d.addSuffix(": ");
		d.addSuffix(new Message("ability.info." + this.id));
		return d;
	}

	public Message name()
	{
		return new Message("ability." + this.id);
	}

}
