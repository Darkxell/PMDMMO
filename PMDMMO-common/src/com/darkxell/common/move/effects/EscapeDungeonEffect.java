package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.dungeon.DungeonExitEvent;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.calculator.MoveEffectCalculator;
import com.darkxell.common.move.effect.MoveEffect;

public class EscapeDungeonEffect extends MoveEffect {

    @Override
    public void effects(MoveContext context, MoveEffectCalculator calculator, boolean missed, ArrayList<Event> effects,
            boolean createAdditionals) {

        if (!missed && context.target.isTeamLeader() && !createAdditionals) {
            effects.add(new DungeonExitEvent(context.floor, context.event, context.target.player()));
        }
    }

}
