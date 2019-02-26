package com.darkxell.common.pokemon.ability;

import java.util.ArrayList;
import java.util.HashMap;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.DungeonEventListener;
import com.darkxell.common.move.Move.MoveCategory;
import com.darkxell.common.pokemon.AffectsPokemon;
import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.status.StatusConditions;
import com.darkxell.common.util.language.Localization;
import com.darkxell.common.util.language.Message;
import com.darkxell.common.weather.Weather;

public abstract class Ability implements AffectsPokemon, DungeonEventListener {
    private static final HashMap<Integer, Ability> abilities = new HashMap<>();

    public static final Ability BLAZE = new AbilityTypeBoost(0, PokemonType.Fire);
    public static final Ability OVERGROW = new AbilityTypeBoost(1, PokemonType.Grass);
    public static final Ability SWARM = new AbilityTypeBoost(2, PokemonType.Bug);
    public static final Ability TORRENT = new AbilityTypeBoost(3, PokemonType.Water);

    public static final Ability CHLOROPHYLL = new AbilityDoubleAttacks(4, Weather.SUNNY);
    public static final Ability GUTS = new AbilityStatBoostWhileAfflicted(6, Stat.Attack);
    public static final AbilityStatBoostWithAlly MINUS = new AbilityStatBoostWithAlly(9, Stat.SpecialAttack, 1.5);
    public static final AbilityStatBoostWithAlly PLUS = new AbilityStatBoostWithAlly(10, Stat.SpecialAttack, 1.5);
    public static final Ability PURE_POWER = new AbilityStatBoost(11, Stat.Attack, 1.5);

    public static final Ability INTIMIDATE = new AbilityMultiplyIncomingDamage(15, MoveCategory.Physical, .8);
    public static final Ability LEVITATE = new AbilityNullifyType(16, PokemonType.Ground);
    public static final Ability SOUNDPROOF = new AbilityNullifySound(19);
    public static final Ability WONDER_GUARD = new AbilityNullifyNonSupEff(21);

    public static final Ability SHED_SKIN = new AbilityClearStatusAilmentOnTurnEnd(28, 50);

    public static final Ability WATER_ABSORB = new AbilityAbsorbDamage(30, PokemonType.Water);
    public static final Ability CUTE_CHARM = new AbilityStatusOnHit(32, StatusConditions.Infatuated, 12);
    public static final Ability EFFECT_SPORE = new AbilityEffectSpore(33);
    public static final Ability POISON_POINT = new AbilityStatusOnHit(36, StatusConditions.Poisoned, 12);
    public static final Ability STATIC = new AbilityStatusOnHit(39, StatusConditions.Paralyzed, 12);

    public static final Ability CLEAR_BODY = new AbilityPreventsAnyStatLoss(41);
    public static final Ability HYPER_CUTTER = new AbilityPreventsStatLoss(42, Stat.Attack);
    public static final Ability INSOMNIA = new AbilityPreventStatus(45, StatusConditions.Asleep,
            StatusConditions.Yawning);
    public static final Ability KEEN_EYE = new AbilityPreventsStatLoss(46, Stat.Accuracy);
    public static final Ability OBLIVIOUS = new AbilityPreventStatus(49, StatusConditions.Infatuated,
            StatusConditions.Taunted);

    public static final Ability ROCK_HEAD = new AbilityPreventRecoilDamage(56);
    public static final Ability SHIELD_DUST = new AbilityPreventAdditionalEffectsOnSelf(58);
    public static final Ability STURDY = new AbilityPreventOneShot(60);

    public static final Ability CLOUD_NINE = new AbilitySetWeather(63, Weather.CLEAR, 36);

    public static final Ability LIGHTNING_ROD = new AbilityPreventMoveUseType(70, PokemonType.Electric);
    public static final Ability PICKUP = new AbilityFindsItemOnFloorStart(71, 12);
    public static final Ability RUNAWAY = new AbilityRunaway(72);
    public static final AbilityTruant TRUANT = new AbilityTruant(75);

    static {
        MINUS.allyAbility = PLUS;
        PLUS.allyAbility = MINUS;
    }

    /** @return The Ability with the input ID. */
    public static Ability find(int id) {
        if (!abilities.containsKey(id))
            return OVERGROW;
        return abilities.get(id);
    }

    /** This Ability's ID. */
    public final int id;

    public Ability(int id) {
        this.id = id;
        abilities.put(this.id, this);
    }

    public Message description() {
        Message d = this.name();
        d.addPrefix("<red>");
        d.addSuffix("</color>");
        d.addSuffix(": ");
        d.addSuffix(new Message("ability.info." + this.id));
        return d;
    }

    public boolean hasTriggeredMessage() {
        return Localization.containsKey("ability.trigger." + this.id);
    }

    public Message name() {
        return new Message("ability." + this.id);
    }

    public void onFloorStart(Floor floor, DungeonPokemon pokemon, ArrayList<Event> events) {
    }

    public void onTurnStart(Floor floor, DungeonPokemon pokemon, ArrayList<Event> events) {
    }

    public Message triggeredMessage(DungeonPokemon pokemon, int messageID) {
        return new Message("ability.trigger." + this.id + (messageID == 0 ? "" : ("." + messageID)))
                .addReplacement("<pokemon>", pokemon.getNickname());
    }

}
