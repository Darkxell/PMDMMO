package com.darkxell.common.move;

import java.util.Collection;
import java.util.HashMap;

import com.darkxell.common.move.effects.ApplyStatusConditionEffect;
import com.darkxell.common.move.effects.DrainEffect;
import com.darkxell.common.move.effects.FixedDamageEffect;
import com.darkxell.common.move.effects.RecoilEffect;
import com.darkxell.common.move.effects.StatChangeEffect;
import com.darkxell.common.move.effects.WeatherHealEffect;
import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.status.StatusCondition;

/** Holds all Move Effects. */
public final class MoveEffects
{
	static final HashMap<Integer, MoveEffect> effects = new HashMap<Integer, MoveEffect>();

	public static final MoveEffect Default = new MoveEffect(0);
	public static final MoveEffect Basic_attack = new MoveEffect(1);
	public static final MoveEffect Inflict_burn_10 = new ApplyStatusConditionEffect(7, StatusCondition.Burn, 10);
	public static final MoveEffect Inflict_constricted_fire_10 = new ApplyStatusConditionEffect(15, StatusCondition.Constricted_fire, 10);
	public static final MoveEffect Fixed_65 = new FixedDamageEffect(49, 65);
	public static final MoveEffect Inflict_asleep = new ApplyStatusConditionEffect(52, StatusCondition.Asleep, 100);
	public static final MoveEffect Inflict_poison = new ApplyStatusConditionEffect(58, StatusCondition.Poisoned, 100);
	public static final MoveEffect Raise_spattack = new StatChangeEffect(76, Stat.SpecialAttack, 1, 100);
	public static final MoveEffect Lower_speed = new StatChangeEffect(85, Stat.Speed, -1, 100);
	public static final MoveEffect Lower_attack = new StatChangeEffect(86, Stat.Attack, -1, 100);
	public static final MoveEffect Drain_50percent = new DrainEffect(90, 50);
	public static final MoveEffect Recoil_12percent5 = new RecoilEffect(92, 12.5);
	public static final MoveEffect Inflict_sleepless = new ApplyStatusConditionEffect(102, StatusCondition.Sleepless, 100);
	public static final MoveEffect WeatherHeal = new WeatherHealEffect(113);
	public static final MoveEffect Lower_defense = new StatChangeEffect(139, Stat.Defense, -1, 100);
	public static final MoveEffect Inflict_leechSeed = new ApplyStatusConditionEffect(143, StatusCondition.Leech_seed, 100);
	public static final MoveEffect Increase_defense = new StatChangeEffect(172, Stat.Defense, 2, 100);
	public static final MoveEffect Lower_evasion = new StatChangeEffect(179, Stat.Evasiveness, -1, 100);
	public static final MoveEffect Lower_accuracy_2s = new StatChangeEffect(203, Stat.Accuracy, -2, 100);
	public static final MoveEffect Lower_speed_30 = new StatChangeEffect(225, Stat.Speed, -1, 30);

	/** @return The Effect with the input ID. */
	public static MoveEffect find(int id)
	{
		if (!effects.containsKey(id)) return Default;
		return effects.get(id);
	}

	/** @return All Effects. */
	public static Collection<MoveEffect> list()
	{
		return effects.values();
	}

}
