package com.darkxell.common.move;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.move.Move.MoveCategory;
import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.DungeonStats;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.pokemon.PropertyModificator;

/** Object that computes various values when using a move, such as damage or accuracy. */
public class MoveEffectCalculator
{

	public final Floor floor;
	public final PropertyModificator modificator = new PropertyModificator();
	public final MoveUse move;
	public final DungeonPokemon target;

	public MoveEffectCalculator(MoveUse move, DungeonPokemon target, Floor floor)
	{
		this.move = move;
		this.target = target;
		this.floor = floor;

		this.modificator.add(this.move().effect);
		this.modificator.addUser(this.user().ability());
		this.modificator.add(target.ability());
		if (this.user().getItem() != null) this.modificator.addUser(this.user().getItem().item());
		if (target.getItem() != null) this.modificator.add(target.getItem().item());
		this.modificator.add(floor.currentWeather().weather);
	}

	protected double accuracyStat()
	{
		Stat acc = Stat.Accuracy;
		int accStage = move.user.stats.getStage(acc);
		accStage = this.modificator.applyStatStageModifications(acc, accStage, move, target, floor);

		DungeonStats stats = move.user.stats.clone();
		stats.setStage(acc, accStage);
		double accuracy = stats.getStat(acc);
		accuracy = this.modificator.applyStatModifications(acc, accuracy, move, target, floor);
		if (accuracy < 0) accuracy = 0;
		if (accuracy > 999) accuracy = 999;

		return accuracy;
	}

	protected int attackStat()
	{
		Stat atk = move.move.move().category == MoveCategory.Special ? Stat.SpecialAttack : Stat.Attack;
		int atkStage = move.user.stats.getStage(atk);
		atkStage = this.modificator.applyStatStageModifications(atk, atkStage, move, target, floor);

		DungeonStats stats = move.user.stats.clone();
		stats.setStage(atk, atkStage);
		double attack = stats.getStat(atk);
		attack = this.modificator.applyStatModifications(atk, attack, move, target, floor);
		if (attack < 0) attack = 0;
		if (attack > 999) attack = 999;

		return (int) attack;
	}

	public int compute()
	{
		int attack = this.attackStat();
		int defense = this.defenseStat();
		int level = this.user().level();
		int power = this.movePower();
		double wildNerfer = this.user().player() != null ? 1 : 0.75;

		double damage = ((attack + power) * 0.6 - defense / 2 + 50 * Math.log(((attack - defense) / 8 + level + 50) * 10) - 311) * wildNerfer;
		if (damage < 1) damage = 1;
		if (damage > 999) damage = 999;

		double multiplier = this.damageMultiplier(this.criticalLands());
		damage *= multiplier;

		// Damage randomness
		damage *= (9 - floor.random.nextDouble() * 2) / 8;

		return (int) Math.round(damage);
	}

	protected boolean criticalLands()
	{
		int crit = move.move.move().critical;
		crit = this.modificator.applyCriticalRateModifications(crit, move, target, floor);
		if (this.effectiveness() == PokemonType.SUPER_EFFECTIVE && crit > 40) crit = 40;
		return floor.random.nextInt(100) < crit;
	}

	protected double damageMultiplier(boolean critical)
	{
		double multiplier = 1;
		multiplier *= this.effectiveness();
		if (move.isStab()) multiplier *= 1.5;
		if (critical) multiplier *= 1.5;

		multiplier *= this.modificator.damageMultiplier(move, target, floor);
		return multiplier;
	}

	protected int defenseStat()
	{
		Stat def = move.move.move().category == MoveCategory.Special ? Stat.SpecialDefense : Stat.Defense;
		int defStage = target.stats.getStage(def);
		defStage = this.modificator.applyStatStageModifications(def, defStage, move, target, floor);

		DungeonStats stats = target.stats.clone();
		stats.setStage(def, defStage);
		double defense = stats.getStat(def);
		defense = this.modificator.applyStatModifications(def, defense, move, target, floor);
		if (defense < 0) defense = 0;
		if (defense > 999) defense = 999;

		return (int) defense;
	}

	protected double effectiveness()
	{
		double effectiveness = move.move.move().type.effectivenessOn(target.species());
		// Ask for status effects such as Miracle Eye, or Floor effects such as Gravity later
		return effectiveness;
	}

	protected double evasionStat()
	{
		Stat ev = Stat.Evasiveness;
		int evStage = target.stats.getStage(ev);
		evStage = this.modificator.applyStatStageModifications(ev, evStage, move, target, floor);

		DungeonStats stats = target.stats.clone();
		stats.setStage(ev, evStage);
		double evasion = stats.getStat(ev);
		evasion = this.modificator.applyStatModifications(ev, evasion, move, target, floor);
		if (evasion < 0) evasion = 0;
		if (evasion > 999) evasion = 999;

		return evasion;
	}

	/** @param usedMove - The Move used.
	 * @param target - The Pok�mon receiving the Move.
	 * @param floor - The Floor context.
	 * @return True if this Move misses. */
	public boolean misses()
	{
		if (this.user() == target) return false;

		int accuracy = this.move().accuracy;

		double userAccuracy = this.accuracyStat();
		double evasion = this.evasionStat();

		accuracy = (int) (accuracy * userAccuracy * evasion);

		return floor.random.nextDouble() * 100 > accuracy;
	}

	public Move move()
	{
		return this.move.move.move();
	}

	protected int movePower()
	{
		return move.move.move().power + move.move.getAddedLevel();
	}

	public DungeonPokemon user()
	{
		return this.move.user;
	}

}