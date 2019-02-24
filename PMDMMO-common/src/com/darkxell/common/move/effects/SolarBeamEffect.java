package com.darkxell.common.move.effects;

import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.StatusConditionCreatedEvent;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.MoveEvents;
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.status.StatusConditions;
import com.darkxell.common.weather.Weather;

public class SolarBeamEffect extends MoveEffect {
    public static final int RESULTING_MOVE = -707;

    public SolarBeamEffect(int id) {
        super(id);
    }

    @Override
    protected void mainEffects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed, MoveEvents effects) {
        if (missed)
            super.mainEffects(moveEvent, calculator, missed, effects);
        else if (floor.currentWeather().weather == Weather.SUNNY) {
            LearnedMove move = new LearnedMove(RESULTING_MOVE);
            MoveSelectionEvent e = new MoveSelectionEvent(floor, eventSource, move, moveEvent.user, moveEvent.user.facing(), false);
            e.displayMessages = false;
            effects.createEffect(e, moveEvent, missed, false, null);
        } else
            effects.createEffect(
                    new StatusConditionCreatedEvent(floor,
                            eventSource, StatusConditions.Solar_beam.create(floor, target, moveEvent.user, floor.random)),
                    moveEvent, missed, moveEvent.move.move().dealsDamage, target);
    }

}
