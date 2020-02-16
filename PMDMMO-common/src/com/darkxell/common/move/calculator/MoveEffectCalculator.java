package com.darkxell.common.move.calculator;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.Event.MessageEvent;
import com.darkxell.common.model.pokemon.Stat;
import com.darkxell.common.move.MoveCategory;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.calculator.CalculatorDamageModule.DefaultDamageModule;
import com.darkxell.common.move.calculator.CalculatorMoveLandingModule.DefaultMoveLandingModule;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.pokemon.DungeonStats;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.pokemon.PropertyModificator;
import com.darkxell.common.status.ActiveFloorStatus;
import com.darkxell.common.status.AppliedStatusCondition;
import com.darkxell.common.util.language.Message;

/** Object that computes various values when using a move, such as damage or accuracy. */
public class MoveEffectCalculator {

    public final MoveContext context;
    private CalculatorDamageModule damageModule = new DefaultDamageModule();
    private double effectiveness = -1;
    public final String[] flags;
    public final PropertyModificator modificator = new PropertyModificator();
    private CalculatorMoveLandingModule moveLandingModule = new DefaultMoveLandingModule();

    public MoveEffectCalculator(MoveContext context) {
        this.context = context;
        this.flags = this.context.event.flags(); // No security issue, is already a copy

        for (MoveEffect e : this.context.move.behavior().effects())
            this.modificator.add(e);
        this.modificator.addUser(this.context.user.ability());
        if (this.context.target != null)
            this.modificator.add(this.context.target.ability());
        if (this.context.user.hasItem())
            this.modificator.addUser(this.context.user.getItem().item());
        if (this.context.target != null && this.context.target.hasItem())
            this.modificator.add(this.context.target.getItem().item());
        this.modificator.add(this.context.floor.currentWeather().weather);
        for (AppliedStatusCondition s : this.context.user.activeStatusConditions())
            this.modificator.addUser(s.condition);
        if (this.context.target != null)
            for (AppliedStatusCondition s : this.context.target.activeStatusConditions())
                this.modificator.add(s.condition);
        for (ActiveFloorStatus status : this.context.floor.activeStatuses())
            this.modificator.add(status.status);
    }

    protected double accuracyStat(ArrayList<Event> events) {
        Stat acc = Stat.Accuracy;
        int accStage = this.context.user.stats.getStage(acc);
        accStage = this.modificator.applyStatStageModifications(acc, accStage, this.context, events);

        DungeonStats stats = this.context.user.stats.clone();
        stats.setStage(acc, accStage);
        double accuracy = stats.getStat(acc);
        accuracy = this.modificator.applyStatModifications(acc, accuracy, this.context, events);
        if (accuracy < 0)
            accuracy = 0;
        if (accuracy > 999)
            accuracy = 999;

        return accuracy;
    }

    protected int attackStat(ArrayList<Event> events) {
        Stat atk = this.context.move.getCategory() == MoveCategory.Special ? Stat.SpecialAttack : Stat.Attack;
        int atkStage = this.context.user.stats.getStage(atk);
        atkStage = this.modificator.applyStatStageModifications(atk, atkStage, this.context, events);

        DungeonStats stats = this.context.user.stats.clone();
        stats.setStage(atk, atkStage);
        double attack = stats.getStat(atk);
        attack = this.modificator.applyStatModifications(atk, attack, context, events);
        if (attack < 0)
            attack = 0;
        if (attack > 999)
            attack = 999;

        return (int) attack;
    }

    public int compute(ArrayList<Event> events) {
        return this.damageModule.compute(this.context, this, events);
    }

    protected double computeEffectiveness() {
        double effectiveness = PokemonType.NORMALLY_EFFECTIVE;
        if (this.context.target != null)
            effectiveness = this.type().effectivenessOn(this.context.target.species());
        effectiveness = this.modificator.applyEffectivenessModifications(effectiveness, this.context);
        return effectiveness;
    }

    protected boolean criticalLands(ArrayList<Event> events) {
        int crit = this.context.move.getCritical();
        crit = this.modificator.applyCriticalRateModifications(crit, this.context, events);
        if (this.effectiveness() == PokemonType.SUPER_EFFECTIVE && crit > 40)
            crit = 40;
        return this.context.floor.random.nextInt(100) < crit;
    }

    protected double damageMultiplier(boolean critical, ArrayList<Event> events) {
        double multiplier = 1;
        multiplier *= this.effectiveness();
        if (this.context.event.usedMove.isStab())
            multiplier *= 1.5;
        if (critical) {
            multiplier *= 1.5;
            events.add(new MessageEvent(this.context.floor, this.context.event, new Message("move.critical")));
        }

        multiplier *= this.modificator.damageMultiplier(this.context, events);
        return multiplier;
    }

    protected int defenseStat(ArrayList<Event> events) {
        Stat def = this.context.move.getCategory() == MoveCategory.Special ? Stat.SpecialDefense : Stat.Defense;
        int defStage = this.context.target.stats.getStage(def);
        defStage = this.modificator.applyStatStageModifications(def, defStage, this.context, events);

        DungeonStats stats = this.context.target.stats.clone();
        stats.setStage(def, defStage);
        double defense = stats.getStat(def);
        defense = this.modificator.applyStatModifications(def, defense, this.context, events);
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
        int evStage = this.context.target.stats.getStage(ev);
        evStage = this.modificator.applyStatStageModifications(ev, evStage, this.context, events);

        DungeonStats stats = this.context.target.stats.clone();
        stats.setStage(ev, evStage);
        double evasion = stats.getStat(ev);
        evasion = this.modificator.applyStatModifications(ev, evasion, this.context, events);
        if (evasion < 0)
            evasion = 0;
        if (evasion > 999)
            evasion = 999;

        // Reminder : Great evasion value actually means the monster is LESS likely to evade.

        return evasion;
    }

    public boolean misses(ArrayList<Event> events) {
        return this.moveLandingModule.misses(this.context, this, events);
    }

    protected int movePower() {
        return this.context.move.getPower() + this.context.learnedMove.getAddedLevel();
    }

    public MoveEffectCalculator setDamageModule(CalculatorDamageModule module) {
        this.damageModule = module;
        return this;
    }

    public MoveEffectCalculator setMoveLandingModule(CalculatorMoveLandingModule module) {
        this.moveLandingModule = module;
        return this;
    }

    private PokemonType type() {
        return this.context.move.getType(this.context.user.originalPokemon);
    }

}
