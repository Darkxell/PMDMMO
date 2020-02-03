package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.Event.MessageEvent;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.calculators.FixedDamageCalculator;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.move.effect.MoveEffectCalculator;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.language.Message;

public class RandomFixedDamageEffect extends MoveEffect {

    private final int[] possibilities;

    public RandomFixedDamageEffect(int id, int... possibilities) {
        super(id);
        this.possibilities = possibilities;
    }

    @Override
    public MoveEffectCalculator buildCalculator(MoveUseEvent moveEvent) {

        for (String flag : moveEvent.flags())
            if (flag.startsWith("rfd=")) {
                return new FixedDamageCalculator(moveEvent, Integer.parseInt(flag.substring("rfd=".length())));
            }

        Logger.e("RandomFixedDamage Effect had no rfd flag");
        return super.buildCalculator(moveEvent);
    }

    public int[] possibilities() {
        return this.possibilities.clone();
    }

    @Override
    public void prepareUse(MoveSelectionEvent moveEvent, ArrayList<Event> events) {
        int chosen = moveEvent.floor.random.nextInt(this.possibilities.length);
        int damage = this.possibilities[chosen];

        events.add(new MessageEvent(moveEvent.floor, moveEvent,
                new Message("move.rfd.chosen." + moveEvent.usedMove().move.moveId() + "." + chosen)));

        super.prepareUse(moveEvent, events);

        for (Event event : events) {
            if (event instanceof MoveUseEvent) {
                event.addFlag("rfd=" + damage);
            }
        }
    }

}
