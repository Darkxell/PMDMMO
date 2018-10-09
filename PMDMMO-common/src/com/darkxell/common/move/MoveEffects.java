package com.darkxell.common.move;

import java.util.Collection;
import java.util.HashMap;

import com.darkxell.common.move.effects.ApplyStatusConditionEffect;
import com.darkxell.common.move.effects.CannotKOEffect;
import com.darkxell.common.move.effects.CantMissEffect;
import com.darkxell.common.move.effects.CompoundEffect;
import com.darkxell.common.move.effects.CopyStatChangesEffect;
import com.darkxell.common.move.effects.CreateFloorStatusEffect;
import com.darkxell.common.move.effects.CureAilmentsEffect;
import com.darkxell.common.move.effects.DestroyTrapEffect;
import com.darkxell.common.move.effects.DoubleDamageEffect;
import com.darkxell.common.move.effects.DoubleIfTargetAilmentEffect;
import com.darkxell.common.move.effects.DrainEffect;
import com.darkxell.common.move.effects.DropItemEffect;
import com.darkxell.common.move.effects.DropsMoneyOnKillEffect;
import com.darkxell.common.move.effects.EscapeDungeonEffect;
import com.darkxell.common.move.effects.FixedDamageEffect;
import com.darkxell.common.move.effects.HPDifferenceDamageEffect;
import com.darkxell.common.move.effects.HPMultiplierEffect;
import com.darkxell.common.move.effects.HPRecoilEffect;
import com.darkxell.common.move.effects.MultipleAttacksEffect;
import com.darkxell.common.move.effects.MultipleAttacksMissingStopsEffect;
import com.darkxell.common.move.effects.MultiplyWeightEffect;
import com.darkxell.common.move.effects.RandomAttacksEffect;
import com.darkxell.common.move.effects.RandomMoveEffect;
import com.darkxell.common.move.effects.RandomStatChangeEffect;
import com.darkxell.common.move.effects.RecoilEffect;
import com.darkxell.common.move.effects.ResetStatEffect;
import com.darkxell.common.move.effects.SelfStatChangeEffect;
import com.darkxell.common.move.effects.SolarBeamEffect;
import com.darkxell.common.move.effects.StatChangeEffect;
import com.darkxell.common.move.effects.StealItemEffect;
import com.darkxell.common.move.effects.StoredDamageEffect;
import com.darkxell.common.move.effects.SwitchWithUserEffect;
import com.darkxell.common.move.effects.TeleportToOtherRoomEffect;
import com.darkxell.common.move.effects.UserLevelDamageEffect;
import com.darkxell.common.move.effects.UserStatChangeEffect;
import com.darkxell.common.move.effects.WeatherChangeEffect;
import com.darkxell.common.move.effects.WeatherHealEffect;
import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.status.FloorStatuses;
import com.darkxell.common.status.StatusConditions;
import com.darkxell.common.weather.Weather;

/** Holds all Move Effects. */
public final class MoveEffects
{
	static final HashMap<Integer, MoveEffect> effects = new HashMap<Integer, MoveEffect>();

	public static final MoveEffect No_additional_effect = new MoveEffect(0);
	public static final MoveEffect Basic_attack = new MoveEffect(1);
	public static final MoveEffect HPRecoil_25 = new HPRecoilEffect(5, 25);
	public static final MoveEffect Inflict_burn_10 = new ApplyStatusConditionEffect(7, StatusConditions.Burn, 10);
	public static final MoveEffect Inflict_focusenergy = new ApplyStatusConditionEffect(10, StatusConditions.Focus_energy, 100);
	public static final MoveEffect Inflict_constricted_fire_10 = new ApplyStatusConditionEffect(15, StatusConditions.Constricted_fire, 10);
	public static final MoveEffect Inflict_constricted_water_10 = new ApplyStatusConditionEffect(16, StatusConditions.Constricted_water, 10);
	public static final MoveEffect Raise_user_attack_10 = new UserStatChangeEffect(22, Stat.Attack, 1, 10);
	public static final MoveEffect Inflict_confused_10 = new ApplyStatusConditionEffect(28, StatusConditions.Confused, 10);
	public static final MoveEffect Inflict_paralysis_15 = new ApplyStatusConditionEffect(35, StatusConditions.Paralyzed, 15);
	public static final MoveEffect Inflict_cringed_10 = new ApplyStatusConditionEffect(36, StatusConditions.Cringed, 10);
	public static final MoveEffect Inflict_cringed_20 = new ApplyStatusConditionEffect(37, StatusConditions.Cringed, 20);
	public static final MoveEffect Inflict_cringed_25 = new ApplyStatusConditionEffect(38, StatusConditions.Cringed, 25);
	public static final MoveEffect Inflict_cringed_40 = new ApplyStatusConditionEffect(40, StatusConditions.Cringed, 40);
	public static final MoveEffect Inflict_confused_30 = new ApplyStatusConditionEffect(41, StatusConditions.Confused, 30);
	public static final MoveEffect Inflict_infatuated = new ApplyStatusConditionEffect(45, StatusConditions.Infatuated, 100);
	public static final MoveEffect Inflict_cringed_35 = new ApplyStatusConditionEffect(47, StatusConditions.Cringed, 35);
	public static final MoveEffect Fixed_65 = new FixedDamageEffect(49, 65);
	public static final MoveEffect Inflict_paralysis_10 = new ApplyStatusConditionEffect(51, StatusConditions.Paralyzed, 10);
	public static final MoveEffect Inflict_asleep = new ApplyStatusConditionEffect(52, StatusConditions.Asleep, 100);
	public static final MoveEffect Inflict_poison = new ApplyStatusConditionEffect(58, StatusConditions.Poisoned, 100);
	public static final MoveEffect Inflict_paralysis = new ApplyStatusConditionEffect(59, StatusConditions.Paralyzed, 100);
	public static final MoveEffect If_hits_lower_attack_defense = new CompoundEffect(62, new SelfStatChangeEffect(-1, Stat.Attack, -1, 100),
			new SelfStatChangeEffect(-1, Stat.Defense, -1, 100));
	public static final MoveEffect Inflict_confused = new ApplyStatusConditionEffect(64, StatusConditions.Confused, 100);
	public static final MoveEffect Raise_attack_Raise_defense;
	public static final MoveEffect Raise_attack = new StatChangeEffect(72, Stat.Attack, 1, 100);
	public static final MoveEffect Inflict_enraged = new ApplyStatusConditionEffect(73, StatusConditions.Enraged, 100);
	public static final MoveEffect Raise_spattack = new StatChangeEffect(76, Stat.SpecialAttack, 1, 100);
	public static final MoveEffect Raise_spdefense_2s = new StatChangeEffect(77, Stat.SpecialDefense, 2, 100);
	public static final MoveEffect Raise_defense = new StatChangeEffect(78, Stat.Defense, 1, 100);
	public static final MoveEffect Raise_defense_2s = new StatChangeEffect(79, Stat.Defense, 2, 100);
	public static final MoveEffect Lower_speed = new StatChangeEffect(85, Stat.Speed, -1, 100);
	public static final MoveEffect Lower_attack = new StatChangeEffect(86, Stat.Attack, -1, 100);
	public static final MoveEffect Lower_defense_2s = new StatChangeEffect(88, Stat.Defense, -2, 100);
	public static final MoveEffect Fixed_userlevel = new UserLevelDamageEffect(89);
	public static final MoveEffect Drain_50percent = new DrainEffect(90, 50);
	public static final MoveEffect Recoil_12percent5 = new RecoilEffect(92, 12.5);
	public static final MoveEffect Solarbeam = new SolarBeamEffect(97);
	public static final MoveEffect Inflict_sleepless = new ApplyStatusConditionEffect(102, StatusConditions.Sleepless, 100);
	public static final MoveEffect Inflict_reflect = new ApplyStatusConditionEffect(105, StatusConditions.Reflect, 100);
	public static final MoveEffect Inflict_safeguard = new ApplyStatusConditionEffect(107, StatusConditions.Safeguard, 100);
	public static final MoveEffect Inflict_lightscreen = new ApplyStatusConditionEffect(109, StatusConditions.Light_screen, 100);
	public static final MoveEffect Attack_5_missingstops = new MultipleAttacksMissingStopsEffect(110, 5, 5);
	public static final MoveEffect Weather_heal = new WeatherHealEffect(113);
	public static final MoveEffect Steal_item = new StealItemEffect(120);
	public static final MoveEffect Raise_speed = new StatChangeEffect(121, Stat.Speed, 1, 100);
	public static final MoveEffect Inflict_revenge = new ApplyStatusConditionEffect(123, StatusConditions.Revenge, 100);
	public static final MoveEffect Double_damage = new DoubleDamageEffect(131);
	public static final MoveEffect Create_watersport = new CreateFloorStatusEffect(137, FloorStatuses.Reduce_fire);
	public static final MoveEffect Lower_defense = new StatChangeEffect(139, Stat.Defense, -1, 100);
	public static final MoveEffect Inflict_leechSeed = new ApplyStatusConditionEffect(143, StatusConditions.Leech_seed, 100);
	public static final MoveEffect Cure_status_ailments = new CureAilmentsEffect(145);
	public static final MoveEffect Inflict_skullbash_Raise_defense;
	public static final MoveEffect Inflict_bide = new ApplyStatusConditionEffect(154, StatusConditions.Bide, 100);
	public static final MoveEffect Lower_accuracy = new StatChangeEffect(155, Stat.Accuracy, -1, 100);
	public static final MoveEffect Cant_ko = new CannotKOEffect(161);
	public static final MoveEffect Fixed_difference_hp = new HPDifferenceDamageEffect(163);
	public static final MoveEffect Switch_position_Raise_random;
	public static final MoveEffect Teleport_other_room = new TeleportToOtherRoomEffect(168);
	public static final MoveEffect Lower_evasion = new StatChangeEffect(179, Stat.Evasiveness, -1, 100);
	public static final MoveEffect Raise_evasion = new StatChangeEffect(180, Stat.Evasiveness, 1, 100);
	public static final MoveEffect Drop_item = new DropItemEffect(181);
	public static final MoveEffect Inflict_petrified = new ApplyStatusConditionEffect(186, StatusConditions.Petrified, 100);
	public static final MoveEffect Use_random_move_on_floor = new RandomMoveEffect(187);
	public static final MoveEffect Inflict_protect = new ApplyStatusConditionEffect(192, StatusConditions.Protect, 100);
	public static final MoveEffect Inflict_taunted = new ApplyStatusConditionEffect(193, StatusConditions.Taunted, 100);
	public static final MoveEffect HP_multiplier = new HPMultiplierEffect(195);
	public static final MoveEffect Inflict_charging = new ApplyStatusConditionEffect(198, StatusConditions.Charging, 100);
	public static final MoveEffect Multiply_target_weight = new MultiplyWeightEffect(200);
	public static final MoveEffect Lower_accuracy_2s = new StatChangeEffect(203, Stat.Accuracy, -2, 100);
	public static final MoveEffect Weather_rain = new WeatherChangeEffect(208, Weather.RAIN);
	public static final MoveEffect Destroy_trap = new DestroyTrapEffect(213);
	public static final MoveEffect Drops_money_on_kill = new DropsMoneyOnKillEffect(218);
	public static final MoveEffect Raise_attack_Raise_spattack = new CompoundEffect(221, Raise_attack, Raise_spattack);
	public static final MoveEffect Lower_attack_2s = new StatChangeEffect(224, Stat.Attack, -2, 100);
	public static final MoveEffect Lower_speed_30 = new StatChangeEffect(225, Stat.Speed, -1, 30);
	public static final MoveEffect Inflict_identified_Reset_evasion;
	public static final MoveEffect Escape_dungeon = new EscapeDungeonEffect(240);
	public static final MoveEffect Inflict_mirrormove = new ApplyStatusConditionEffect(245, StatusConditions.Mirror_move, 100);
	public static final MoveEffect Copy_stat_changes = new CopyStatChangesEffect(254);
	public static final MoveEffect Attack_2to5 = new MultipleAttacksEffect(321, 2, 5);
	public static final MoveEffect Fixed_bide = new StoredDamageEffect(322);
	public static final MoveEffect Cant_miss = new CantMissEffect(323);
	public static final MoveEffect Attack_2 = new MultipleAttacksEffect(324, 2, 2);
	public static final MoveEffect Random_attacks_3 = new RandomAttacksEffect(325, 3);
	public static final MoveEffect Double_target_ailment = new DoubleIfTargetAilmentEffect(326);
	public static final MoveEffect Create_mudsport = new CreateFloorStatusEffect(327, FloorStatuses.Reduce_electric);
	public static final MoveEffect Lower_attack_10 = new StatChangeEffect(328, Stat.Attack, -1, 10);
	public static final MoveEffect Inflict_constricted_10 = new ApplyStatusConditionEffect(329, StatusConditions.Constricted, 10);
	public static final MoveEffect Switch_position = new SwitchWithUserEffect(330);

	static
	{
		Raise_attack_Raise_defense = new CompoundEffect(71, Raise_attack, Raise_defense);
		Inflict_skullbash_Raise_defense = new CompoundEffect(151, Raise_defense, new ApplyStatusConditionEffect(-1, StatusConditions.Skull_bash, 100));
		Switch_position_Raise_random = new CompoundEffect(164, Switch_position, new RandomStatChangeEffect(-1, 1, 100));
		Inflict_identified_Reset_evasion = new CompoundEffect(231, new ResetStatEffect(-1, Stat.Evasiveness),
				new ApplyStatusConditionEffect(-1, StatusConditions.Identified, 100));
	}

	/** @return The Effect with the input ID. */
	public static MoveEffect find(int id)
	{
		if (!effects.containsKey(id)) return No_additional_effect;
		return effects.get(id);
	}

	/** @return All Effects. */
	public static Collection<MoveEffect> list()
	{
		return effects.values();
	}

}
