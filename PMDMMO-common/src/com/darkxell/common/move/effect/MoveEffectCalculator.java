package com.darkxell.common.move.effect;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.Event.MessageEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveCategory;
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
public class MoveEffectCalculator {

    private double effectiveness = -1;
    public final String[] flags;
    public final Floor floor;
    public final PropertyModificator modificator = new PropertyModificator();
    public final MoveUse move;
    public final MoveUseEvent moveEvent;
    public final DungeonPokemon target;

    public MoveEffectCalculator(MoveUseEvent moveEvent) {
        this.moveEvent = moveEvent;
        this.move = this.moveEvent.usedMove;
        this.target = this.moveEvent.target;
        this.floor = this.moveEvent.floor;
        this.flags = this.moveEvent.flags(); // No security issue, is already a copy

        if (this.move().effect() instanceof CompoundEffect)
            for (MoveEffect e : ((CompoundEffect) this.move().effect()).effects)
                this.modificator.add(e);
        else
            this.modificator.add(this.move().effect());
        this.modificator.addUser(this.user().ability());
        if (target != null)
            this.modificator.add(target.ability());
        if (this.user().hasItem())
            this.modificator.addUser(this.user().getItem().item());
        if (target != null && target.hasItem())
            this.modificator.add(target.getItem().item());
        this.modificator.add(floor.currentWeather().weather);
        for (AppliedStatusCondition s : this.move.user.activeStatusConditions())
            this.modificator.addUser(s.condition);
        if (target != null)
            for (AppliedStatusCondition s : this.target.activeStatusConditions())
                this.modificator.add(s.condition);
        for (ActiveFloorStatus status : floor.activeStatuses())
            this.modificator.add(status.status);
    }

    protected double accuracyStat(ArrayList<Event> events) {
        Stat acc = Stat.Accuracy;
        int accStage = move.user.stats.getStage(acc);
        accStage = this.modificator.applyStatStageModifications(acc, accStage, move, target, floor, events);

        DungeonStats stats = move.user.stats.clone();
        stats.setStage(acc, accStage);
        double accuracy = stats.getStat(acc);
        accuracy = this.modificator.applyStatModifications(acc, accuracy, move, target, floor, moveEvent, events);
        if (accuracy < 0)
            accuracy = 0;
        if (accuracy > 999)
            accuracy = 999;

        return accuracy;
    }

    protected int attackStat(ArrayList<Event> events) {
        Stat atk = this.move().category == MoveCategory.Special ? Stat.SpecialAttack : Stat.Attack;
        int atkStage = move.user.stats.getStage(atk);
        atkStage = this.modificator.applyStatStageModifications(atk, atkStage, move, target, floor, events);

        DungeonStats stats = move.user.stats.clone();
        stats.setStage(atk, atkStage);
        double attack = stats.getStat(atk);
        attack = this.modificator.applyStatModifications(atk, attack, move, target, floor, moveEvent, events);
        if (attack < 0)
            attack = 0;
        if (attack > 999)
            attack = 999;

        return (int) attack;
    }

    public int compute(ArrayList<Event> events) {
        int attack = this.attackStat(events);
        int defense = this.defenseStat(events);
        int level = this.user().level();
        int power = this.movePower();
        double wildNerfer = this.user().isEnemy() ? 1 : 0.75;

        double damage = ((attack + power) * 0.6 - defense / 2
                + 50 * Math.log(((attack - defense) / 8 + level + 50) * 10) - 311) * wildNerfer;
        if (damage < 1)
            damage = 1;
        if (damage > 999)
            damage = 999;

        double multiplier = this.damageMultiplier(this.criticalLands(events), events);
        damage *= multiplier;

        // Damage randomness
        damage *= (9 - floor.random.nextDouble() * 2) / 8;

        damage = this.modificator.applyDamageModifications(damage, moveEvent, events);

        return (int) Math.round(damage);
    }

    protected double computeEffectiveness() {
        double effectiveness = PokemonType.NORMALLY_EFFECTIVE;
        if (this.target != null)
            effectiveness = this.type().effectivenessOn(target.species());
        effectiveness = this.modificator.applyEffectivenessModifications(effectiveness, move, target, floor);
        return effectiveness;
    }

    protected boolean criticalLands(ArrayList<Event> events) {
        int crit = move.move.move().critical;
        crit = this.modificator.applyCriticalRateModifications(crit, move, target, floor, events);
        if (this.effectiveness() == PokemonType.SUPER_EFFECTIVE && crit > 40)
            crit = 40;
        return floor.random.nextInt(100) < crit;
    }

    protected double damageMultiplier(boolean critical, ArrayList<Event> events) {
        double multiplier = 1;
        multiplier *= this.effectiveness();
        if (move.isStab())
            multiplier *= 1.5;
        if (critical) {
            multiplier *= 1.5;
            events.add(new MessageEvent(this.floor, this.moveEvent, new Message("move.critical")));
        }

        multiplier *= this.modificator.damageMultiplier(this.moveEvent, events);
        return multiplier;
    }

    protected int defenseStat(ArrayList<Event> events) {
        Stat def = move.move.move().category == MoveCategory.Special ? Stat.SpecialDefense : Stat.Defense;
        int defStage = target.stats.getStage(def);
        defStage = this.modificator.applyStatStageModifications(def, defStage, move, target, floor, events);

        DungeonStats stats = target.stats.clone();
        stats.setStage(def, defStage);
        double defense = stats.getStat(def);
        defense = this.modificator.applyStatModifications(def, defense, move, target, floor, moveEvent, events);
        if (defense < 0)
            defense = 0;
        if (defense > 999)
            defense = 999;

        return (int) defense;
    }

    public double effectiveness() {
        if (this.effectiveness == -1)
            this.effectiveness = this.computeEffectiveness();
        return this.effectiveness;
    }

    protected double evasionStat(ArrayList<Event> events) {
        Stat ev = Stat.Evasiveness;
        int evStage = target.stats.getStage(ev);
        evStage = this.modificator.applyStatStageModifications(ev, evStage, move, target, floor, events);

        DungeonStats stats = target.stats.clone();
        stats.setStage(ev, evStage);
        double evasion = stats.getStat(ev);
        evasion = this.modificator.applyStatModifications(ev, evasion, move, target, floor, moveEvent, events);
        if (evasion < 0)
            evasion = 0;
        if (evasion > 999)
            evasion = 999;

        // Reminder : Great evasion value actually means the monster is LESS likely to evade.

        return evasion;
    }

    /**
     * @param  usedMove - The Move used.
     * @param  target   - The Pokemon receiving the Move.
     * @param  floor    - The Floor context.
     * @return          True if this Move misses.
     */
    public boolean misses(ArrayList<Event> events) {
        if (this.target == null)
            return false;

        int accuracy = this.move().accuracy;

        double userAccuracy = this.accuracyStat(events);
        double evasion = this.evasionStat(events);

        accuracy = (int) (accuracy * userAccuracy * evasion);

        return floor.random.nextDouble() * 100 > accuracy; // ITS SUPERIOR because you return 'MISSES'
    }

    public Move move() {
        return this.move.move.move();
    }

    protected int movePower() {
        return this.move().power + move.move.getAddedLevel();
    }

    private PokemonType type() {
        return this.move().getType(this.move.user.originalPokemon);
    }

    public DungeonPokemon user() {
        return this.move.user;
    }

}
