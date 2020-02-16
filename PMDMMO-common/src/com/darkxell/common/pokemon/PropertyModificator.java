package com.darkxell.common.pokemon;

import java.util.ArrayList;
import java.util.HashSet;

import com.darkxell.common.event.Event;
import com.darkxell.common.model.pokemon.Stat;
import com.darkxell.common.move.MoveContext;

/**
 * Utility object that stores various objects implementing AffectsPokemon, and applies all those modificators at once.
 */
public class PropertyModificator {

    private ArrayList<AffectsPokemon> affecters = new ArrayList<>();
    private HashSet<AffectsPokemon> users = new HashSet<>();

    public void add(AffectsPokemon affecter) {
        this.affecters.add(affecter);
    }

    public void addUser(AffectsPokemon affecter) {
        this.add(affecter);
        this.users.add(affecter);
    }

    /** Calls {@link AffectsPokemon#applyCriticalRateModifications} on each affecter. */
    public int applyCriticalRateModifications(int critical, MoveContext context, ArrayList<Event> events) {
        for (AffectsPokemon affecter : this.affecters)
            critical = affecter.applyCriticalRateModifications(critical, context, this.users.contains(affecter),
                    events);
        return critical;
    }

    /**
     * Calls {@link AffectsPokemon#applyDamageModifications} on each affecter.
     * 
     * @param moveEvent - The concerned Move use event
     */
    public double applyDamageModifications(double damage, MoveContext context, ArrayList<Event> events) {
        for (AffectsPokemon affecter : this.affecters)
            damage = affecter.applyDamageModifications(damage, this.users.contains(affecter), context, events);
        return damage;
    }

    /** Calls {@link AffectsPokemon#applyCriticalRateModifications} on each affecter. */
    public double applyEffectivenessModifications(double effectiveness, MoveContext context) {
        for (AffectsPokemon affecter : this.affecters)
            effectiveness = affecter.applyEffectivenessModifications(effectiveness, context,
                    this.users.contains(affecter));
        return effectiveness;
    }

    /** Calls {@link AffectsPokemon#applyStatModifications} on each affecter. */
    public double applyStatModifications(Stat stat, double value, MoveContext context, ArrayList<Event> events) {
        for (AffectsPokemon affecter : this.affecters)
            value = affecter.applyStatModifications(stat, value, context, this.users.contains(affecter), events);
        return value;
    }

    /** Calls {@link AffectsPokemon#applyStatStageModifications} on each affecter. */
    public int applyStatStageModifications(Stat stat, int stage, MoveContext context, ArrayList<Event> events) {
        for (AffectsPokemon affecter : this.affecters)
            stage = affecter.applyStatStageModifications(stat, stage, context, this.users.contains(affecter), events);
        return stage;
    }

    public void clear() {
        this.affecters.clear();
        this.users.clear();
    }

    /** Calls {@link AffectsPokemon#damageMultiplier} on each affecter. */
    public double damageMultiplier(MoveContext context, ArrayList<Event> events) {
        double multiplier = 1;
        for (AffectsPokemon affecter : this.affecters)
            multiplier *= affecter.damageMultiplier(this.users.contains(affecter), context, events);
        return multiplier;
    }

}
