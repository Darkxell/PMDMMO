package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.calculators.StoredDamageCalculator;
import com.darkxell.common.status.AppliedStatusCondition;
import com.darkxell.common.status.StatusConditions;
import com.darkxell.common.util.Logger;

public class StoredDamageEffect extends MoveEffect {

    public StoredDamageEffect(int id) {
        super(id);
    }

    @Override
    public MoveEffectCalculator buildCalculator(MoveUseEvent moveEvent) {
        return new StoredDamageCalculator(moveEvent, target, floor, flags);
    }

    @Override
    public void prepareUse(MoveSelectionEvent moveEvent, ArrayList<DungeonEvent> events) {
        super.prepareUse(moveEvent, events);
        AppliedStatusCondition storer = moveEvent.user.getStatusCondition(StatusConditions.Bide);
        if (storer == null)
            Logger.e("Pokemon used " + moveEvent.move.move().name() + " but had no Bide status!");
        else
            storer.addFlag("attacked");
    }

}
