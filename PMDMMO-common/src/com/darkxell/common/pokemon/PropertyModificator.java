package com.darkxell.common.pokemon;

import java.util.ArrayList;
import java.util.HashSet;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.pokemon.BaseStats.Stat;

/** Utility object that stores various objects implementing AffectsPokemon, and applies all those modificators at once. */
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
    public int applyCriticalRateModifications(int critical, MoveUse move, DungeonPokemon target, Floor floor, ArrayList<DungeonEvent> events) {
        for (AffectsPokemon affecter : this.affecters)
            critical = affecter.applyCriticalRateModifications(critical, move, target, this.users.contains(affecter), floor, events);
        return critical;
    }

    /** Calls {@link AffectsPokemon#applyDamageModifications} on each affecter.
     * 
     * @param moveEvent TODO */
    public double applyDamageModifications(double damage, MoveUseEvent moveEvent, ArrayList<DungeonEvent> events) {
        for (AffectsPokemon affecter : this.affecters)
            damage = affecter.applyDamageModifications(damage, this.users.contains(affecter), moveEvent, events);
        return damage;
    }

    /** Calls {@link AffectsPokemon#applyCriticalRateModifications} on each affecter. */
    public double applyEffectivenessModifications(double effectiveness, MoveUse move, DungeonPokemon target, Floor floor) {
        for (AffectsPokemon affecter : this.affecters)
            effectiveness = affecter.applyEffectivenessModifications(effectiveness, move, target, this.users.contains(affecter), floor);
        return effectiveness;
    }

    /** Calls {@link AffectsPokemon#applyStatModifications} on each affecter. */
    public double applyStatModifications(Stat stat, double value, MoveUse move, DungeonPokemon target, Floor floor, MoveUseEvent moveEvent,
            ArrayList<DungeonEvent> events) {
        for (AffectsPokemon affecter : this.affecters)
            value = affecter.applyStatModifications(stat, value, move, target, this.users.contains(affecter), floor, moveEvent, events);
        return value;
    }

    /** Calls {@link AffectsPokemon#applyStatStageModifications} on each affecter. */
    public int applyStatStageModifications(Stat stat, int stage, MoveUse move, DungeonPokemon target, Floor floor, ArrayList<DungeonEvent> events) {
        for (AffectsPokemon affecter : this.affecters)
            stage = affecter.applyStatStageModifications(stat, stage, move, target, this.users.contains(affecter), floor, events);
        return stage;
    }

    public void clear() {
        this.affecters.clear();
        this.users.clear();
    }

    /** Calls {@link AffectsPokemon#damageMultiplier} on each affecter. */
    public double damageMultiplier(MoveUseEvent moveEvent, ArrayList<DungeonEvent> events) {
        double multiplier = 1;
        for (AffectsPokemon affecter : this.affecters)
            multiplier *= affecter.damageMultiplier(this.users.contains(affecter), moveEvent, events);
        return multiplier;
    }

}
