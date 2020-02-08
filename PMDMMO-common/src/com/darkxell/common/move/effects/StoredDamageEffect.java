package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.calculators.StoredDamageCalculator;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.move.effect.MoveEffectCalculator;
import com.darkxell.common.status.AppliedStatusCondition;
import com.darkxell.common.status.StatusConditions;
import com.darkxell.common.util.Logger;

public class StoredDamageEffect extends MoveEffect {

    @Override
    public void additionalEffectsOnUse(MoveSelectionEvent moveEvent, Move move, ArrayList<Event> events) {
        AppliedStatusCondition storer = moveEvent.usedMove().user.getStatusCondition(StatusConditions.Bide);
        if (storer == null)
            Logger.e("Pokemon used " + moveEvent.usedMove().move.move().name() + " but had no Bide status!");
        else
            storer.addFlag("attacked");
    }

    @Override
    public MoveEffectCalculator buildCalculator(MoveUseEvent moveEvent) {
        return new StoredDamageCalculator(moveEvent);
    }

    @Override
    public void effects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed,
            ArrayList<Event> effects, boolean createAdditionals) {}

}
