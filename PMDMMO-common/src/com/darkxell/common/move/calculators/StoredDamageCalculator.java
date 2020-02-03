package com.darkxell.common.move.calculators;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.effect.MoveEffectCalculator;
import com.darkxell.common.status.AppliedStatusCondition;
import com.darkxell.common.status.StatusConditions;
import com.darkxell.common.util.Logger;

public class StoredDamageCalculator extends MoveEffectCalculator {

    public StoredDamageCalculator(MoveUseEvent moveEvent) {
        super(moveEvent);
    }

    @Override
    public int compute(ArrayList<Event> events) {
        AppliedStatusCondition storer = this.move.user.getStatusCondition(StatusConditions.Bide);
        if (storer == null) {
            Logger.e("Pokemon used " + this.move().name() + " but had no Bide status!");
            return super.compute(events);
        }

        String[] flags = storer.listFlags();
        int stored = 0;
        for (String flag : flags)
            if (flag.startsWith("damage"))
                stored += Integer.parseInt(flag.split(":")[1]);
        return stored * 2;
    }

}
