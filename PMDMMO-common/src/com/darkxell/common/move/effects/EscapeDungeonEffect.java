package com.darkxell.common.move.effects;

import com.darkxell.common.event.dungeon.DungeonExitEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.MoveEvents;

public class EscapeDungeonEffect extends MoveEffect {

    public EscapeDungeonEffect(int id) {
        super(id);
    }

    @Override
    public void additionalEffects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed,
            MoveEvents effects) {
        super.additionalEffects(moveEvent, calculator, missed, effects);
        if (moveEvent.target.isTeamLeader())
            effects.createEffect(new DungeonExitEvent(moveEvent.floor, moveEvent, moveEvent.target.player()), moveEvent,
                    missed, false);
    }

}
