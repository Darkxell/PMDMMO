package com.darkxell.common.move.behavior;

import static com.darkxell.common.move.effect.MoveEffectBuilder.*;

import java.util.Collection;
import java.util.HashMap;

import com.darkxell.common.model.pokemon.Stat;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.move.effects.*;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.status.FloorStatuses;
import com.darkxell.common.status.StatusConditions;
import com.darkxell.common.weather.Weather;

/** Holds all Move Behaviors. */
public final class MoveBehaviors {
    static final HashMap<Integer, MoveBehavior> behaviors = new HashMap<>();

    // When Scald, check Frozen
    // When Dive, check Whirlpool
    // When Fly or Bounce, check Gust
    // When Dig, check Earthquake and Magnitude
    // When Sketch, check Mimic

    public static final MoveBehavior Raise_spdefense = build(-1, specialDefense(1));
    public static final MoveBehavior No_additional_effect = build(0);
    public static final MoveBehavior Basic_attack = build(1);
    public static final MoveBehavior HPRecoil_25 = build(5, new HPRecoilEffect(25));
    public static final MoveBehavior Inflict_burn_10 = build(7, status(StatusConditions.Burn, 10));
    public static final MoveBehavior Inflict_frozen_10 = build(8, status(StatusConditions.Frozen, 10));
    public static final MoveBehavior Inflict_focusenergy = build(10, status(StatusConditions.Focus_energy));
    public static final MoveBehavior One_hit_ko = build(12, new FixedDamageEffect(9999));
    public static final MoveBehavior Inflict_constricted_fire_10 = build(15,
            status(StatusConditions.Constricted_fire, 10));
    public static final MoveBehavior Inflict_constricted_water_10 = build(16,
            status(StatusConditions.Constricted_water, 10));
    public static final MoveBehavior Inflict_poisoned_18 = build(19, status(StatusConditions.Poisoned, 18));
    public static final MoveBehavior Lowers_defense_10percent = build(21, new StatChangeEffect(Stat.Defense, 1, 10));
    public static final MoveBehavior Raise_user_attack_10 = build(22, new UserStatChangeEffect(Stat.Attack, 1, 10));
    public static final MoveBehavior Inflict_confused_10 = build(28, status(StatusConditions.Confused, 10));
    public static final MoveBehavior Inflict_paralysis_15 = build(35, status(StatusConditions.Paralyzed, 15));
    public static final MoveBehavior Inflict_cringed_10 = build(36, status(StatusConditions.Cringed, 10));
    public static final MoveBehavior Inflict_cringed_20 = build(37, status(StatusConditions.Cringed, 20));
    public static final MoveBehavior Inflict_cringed_25 = build(38, status(StatusConditions.Cringed, 25));
    public static final MoveBehavior Inflict_cringed_30 = build(39, status(StatusConditions.Cringed, 39));
    public static final MoveBehavior Inflict_cringed_40 = build(40, status(StatusConditions.Cringed, 40));
    public static final MoveBehavior Inflict_confused_30 = build(41, status(StatusConditions.Confused, 30));
    public static final MoveBehavior Inflict_infatuated = build(45, status(StatusConditions.Infatuated));
    public static final MoveBehavior Inflict_cringed_35 = build(47, status(StatusConditions.Cringed, 35));
    public static final MoveBehavior Fixed_55 = build(48, new FixedDamageEffect(55));
    public static final MoveBehavior Fixed_65 = build(49, new FixedDamageEffect(65));
    public static final MoveBehavior Inflict_paralysis_10 = build(51, status(StatusConditions.Paralyzed, 10));
    public static final MoveBehavior Inflict_asleep = build(52, status(StatusConditions.Asleep));
    public static final MoveBehavior Cant_miss_Inflict_yawning;
    public static final MoveBehavior Inflict_immobilized = build(55, status(StatusConditions.Immobilized));
    public static final MoveBehavior Inflict_poison = build(58, status(StatusConditions.Poisoned));
    public static final MoveBehavior Inflict_paralysis = build(59, status(StatusConditions.Paralyzed));
    public static final MoveBehavior If_hits_lower_attack_defense = build(62,
            new SelfStatChangeEffect(Stat.Attack, -1, 100), new SelfStatChangeEffect(Stat.Defense, -1, 100));
    public static final MoveBehavior Inflict_confused = build(64, status(StatusConditions.Confused));
    public static final MoveBehavior Lower_spdefense_2s = build(65, specialDefense(-2));
    public static final MoveBehavior Blowback_random = build(66, new BlowbackRandomEffect());
    public static final MoveBehavior Raise_attack_Raise_defense;
    public static final MoveBehavior Raise_attack = build(72, attack(1));
    public static final MoveBehavior Inflict_enraged = build(73, status(StatusConditions.Enraged));
    public static final MoveBehavior Raise_attack_2s = build(74, attack(2));
    public static final MoveBehavior Raise_spattack_Raise_spdefense;
    public static final MoveBehavior Raise_spattack = build(76, specialAttack(1));
    public static final MoveBehavior Raise_spdefense_2s = build(77, specialDefense(2));
    public static final MoveBehavior Raise_defense = build(78, defense(1));
    public static final MoveBehavior Raise_defense_2s = build(79, defense(2));
    public static final MoveBehavior Lower_speed = build(85, speed(-1));
    public static final MoveBehavior Lower_attack = build(86, attack(-1));
    public static final MoveBehavior Lower_attack_2s = build(87, attack(-2));
    public static final MoveBehavior Lower_defense_2s = build(88, defense(-2));
    public static final MoveBehavior Fixed_userlevel = build(89, new UserLevelDamageEffect());
    public static final MoveBehavior Drain_50percent = build(90, new DrainEffect(50));
    public static final MoveBehavior Recoil_12percent5 = build(92, new RecoilEffect(12.5));
    public static final MoveBehavior Raise_attack_2s_Inflict_confused;
    public static final MoveBehavior Solarbeam = build(97, new SolarBeamEffect());
    public static final MoveBehavior Wrap = build(101, new WrapStatusConditionEffect());
    public static final MoveBehavior Inflict_sleepless = build(102, status(StatusConditions.Sleepless));
    public static final MoveBehavior Inflict_reflect = build(105, status(StatusConditions.Reflect));
    public static final MoveBehavior Inflict_safeguard = build(107, status(StatusConditions.Safeguard));
    public static final MoveBehavior Inflict_lightscreen = build(109, status(StatusConditions.Light_screen));
    public static final MoveBehavior Attack_5_missingstops = build(110, new MultipleAttacksMissingStopsEffect(5, 5));
    public static final MoveBehavior Weather_heal = build(113, new WeatherHealEffect());
    public static final MoveBehavior Inflict_asleep_Heal_max_Heal_all_ailments;
    public static final MoveBehavior Heal_50percent = build(116, new HealEffect(.5));
    public static final MoveBehavior Steal_item = build(120, new StealItemEffect());
    public static final MoveBehavior Raise_speed = build(121, speed(1));
    public static final MoveBehavior Inflict_revenge = build(123, status(StatusConditions.Revenge));
    public static final MoveBehavior Ifghost_504_505 = build(127, new ConditionalEffect(-504, -505,
            (moveEvent, events) -> moveEvent.usedMove().user.species().isType(PokemonType.Ghost)));
    public static final MoveBehavior Double_damage_Hurt_user_if_misses;
    public static final MoveBehavior Change_type_with_user_id = build(130, new ChangeTypeWithUserId());
    public static final MoveBehavior Double_damage = build(131, new DoubleDamageEffect());
    public static final MoveBehavior Create_watersport = build(137,
            new CreateFloorStatusEffect(FloorStatuses.Reduce_fire));
    public static final MoveBehavior Lower_defense_30 = build(138, new StatChangeEffect(Stat.Defense, -1, 30));
    public static final MoveBehavior Lower_defense = build(139, defense(-1));
    public static final MoveBehavior Inflict_ingrained = build(141, status(StatusConditions.Ingrained));
    public static final MoveBehavior Inflict_leechSeed = build(143, status(StatusConditions.Leech_seed));
    public static final MoveBehavior Cure_status_ailments = build(145, new CureAilmentsEffect());
    public static final MoveBehavior Random_fixed_damage = build(150,
            new RandomFixedDamageEffect(150, 5, 10, 15, 20, 25, 30, 35, 40));
    public static final MoveBehavior Inflict_skullbash_Raise_defense;
    public static final MoveBehavior Inflict_bide = build(154, status(StatusConditions.Bide));
    public static final MoveBehavior Lower_accuracy = build(155, accuracy(-1));
    public static final MoveBehavior Lower_spdefense_3s = build(158, specialDefense(-3));
    public static final MoveBehavior Half_target_hp_damage = build(160, new HalfTargetHPDamageEffect());
    public static final MoveBehavior Cant_ko = build(161, new CannotKOEffect());
    public static final MoveBehavior Fixed_difference_hp = build(163, new HPDifferenceDamageEffect());
    public static final MoveBehavior Switch_position_Raise_random;
    public static final MoveBehavior Teleport_other_room = build(168, new TeleportToOtherRoomEffect());
    public static final MoveBehavior Break_lightscreen_Break_reflect;
    public static final MoveBehavior Inflict_Sureshot = build(173, status(StatusConditions.Sure_shot));
    public static final MoveBehavior Lower_evasion = build(179, evasiveness(-1));
    public static final MoveBehavior Raise_evasion = build(180, evasiveness(1));
    public static final MoveBehavior Drop_item = build(181, new DropItemEffect());
    public static final MoveBehavior Inflict_petrified = build(186, status(StatusConditions.Petrified));
    public static final MoveBehavior Use_random_move_on_floor = build(187, new RandomMoveEffect());
    public static final MoveBehavior Inflict_captivating = build(190, status(StatusConditions.Captivating));
    public static final MoveBehavior Inflict_protect = build(192, status(StatusConditions.Protect));
    public static final MoveBehavior Inflict_taunted = build(193, status(StatusConditions.Taunted));
    public static final MoveBehavior HP_multiplier = build(195, new HPMultiplierEffect());
    public static final MoveBehavior Deal_half_hp_self_Destroy_surrounding_tiles;
    public static final MoveBehavior Deal_half_hp_self_Destroy_surrounding_2_tiles;
    public static final MoveBehavior Inflict_charging = build(198, status(StatusConditions.Charging));
    public static final MoveBehavior Multiply_target_weight = build(200, new MultiplyWeightEffect());
    public static final MoveBehavior Lower_accuracy_2s = build(203, accuracy(-2));
    public static final MoveBehavior Weather_rain = build(208, new WeatherChangeEffect(Weather.RAIN));
    public static final MoveBehavior Zeroes_pp = build(209, new SetPPtoZeroEffect());
    public static final MoveBehavior Inflict_mirror_coat = build(211, status(StatusConditions.Mirror_coat));
    public static final MoveBehavior Destroy_trap = build(213, new DestroyTrapEffect());
    public static final MoveBehavior Inflict_encore = build(215, status(StatusConditions.Encore));
    public static final MoveBehavior Weather_sunny = build(217, new WeatherChangeEffect(Weather.SUNNY));
    public static final MoveBehavior Drops_money_on_kill = build(218, new DropsMoneyOnKillEffect());
    public static final MoveBehavior Raise_attack_Raise_spattack = build(221, attack(1), specialAttack(1));
    public static final MoveBehavior Deal_half_max_hp_Raise_attack_20s;
    public static final MoveBehavior Lower_speed_30 = build(225, new StatChangeEffect(Stat.Speed, -1, 30));
    public static final MoveBehavior Inflict_identified_Reset_evasion = build(231, status(StatusConditions.Identified));
    public static final MoveBehavior Escape_dungeon = build(240, new EscapeDungeonEffect());
    public static final MoveBehavior Inflict_mirrormove = build(254, status(StatusConditions.Mirror_move));
    public static final MoveBehavior Copy_stat_changes = build(254, new CopyStatChangesEffect());
    public static final MoveBehavior Copy_last_move = build(263, new CopyLastMoveEffect());
    public static final MoveBehavior Attack_2to5 = build(321, new MultipleAttacksEffect(2, 5));
    public static final MoveBehavior Fixed_bide = build(322, new StoredDamageEffect());
    public static final MoveBehavior Cant_miss = build(323, new CantMissEffect());
    public static final MoveBehavior Attack_2 = build(324, new MultipleAttacksEffect(2, 2));
    public static final MoveBehavior Random_attacks_3 = build(325, new RandomAttacksEffect(3));
    public static final MoveBehavior Double_target_ailment = build(326, new DoubleIfTargetAilmentEffect());
    public static final MoveBehavior Create_mudsport = build(327,
            new CreateFloorStatusEffect(FloorStatuses.Reduce_electric));
    public static final MoveBehavior Lower_attack_10 = build(328, new StatChangeEffect(Stat.Attack, -1, 10));
    public static final MoveBehavior Inflict_constricted_10 = build(329, status(StatusConditions.Constricted, 10));
    public static final MoveBehavior Switch_position = build(330, new SwitchWithUserEffect());
    public static final MoveBehavior Blowback = build(331, new BlowbackEffect());
    public static final MoveBehavior Inflict_curse_Lose_user25;
    public static final MoveBehavior Raise_attack_defense_Lower_speed;

    static {
        Cant_miss_Inflict_yawning = build(53, new CantMissEffect(), status(StatusConditions.Yawning));
        Raise_attack_Raise_defense = build(71, attack(1), defense(1));
        Raise_spattack_Raise_spdefense = build(75, specialAttack(1), specialDefense(1));
        Raise_attack_2s_Inflict_confused = build(94, attack(1), status(StatusConditions.Confused));
        Inflict_asleep_Heal_max_Heal_all_ailments = build(115, status(StatusConditions.Asleep), new HealEffect(1),
                new CureAilmentsEffect());
        Double_damage_Hurt_user_if_misses = build(128, new DoubleDamageEffect(), new DealDamageToUserIfMissEffect(.5));
        Inflict_skullbash_Raise_defense = build(151, defense(1), status(StatusConditions.Skull_bash));
        Switch_position_Raise_random = build(164, new SwitchWithUserEffect(), new RandomStatChangeEffect(1, 100));
        Break_lightscreen_Break_reflect = build(171,
                new RemoveStatusConditionBeforeDamageEffect(StatusConditions.Light_screen),
                new RemoveStatusConditionBeforeDamageEffect(StatusConditions.Reflect));
        Deal_half_hp_self_Destroy_surrounding_tiles = build(196, new DealHpMultiplierDamageToSelfEffect(.5),
                new DestroySurroundingTilesEffect(1));
        Deal_half_hp_self_Destroy_surrounding_2_tiles = build(197, new DealHpMultiplierDamageToSelfEffect(.5),
                new DestroySurroundingTilesEffect(2));
        Deal_half_max_hp_Raise_attack_20s = build(222, new DealMaxHpMultiplierDamageEffect(.5), attack(20));
        Inflict_curse_Lose_user25 = build(332, new UserPercentDamageEffect(.25), status(StatusConditions.Cursed));
        Raise_attack_defense_Lower_speed = build(333, attack(1), defense(1), speed(-1));
    }

    private static MoveBehavior build(int id, MoveEffect... effects) {
        return new MoveBehavior(id, effects);
    }

    /** @return The Effect with the input ID. */
    public static MoveBehavior find(int id) {
        if (!behaviors.containsKey(id))
            return No_additional_effect;
        return behaviors.get(id);
    }

    /** @return All Effects. */
    public static Collection<MoveBehavior> list() {
        return behaviors.values();
    }

    public static void register(MoveBehavior behavior) {
        behaviors.put(behavior.id, behavior);
    }

}
