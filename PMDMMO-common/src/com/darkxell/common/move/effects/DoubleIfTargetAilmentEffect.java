package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.status.AppliedStatusCondition;

public class DoubleIfTargetAilmentEffect extends MoveEffect {

    public DoubleIfTargetAilmentEffect(int id) {
        super(id);
    }

    @Override
    public double damageMultiplier(MoveUse move, DungeonPokemon target, boolean isUser, Floor floor, String[] flags,
            ArrayList<DungeonEvent> events) {
        for (AppliedStatusCondition s : target.activeStatusConditions())
            if (s.condition.isAilment)
                return 2;
        return super.damageMultiplier(move, target, isUser, floor, flags, events);
    }

}
