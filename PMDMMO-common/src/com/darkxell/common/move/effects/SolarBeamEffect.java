package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.event.pokemon.StatusConditionCreatedEvent;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.move.effect.MoveEffectCalculator;
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.status.StatusConditions;
import com.darkxell.common.weather.Weather;

public class SolarBeamEffect extends MoveEffect {
    public static final int RESULTING_MOVE = -707;

    @Override
    public boolean alterMoveCreation(MoveSelectionEvent moveEvent, ArrayList<Event> events) {
        if (moveEvent.floor.currentWeather().weather == Weather.SUNNY) {
            LearnedMove move = new LearnedMove(RESULTING_MOVE);
            MoveSelectionEvent e = new MoveSelectionEvent(moveEvent.floor, moveEvent, move, moveEvent.usedMove().user,
                    moveEvent.usedMove().user.facing(), false);
            e.displayMessages = false;
            events.add(e);
            return true;
        }
        return false;
    }

    @Override
    public void effects(MoveContext context, MoveEffectCalculator calculator, boolean missed, ArrayList<Event> effects,
            boolean createAdditionals) {
        if (!createAdditionals) {
            effects.add(new StatusConditionCreatedEvent(context.floor, context.event,
                    StatusConditions.Solar_beam.create(context.floor, context.target, context.user)));
        }
    }

}
