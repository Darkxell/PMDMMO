package com.darkxell.common.pokemon.ability;

import java.util.HashMap;

import com.darkxell.common.event.DungeonEventListener;
import com.darkxell.common.pokemon.AffectsPokemon;
import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.status.StatusCondition;
import com.darkxell.common.util.language.Lang;
import com.darkxell.common.util.language.Message;
import com.darkxell.common.weather.Weather;

public abstract class Ability implements AffectsPokemon, DungeonEventListener
{
	private static final HashMap<Integer, Ability> abilities = new HashMap<Integer, Ability>();

	public static final Ability BLAZE = new AbilityTypeBoost(0, PokemonType.Fire);
	public static final Ability OVERGROW = new AbilityTypeBoost(1, PokemonType.Grass);
	public static final Ability SWARM = new AbilityTypeBoost(2, PokemonType.Bug);
	public static final Ability TORRENT = new AbilityTypeBoost(3, PokemonType.Water);

	public static final Ability CHLOROPHYLL = new AbilityDoubleAttacks(4, Weather.SUNNY);

	public static final Ability LEVITATE = new AbilityNullifyType(16, PokemonType.Ground);
	public static final Ability SOUNDPROOF = new AbilityNullifySound(19);
	public static final Ability WONDER_GUARD = new AbilityNullifyNonSupEff(21);

	public static final Ability POISON_POINT = new AbilityStatusOnHit(36, StatusCondition.Poisoned, 12);

	public static final Ability KEEN_EYE = new AbilityPreventsStatLoss(46, Stat.Accuracy);

	public static final Ability SHIELD_DUST = new AbilityPreventAdditionalEffectsOnSelf(58);

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

	public boolean hasTriggeredMessage()
	{
		return Lang.containsKey("ability.trigger." + this.id);
	}

	public Message name()
	{
		return new Message("ability." + this.id);
	}

	public Message triggeredMessage(DungeonPokemon pokemon)
	{
		return new Message("ability.trigger." + this.id).addReplacement("<pokemon>", pokemon.getNickname());
	}

}
