package com.darkxell.common.move.calculator.modules;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.calculator.CalculatorDamageModule;
import com.darkxell.common.move.calculator.MoveEffectCalculator;
import com.darkxell.common.status.AppliedStatusCondition;
import com.darkxell.common.status.StatusConditions;
import com.darkxell.common.util.Logger;

public class StoredDamageModule implements CalculatorDamageModule {

    @Override
    public int compute(MoveContext context, MoveEffectCalculator calculator, ArrayList<Event> events) {
        AppliedStatusCondition storer = context.user.getStatusCondition(StatusConditions.Bide);
        if (storer == null) {
            Logger.e("Pokemon used " + context.move.name() + " but had no Bide status!");
            return new DefaultDamageModule().compute(context, calculator, events);
        }

        String[] flags = storer.listFlags();
        int stored = 0;
        for (String flag : flags)
            if (flag.startsWith("damage"))
                stored += Integer.parseInt(flag.split(":")[1]);
        return stored * 2;
    }

}
