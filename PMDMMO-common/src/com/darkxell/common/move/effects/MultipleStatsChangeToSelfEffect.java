package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.event.stats.StatChangedEvent;
import com.darkxell.common.model.pokemon.Stat;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.calculator.MoveEffectCalculator;
import com.darkxell.common.move.effect.MoveEffect;

public class MultipleStatsChangeToSelfEffect extends MoveEffect {

    public final int probability;
    public final int stage;
    private Stat[] stats;

    public MultipleStatsChangeToSelfEffect(int stage, int probability, Stat... stats) {
        this.stage = stage;
        this.probability = probability;
        this.stats = stats;
    }

    @Override
    public void additionalEffectsOnUse(MoveSelectionEvent moveEvent, Move move, ArrayList<Event> events) {
        super.additionalEffectsOnUse(moveEvent, move, events);

        if (!events.isEmpty() && moveEvent.floor.random.nextDouble() * 100 < this.probability) {
            for (Stat stat : this.stats)
                events.add(
                        new StatChangedEvent(moveEvent.floor, moveEvent, moveEvent.usedMove().user, stat, this.stage));
        }
    }

    @Override
    public void effects(MoveContext context, MoveEffectCalculator calculator, boolean missed, ArrayList<Event> effects,
            boolean createAdditionals) {}

}
