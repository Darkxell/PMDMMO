package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.calculator.MoveEffectCalculator;
import com.darkxell.common.move.calculator.modules.StoredDamageModule;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.status.AppliedStatusCondition;
import com.darkxell.common.status.StatusConditions;
import com.darkxell.common.util.Logger;

public class StoredDamageEffect extends MoveEffect {

    @Override
    public void additionalEffectsOnUse(MoveSelectionEvent context, Move move, ArrayList<Event> events) {
        AppliedStatusCondition storer = context.usedMove().user.getStatusCondition(StatusConditions.Bide);
        if (storer == null)
            Logger.e("Pokemon used " + context.usedMove().move.move().name() + " but had no Bide status!");
        else
            storer.addFlag("attacked");
    }

    @Override
    public void buildCalculator(MoveContext context, MoveEffectCalculator calculator) {
        calculator.setDamageModule(new StoredDamageModule());
    }

    @Override
    public void effects(MoveContext context, MoveEffectCalculator calculator, boolean missed, ArrayList<Event> effects,
            boolean createAdditionals) {}

}
