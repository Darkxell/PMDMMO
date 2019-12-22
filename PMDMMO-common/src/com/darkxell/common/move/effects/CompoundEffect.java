package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.MoveEvents;
import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.language.Localization;
import com.darkxell.common.util.language.Message;

public class CompoundEffect extends MoveEffect {

    public final MoveEffect[] effects;

    public CompoundEffect(int id, MoveEffect... effects) {
        super(id);
        this.effects = effects;
    }

    @Override
    public void additionalEffects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed,
            MoveEvents effects) {
        super.additionalEffects(moveEvent, calculator, missed, effects);

        for (MoveEffect e : this.effects)
            e.additionalEffects(moveEvent, calculator, missed, effects);
    }

    @Override
    public void additionalEffectsOnUse(MoveSelectionEvent moveEvent, Move move, ArrayList<Event> events) {
        super.additionalEffectsOnUse(moveEvent, move, events);

        for (MoveEffect e : this.effects)
            e.additionalEffectsOnUse(moveEvent, move, events);
    }

    @Override
    public int applyCriticalRateModifications(int critical, MoveUse move, DungeonPokemon target, boolean isUser,
            Floor floor, ArrayList<Event> events) {
        for (MoveEffect effect : this.effects)
            critical = effect.applyCriticalRateModifications(critical, move, target, isUser, floor, events);
        return critical;
    }

    @Override
    public double applyDamageModifications(double damage, boolean isUser, MoveUseEvent moveEvent,
            ArrayList<Event> events) {
        for (MoveEffect effect : this.effects)
            damage = effect.applyDamageModifications(damage, isUser, moveEvent, events);
        return damage;
    }

    @Override
    public double applyEffectivenessModifications(double effectiveness, MoveUse move, DungeonPokemon target,
            boolean isUser, Floor floor) {
        for (MoveEffect effect : this.effects)
            effectiveness = effect.applyEffectivenessModifications(effectiveness, move, target, isUser, floor);
        return effectiveness;
    }

    @Override
    public double applyStatModifications(Stat stat, double value, MoveUse move, DungeonPokemon target, boolean isUser,
            Floor floor, MoveUseEvent moveEvent, ArrayList<Event> events) {
        for (MoveEffect effect : this.effects)
            value = effect.applyStatModifications(stat, value, move, target, isUser, floor, moveEvent, events);
        return value;
    }

    @Override
    public int applyStatStageModifications(Stat stat, int stage, MoveUse move, DungeonPokemon target, boolean isUser,
            Floor floor, ArrayList<Event> events) {
        for (MoveEffect effect : this.effects)
            stage = effect.applyStatStageModifications(stat, stage, move, target, isUser, floor, events);
        return stage;
    }

    @Override
    public MoveEffectCalculator buildCalculator(MoveUseEvent moveEvent) {
        MoveEffectCalculator calculator = super.buildCalculator(moveEvent);
        for (MoveEffect effect : this.effects) {
            MoveEffectCalculator c = effect.buildCalculator(moveEvent);
            if (c != null)
                calculator = c;
        }
        return calculator;
    }

    @Override
    public Message description(Move move) {
        Message m = super.description(move);
        if (Localization.containsKey("move.info." + this.id))
            return m;
        for (MoveEffect e : this.effects) {
            Message em = e.descriptionBase(move);
            if (Localization.containsKey("move.info." + e.id))
                em = new Message("move.info." + e.id);

            if (!em.id.equals(DEFAULT_DESCRIPTION_ID)) { // If effect has default description, don't show it
                em.addReplacement("<move>", move.name());

                if (m.id.equals(DEFAULT_DESCRIPTION_ID)) // If compound has default description, don't show it
                    m = em;
                else { // Else add effect description to total description
                    m.addSuffix(" <br>");
                    m.addSuffix(em);
                }
            }
        }
        return m;
    }

}
