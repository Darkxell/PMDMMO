package com.darkxell.common.move.effects;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.pokemon.StatusConditionCreatedEvent;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.MoveEvents;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.status.StatusConditions;
import com.darkxell.common.weather.Weather;

public class SolarBeamEffect extends MoveEffect {
    public static final int RESULTING_MOVE = -707;

    public SolarBeamEffect(int id) {
        super(id);
    }

    @Override
    protected void mainEffects(MoveUse usedMove, DungeonPokemon target, String[] flags, Floor floor,
            MoveEffectCalculator calculator, boolean missed, MoveEvents effects) {
        if (missed)
            super.mainEffects(usedMove, target, flags, floor, calculator, missed, effects);
        else if (floor.currentWeather().weather == Weather.SUNNY) {
            LearnedMove move = new LearnedMove(RESULTING_MOVE);
            MoveSelectionEvent e = new MoveSelectionEvent(floor, eventSource, move, usedMove.user, usedMove.user.facing(), false);
            e.displayMessages = false;
            effects.createEffect(e, usedMove, target, floor, missed, false, null);
        } else
            effects.createEffect(
                    new StatusConditionCreatedEvent(floor,
                            StatusConditions.Solar_beam.create(floor, target, usedMove.user, floor.random)),
                    usedMove, target, floor, missed, usedMove.move.move().dealsDamage, target);
    }

}
