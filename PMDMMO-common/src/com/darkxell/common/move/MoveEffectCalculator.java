package com.darkxell.common.move;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.DungeonEvent.MessageEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.move.Move.MoveCategory;
import com.darkxell.common.move.effects.CompoundEffect;
import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.DungeonStats;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.pokemon.PropertyModificator;
import com.darkxell.common.status.ActiveFloorStatus;
import com.darkxell.common.status.AppliedStatusCondition;
import com.darkxell.common.util.language.Message;

/** Object that computes various values when using a move, such as damage or accuracy. */
public class MoveEffectCalculator
{

	private double effectiveness = -1;
	public final String[] flags;
	public final Floor floor;
	public final PropertyModificator modificator = new PropertyModificator();
	public final MoveUse move;
	public final DungeonPokemon target;

	public MoveEffectCalculator(MoveUse move, DungeonPokemon target, Floor floor, String[] flags)
	{
		this.move = move;
		this.target = target;
		this.floor = floor;
		this.flags = flags;

		if (this.move().effect() instanceof CompoundEffect) for (MoveEffect e : ((CompoundEffect) this.move().effect()).effects)
			this.modificator.add(e);
		else this.modificator.add(this.move().effect());
		this.modificator.addUser(this.user().ability());
		if (target != null) this.modificator.add(target.ability());
		if (this.user().getItem() != null) this.modificator.addUser(this.user().getItem().item());
		if (target != null && target.getItem() != null) this.modificator.add(target.getItem().item());
		this.modificator.add(floor.currentWeather().weather);
		for (AppliedStatusCondition s : this.move.user.activeStatusConditions())
			this.modificator.addUser(s.condition);
		if (target != null) for (AppliedStatusCondition s : this.target.activeStatusConditions())
			this.modificator.add(s.condition);
		for (ActiveFloorStatus status : floor.activeStatuses())
			this.modificator.add(status.status);
	}

	protected double accuracyStat(ArrayList<DungeonEvent> events)
	{
		Stat acc = Stat.Accuracy;
		int accStage = move.user.stats.getStage(acc);
		accStage = this.modificator.applyStatStageModifications(acc, accStage, move, target, floor, events);

		DungeonStats stats = move.user.stats.clone();
		stats.setStage(acc, accStage);
		double accuracy = stats.getStat(acc);
		accuracy = this.modificator.applyStatModifications(acc, accuracy, move, target, floor, events);
		if (accuracy < 0) accuracy = 0;
		if (accuracy > 999) accuracy = 999;

		return accuracy;
	}

	protected int attackStat(ArrayList<DungeonEvent> events)
	{
		Stat atk = move.move.move().category == MoveCategory.Special ? Stat.SpecialAttack : Stat.Attack;
		int atkStage = move.user.stats.getStage(atk);
		atkStage = this.modificator.applyStatStageModifications(atk, atkStage, move, target, floor, events);

		DungeonStats stats = move.user.stats.clone();
		stats.setStage(atk, atkStage);
		double attack = stats.getStat(atk);
		attack = this.modificator.applyStatModifications(atk, attack, move, target, floor, events);
		if (attack < 0) attack = 0;
		if (attack > 999) attack = 999;

		return (int) attack;
	}

	public int compute(ArrayList<DungeonEvent> events)
	{
		int attack = this.attackStat(events);
		int defense = this.defenseStat(events);
		int level = this.user().level();
		int power = this.movePower();
		double wildNerfer = this.user().isEnemy() ? 1 : 0.75;

		double damage = ((attack + power) * 0.6 - defense / 2 + 50 * Math.log(((attack - defense) / 8 + level + 50) * 10) - 311) * wildNerfer;
		if (damage < 1) damage = 1;
		if (damage > 999) damage = 999;

		double multiplier = this.damageMultiplier(this.criticalLands(events), events);
		damage *= multiplier;

		// Damage randomness
		damage *= (9 - floor.random.nextDouble() * 2) / 8;

		damage = this.modificator.applyDamageModifications(damage, this.move, this.target, this.floor, events);

		return (int) Math.round(damage);
	}

	protected double computeEffectiveness()
	{
		double effectiveness = PokemonType.NORMALLY_EFFECTIVE;
		if (this.target != null) effectiveness = move.move.move().type.effectivenessOn(target.species());
		effectiveness = this.modificator.applyEffectivenessModifications(effectiveness, move, target, floor);
		return effectiveness;
	}

	protected boolean criticalLands(ArrayList<DungeonEvent> events)
	{
		int crit = move.move.move().critical;
		crit = this.modificator.applyCriticalRateModifications(crit, move, target, floor, events);
		if (this.effectiveness() == PokemonType.SUPER_EFFECTIVE && crit > 40) crit = 40;
		return floor.random.nextInt(100) < crit;
	}

	protected double damageMultiplier(boolean critical, ArrayList<DungeonEvent> events)
	{
		double multiplier = 1;
		multiplier *= this.effectiveness();
		if (move.isStab()) multiplier *= 1.5;
		if (critical)
		{
			multiplier *= 1.5;
			events.add(new MessageEvent(this.floor, new Message("move.critical")));
		}

		multiplier *= this.modificator.damageMultiplier(this.move, this.target, this.floor, this.flags, events);
		return multiplier;
	}

	protected int defenseStat(ArrayList<DungeonEvent> events)
	{
		Stat def = move.move.move().category == MoveCategory.Special ? Stat.SpecialDefense : Stat.Defense;
		int defStage = target.stats.getStage(def);
		defStage = this.modificator.applyStatStageModifications(def, defStage, move, target, floor, events);

		DungeonStats stats = target.stats.clone();
		stats.setStage(def, defStage);
		double defense = stats.getStat(def);
		defense = this.modificator.applyStatModifications(def, defense, move, target, floor, events);
		if (defense < 0) defense = 0;
		if (defense > 999) defense = 999;

		return (int) defense;
	}

	public double effectiveness()
	{
		if (this.effectiveness == -1) this.effectiveness = this.computeEffectiveness();
		return this.effectiveness;
	}

	protected double evasionStat(ArrayList<DungeonEvent> events)
	{
		Stat ev = Stat.Evasiveness;
		int evStage = target.stats.getStage(ev);
		evStage = this.modificator.applyStatStageModifications(ev, evStage, move, target, floor, events);

		DungeonStats stats = target.stats.clone();
		stats.setStage(ev, evStage);
		double evasion = stats.getStat(ev);
		evasion = this.modificator.applyStatModifications(ev, evasion, move, target, floor, events);
		if (evasion < 0) evasion = 0;
		if (evasion > 999) evasion = 999;

		return evasion;
	}

	/** @param usedMove - The Move used.
	 * @param target - The Pokemon receiving the Move.
	 * @param floor - The Floor context.
	 * @return True if this Move misses. */
	public boolean misses(ArrayList<DungeonEvent> events)
	{
		if (this.target == null) return false;

		int accuracy = this.move().accuracy;

		double userAccuracy = this.accuracyStat(events);
		double evasion = this.evasionStat(events);

		accuracy = (int) (accuracy * userAccuracy * evasion);

		return floor.random.nextDouble() * 100 > accuracy; // ITS SUPERIOR because you return 'MISSES'
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
