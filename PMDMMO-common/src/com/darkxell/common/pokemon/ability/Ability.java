package com.darkxell.common.pokemon.ability;

import java.util.HashMap;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.move.Move;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.util.language.Lang;
import com.darkxell.common.util.language.Message;

public abstract class Ability
{
	private static final HashMap<Integer, Ability> abilities = new HashMap<Integer, Ability>();

	public static final AbilityTypeBoost BLAZE = new AbilityTypeBoost(0, PokemonType.Fire);
	public static final Ability LEVITATE = new Ability(16) {};
	public static final AbilityTypeBoost OVERGROW = new AbilityTypeBoost(1, PokemonType.Grass);
	public static final AbilityTypeBoost SWARM = new AbilityTypeBoost(2, PokemonType.Bug);

	public static final AbilityTypeBoost TORRENT = new AbilityTypeBoost(3, PokemonType.Water);

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

	/** Called when a Pok�mon uses a damaging move. Modifies the attack stat.
	 * 
	 * @param isUser - true if this Ability belongs to the Move's user. */
	public int applyAttackModifications(int attack, Move move, DungeonPokemon user, DungeonPokemon target, boolean isUser, Floor floor)
	{
		return attack;
	}

	/** Called when a Pok�mon uses a damaging move. Modifies the attack stat stage.
	 * 
	 * @param isUser - true if this Ability belongs to the Move's user. */
	public int applyAttackStageModifications(int atkStage, Move move, DungeonPokemon user, DungeonPokemon target, boolean isUser, Floor floor)
	{
		return atkStage;
	}

	/** Called when a Pok�mon uses a damaging move. Modifies the critical hit rate.
	 * 
	 * @param isUser - true if this Ability belongs to the Move's user. */
	public int applyCriticalRateModifications(int critical, Move move, DungeonPokemon user, DungeonPokemon target, boolean isUser, Floor floor)
	{
		return critical;
	}

	/** Called when a Pok�mon uses a damaging move. Modifies the defense stat.
	 * 
	 * @param isUser - true if this Ability belongs to the Move's user. */
	public int applyDefenseModifications(int defense, Move move, DungeonPokemon user, DungeonPokemon target, boolean isUser, Floor floor)
	{
		return defense;
	}

	/** Called when a Pok�mon uses a damaging move. Modifies the defense stat stage.
	 * 
	 * @param isUser - true if this Ability belongs to the Move's user. */
	public int applyDefenseStageModifications(int defStage, Move move, DungeonPokemon user, DungeonPokemon target, boolean isUser, Floor floor)
	{
		return defStage;
	}

	/** Called when a Pok�mon uses a damaging move.
	 * 
	 * @param isUser - true if this Ability belongs to the Move's user.
	 * @return A damage multiplier applied to the final damage value. */
	public double damageMultiplier(MoveUse move, DungeonPokemon user, DungeonPokemon target, boolean isUser, Floor floor)
	{
		return 1;
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
